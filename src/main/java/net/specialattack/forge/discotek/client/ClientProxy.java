package net.specialattack.forge.discotek.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.specialattack.forge.core.client.MC;
import net.specialattack.forge.discotek.CommonProxy;
import net.specialattack.forge.discotek.Objects;
import net.specialattack.forge.discotek.client.render.DistanceComparator;
import net.specialattack.forge.discotek.client.render.IPostRenderer;
import net.specialattack.forge.discotek.client.render.ItemRendererBlockColoredLamp;
import net.specialattack.forge.discotek.client.render.ItemRendererMultiPass;
import net.specialattack.forge.discotek.sound.ChannelDiscoTek;
import net.specialattack.forge.discotek.sound.LibraryDiscoTek;
import org.lwjgl.opengl.GL11;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static boolean beat = false;
    public static HashSet<ChannelDiscoTek> channels = new HashSet<ChannelDiscoTek>();
    private static HashSet<IPostRenderer> lights = new HashSet<IPostRenderer>();
    private static TreeSet<IPostRenderer> reusableLights = new TreeSet<IPostRenderer>(new DistanceComparator());
    private int totalCount = 0;
    private int renderedCount = 0;

    public static void addTile(IPostRenderer light) {
        if (light != null && light.rendersLight()) {
            ClientProxy.lights.add(light);
        }
    }

    public static void removeTile(IPostRenderer light) {
        ClientProxy.lights.remove(light);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        //Objects.blockLight.setLightRenderer(0, new LightRendererFresnel());
        //Objects.blockLight.setLightRenderer(1, new LightRendererMap());
        //Objects.blockLight.setLightRenderer(2, new LightRendererMapLED());
        //Objects.blockLight.setLightRenderer(3, new LightRendererDimmer());
        //Objects.blockLight.setLightRenderer(4, new LightRendererRadialLaser());
        //Objects.blockLight.setLightRenderer(5, new LightRendererHologram());
        //Objects.blockLight.setLightRenderer(6, new LightRendererPositionableRadialLaser());

        MinecraftForge.EVENT_BUS.register(this);

        FMLCommonHandler.instance().bus().register(this);// FIXME: temp
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLight.class, new TileEntityLightRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        MinecraftForgeClient.registerItemRenderer(Objects.itemLens, new ItemRendererMultiPass());
        MinecraftForgeClient.registerItemRenderer(Objects.itemColorConfigurator, new ItemRendererMultiPass());
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objects.blockLight), new ItemRendererBlockLight());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objects.blockColoredLamp), new ItemRendererBlockColoredLamp());
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (!ClientProxy.channels.isEmpty()) {
            Minecraft mc = MC.getMc();
            ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

            double width = resolution.getScaledWidth_double();
            double height = resolution.getScaledHeight_double();
            double splitHeight = height / ClientProxy.channels.size();

            int i = 0;
            for (ChannelDiscoTek channel : ClientProxy.channels) {
                channel.render(0.0D, splitHeight * i, width, splitHeight);
                i++;
            }
        }
    }

    @SubscribeEvent
    public void onSoundSetup(SoundSetupEvent event) {
        Objects.log.info("Replacing default soundsystem library with custom one");
        try {
            SoundSystemConfig.removeLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.addLibrary(LibraryDiscoTek.class);
        } catch (SoundSystemException e) {
            Objects.log.error("Failed setting up custom soundsystem", e);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) {
            ClientProxy.lights.clear();
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (event.world.isRemote) {
            @SuppressWarnings("unchecked") Map<ChunkPosition, TileEntity> tiles = event.getChunk().chunkTileEntityMap;

            for (TileEntity light : tiles.values()) {
                if (light instanceof IPostRenderer) {
                    ClientProxy.lights.remove(light);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayText(RenderGameOverlayEvent.Text event) {
        if (MC.getGameSettings().showDebugInfo) {
            event.left.add("DiscoTek Lights: " + this.renderedCount + "/" + this.totalCount);
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = MC.getMc();
        mc.mcProfiler.startSection("discotek");
        this.totalCount = 0;
        this.renderedCount = 0;
        if (ClientProxy.lights.isEmpty()) {
            MC.getMc().mcProfiler.endSection();
            return;
        }
        mc.mcProfiler.startSection("invalidation");
        Iterator<IPostRenderer> iterator = ClientProxy.lights.iterator();

        while (iterator.hasNext()) {
            IPostRenderer light = iterator.next();

            if (light.shouldRemoveRenderer()) {
                iterator.remove();
            }
        }

        if (ClientProxy.lights.isEmpty()) {
            mc.mcProfiler.endSection();
            mc.mcProfiler.endSection();
            return;
        }

        mc.mcProfiler.endStartSection("sorting");

        ClientProxy.reusableLights.addAll(ClientProxy.lights);

        mc.mcProfiler.endStartSection("culling");

        EntityClientPlayerMP player = mc.thePlayer;

        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

        Frustrum frustrum = new Frustrum();
        frustrum.setPosition(playerX, playerY, playerZ);

        iterator = ClientProxy.reusableLights.iterator();

        while (iterator.hasNext()) {
            this.totalCount++;

            IPostRenderer light = iterator.next();

            if (light != null) {
                AxisAlignedBB aabb = light.getRenderingAABB(light, event.partialTicks).offset(light.getPosX(), light.getPosY(), light.getPosZ());

                if (!frustrum.isBoundingBoxInFrustum(aabb)) {
                    //iterator.remove();
                    continue;
                }
            } else {
                iterator.remove();
                continue;
            }

            this.renderedCount++;
        }

        mc.mcProfiler.endStartSection("rendering");

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //MC.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        //GL11.glPolygonOffset(-3.0F, -3.0F);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        iterator = ClientProxy.reusableLights.iterator();

        while (iterator.hasNext()) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            IPostRenderer light = iterator.next();
            double d3 = light.getPosX() - playerX;
            double d4 = light.getPosY() - playerY;
            double d5 = light.getPosZ() - playerZ;

            if (light.shouldRemoveRenderer()) {
                iterator.remove();
            }

            if (d3 * d3 + d4 * d4 + d5 * d5 < 65536.0D) {
                light.renderPost(d3, d4, d5, event.partialTicks);
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        //GL11.glPolygonOffset(0.0F, 0.0F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();

        ClientProxy.reusableLights.clear();

        mc.mcProfiler.endSection();
        mc.mcProfiler.endSection();
    }
}
