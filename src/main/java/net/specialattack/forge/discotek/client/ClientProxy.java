
package net.specialattack.forge.discotek.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.specialattack.forge.core.client.MC;
import net.specialattack.forge.discotek.CommonProxy;
import net.specialattack.forge.discotek.Objects;
import net.specialattack.forge.discotek.client.gui.GuiLight;
import net.specialattack.forge.discotek.client.render.DistanceComparator;
import net.specialattack.forge.discotek.client.render.ItemRendererBlockColoredLamp;
import net.specialattack.forge.discotek.client.render.ItemRendererBlockLight;
import net.specialattack.forge.discotek.client.render.ItemRendererMultiPass;
import net.specialattack.forge.discotek.client.renderer.light.ILightRenderHandler;
import net.specialattack.forge.discotek.client.renderer.light.LightRendererDimmer;
import net.specialattack.forge.discotek.client.renderer.light.LightRendererFresnel;
import net.specialattack.forge.discotek.client.renderer.light.LightRendererHologram;
import net.specialattack.forge.discotek.client.renderer.light.LightRendererMap;
import net.specialattack.forge.discotek.client.renderer.light.LightRendererMapLED;
import net.specialattack.forge.discotek.client.renderer.light.LightRendererPositionableRadialLaser;
import net.specialattack.forge.discotek.client.renderer.light.LightRendererRadialLaser;
import net.specialattack.forge.discotek.client.renderer.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.controller.instance.IControllerInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityController;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        Objects.blockLight.setLightRenderer(0, new LightRendererFresnel());
        Objects.blockLight.setLightRenderer(1, new LightRendererMap());
        Objects.blockLight.setLightRenderer(2, new LightRendererMapLED());
        Objects.blockLight.setLightRenderer(3, new LightRendererDimmer());
        Objects.blockLight.setLightRenderer(4, new LightRendererRadialLaser());
        Objects.blockLight.setLightRenderer(5, new LightRendererHologram());
        Objects.blockLight.setLightRenderer(6, new LightRendererPositionableRadialLaser());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLight.class, new TileEntityLightRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        MinecraftForgeClient.registerItemRenderer(Objects.itemLens, new ItemRendererMultiPass());
        MinecraftForgeClient.registerItemRenderer(Objects.itemColorConfigurator, new ItemRendererMultiPass());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objects.blockLight), new ItemRendererBlockLight());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objects.blockColoredLamp), new ItemRendererBlockColoredLamp());

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void openControllerGui(TileEntityController tile) {
        if (tile != null) {
            IControllerInstance controller = tile.getControllerInstance();
            if (controller != null) {
                controller.openGui(MC.getPlayer(), Side.CLIENT);
            }
        }
    }

    @Override
    public void openLightGui(TileEntityLight tile) {
        if (tile != null) {
            FMLClientHandler.instance().displayGuiScreen(MC.getPlayer(), new GuiLight(tile));
        }
    }

    private static HashSet<TileEntityLight> lights = new HashSet<TileEntityLight>();
    private static TreeSet<TileEntityLight> reusableLights = new TreeSet<TileEntityLight>(new DistanceComparator());
    private int totalCount = 0;
    private int renderedCount = 0;

    public static void addTile(TileEntityLight light) {
        ILightRenderHandler handler = light.getRenderHandler();
        if (handler != null && handler.rendersLight()) {
            ClientProxy.lights.add(light);
        }
    }

    public static void removeTile(TileEntityLight light) {
        ClientProxy.lights.remove(light);
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
            @SuppressWarnings("unchecked")
            Map<ChunkPosition, TileEntity> tiles = event.getChunk().chunkTileEntityMap;
            Iterator<TileEntity> iterator = tiles.values().iterator();

            while (iterator.hasNext()) {
                TileEntity light = iterator.next();

                if (light instanceof TileEntityLight) {
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
        MC.getMinecraft().mcProfiler.startSection("discotek");
        this.totalCount = 0;
        this.renderedCount = 0;
        if (ClientProxy.lights.isEmpty()) {
            MC.getMinecraft().mcProfiler.endSection();
            return;
        }
        MC.getMinecraft().mcProfiler.startSection("invalidation");
        Iterator<TileEntityLight> iterator = ClientProxy.lights.iterator();

        while (iterator.hasNext()) {
            TileEntityLight light = iterator.next();

            if (light.isNotValid()) {
                iterator.remove();
            }
        }

        if (ClientProxy.lights.isEmpty()) {
            MC.getMinecraft().mcProfiler.endSection();
            MC.getMinecraft().mcProfiler.endSection();
            return;
        }

        MC.getMinecraft().mcProfiler.endStartSection("sorting");

        ClientProxy.reusableLights.addAll(ClientProxy.lights);

        MC.getMinecraft().mcProfiler.endStartSection("culling");

        EntityClientPlayerMP player = MC.getMinecraft().thePlayer;

        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

        Frustrum frustrum = new Frustrum();
        frustrum.setPosition(playerX, playerY, playerZ);

        iterator = ClientProxy.reusableLights.iterator();

        while (iterator.hasNext()) {
            this.totalCount++;

            TileEntityLight light = iterator.next();

            ILightRenderHandler handler = light.getRenderHandler();

            if (handler != null) {
                AxisAlignedBB aabb = handler.getRenderingAABB(light, event.partialTicks).offset(light.xCoord, light.yCoord, light.zCoord);

                if (!frustrum.isBoundingBoxInFrustum(aabb)) {
                    iterator.remove();
                    continue;
                }
            }
            else {
                iterator.remove();
                continue;
            }

            this.renderedCount++;
        }

        MC.getMinecraft().mcProfiler.endStartSection("rendering");

        TileEntityLightRenderer.lightOnly = true;

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
            TileEntityLight light = iterator.next();
            double d3 = light.xCoord - playerX;
            double d4 = light.yCoord - playerY;
            double d5 = light.zCoord - playerZ;

            if (light.isNotValid()) {
                iterator.remove();
            }

            if (d3 * d3 + d4 * d4 + d5 * d5 < 65536.0D) {
                TileEntityRendererDispatcher.instance.renderTileEntityAt(light, d3, d4, d5, event.partialTicks);
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

        TileEntityLightRenderer.lightOnly = false;
        ClientProxy.reusableLights.clear();

        MC.getMinecraft().mcProfiler.endSection();
        MC.getMinecraft().mcProfiler.endSection();
    }

}
