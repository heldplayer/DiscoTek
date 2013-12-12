
package net.specialattack.discotek.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import me.heldplayer.util.HeldCore.client.MC;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.specialattack.discotek.CommonProxy;
import net.specialattack.discotek.Objects;
import net.specialattack.discotek.client.gui.GuiLight;
import net.specialattack.discotek.client.lights.LightRendererDimmer;
import net.specialattack.discotek.client.lights.LightRendererFresnel;
import net.specialattack.discotek.client.lights.LightRendererMap;
import net.specialattack.discotek.client.lights.LightRendererRadialLaser;
import net.specialattack.discotek.client.render.DistanceComparator;
import net.specialattack.discotek.client.render.ItemRendererBlockLight;
import net.specialattack.discotek.client.render.ItemRendererLens;
import net.specialattack.discotek.client.render.tileentity.TileEntityLightRenderer;
import net.specialattack.discotek.controllers.IControllerInstance;
import net.specialattack.discotek.lights.ILightRenderHandler;
import net.specialattack.discotek.tileentity.TileEntityController;
import net.specialattack.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLight.class, new TileEntityLightRenderer());

        Objects.blockLight.setLightRenderer(0, new LightRendererFresnel());
        Objects.blockLight.setLightRenderer(1, new LightRendererMap(false));
        Objects.blockLight.setLightRenderer(2, new LightRendererMap(true));
        Objects.blockLight.setLightRenderer(3, new LightRendererDimmer());
        Objects.blockLight.setLightRenderer(4, new LightRendererRadialLaser());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        MinecraftForgeClient.registerItemRenderer(Objects.itemLens.itemID, new ItemRendererLens());
        MinecraftForgeClient.registerItemRenderer(Objects.blockLight.blockID, new ItemRendererBlockLight());
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

    public static void addTile(TileEntityLight light) {
        ILightRenderHandler handler = light.getRenderHandler();
        if (handler != null && handler.rendersLight()) {
            lights.add(light);
        }
    }

    public static void removeTile(TileEntityLight light) {
        lights.remove(light);
    }

    @ForgeSubscribe
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) {
            lights.clear();
        }
    }

    @ForgeSubscribe
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (event.world.isRemote) {
            @SuppressWarnings("unchecked")
            Map<ChunkPosition, TileEntity> tiles = (Map<ChunkPosition, TileEntity>) event.getChunk().chunkTileEntityMap;
            Iterator<TileEntity> iterator = tiles.values().iterator();

            while (iterator.hasNext()) {
                TileEntity light = iterator.next();

                if (light instanceof TileEntityLight) {
                    lights.remove(light);
                }
            }
        }
    }

    @ForgeSubscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        MC.getMinecraft().mcProfiler.startSection("discotek");
        if (lights.isEmpty()) {
            MC.getMinecraft().mcProfiler.endSection();
            return;
        }
        MC.getMinecraft().mcProfiler.startSection("invalidation");
        Iterator<TileEntityLight> iterator = lights.iterator();

        while (iterator.hasNext()) {
            TileEntityLight light = iterator.next();

            if (light.isNotValid()) {
                iterator.remove();
            }
        }

        if (lights.isEmpty()) {
            MC.getMinecraft().mcProfiler.endSection();
            MC.getMinecraft().mcProfiler.endSection();
            return;
        }

        MC.getMinecraft().mcProfiler.endStartSection("sorting");

        reusableLights.addAll(lights);

        MC.getMinecraft().mcProfiler.endStartSection("culling");

        EntityClientPlayerMP player = MC.getMinecraft().thePlayer;

        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.partialTicks;

        Frustrum frustrum = new Frustrum();
        frustrum.setPosition(d0, d1, d2);

        iterator = reusableLights.iterator();

        while (iterator.hasNext()) {
            TileEntityLight light = iterator.next();

            AxisAlignedBB aabb = light.getRenderHandler().getRenderingAABB(light, event.partialTicks).offset(d0, d1, d2);

            if (!frustrum.isBoundingBoxInFrustum(aabb)) {
                iterator.remove();
            }
        }

        MC.getMinecraft().mcProfiler.endStartSection("rendering");

        TileEntityLightRenderer.lightOnly = true;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        MC.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPolygonOffset(-3.0F, -3.0F);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        iterator = reusableLights.iterator();

        while (iterator.hasNext()) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            TileEntityLight light = iterator.next();
            double d3 = (double) light.xCoord - d0;
            double d4 = (double) light.yCoord - d1;
            double d5 = (double) light.zCoord - d2;

            if (light.isNotValid()) {
                iterator.remove();
            }

            if (d3 * d3 + d4 * d4 + d5 * d5 < 65536.0D) {
                TileEntityRenderer.instance.renderTileEntityAt(light, d3, d4, d5, event.partialTicks);
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPolygonOffset(0.0F, 0.0F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();

        TileEntityLightRenderer.lightOnly = false;
        reusableLights.clear();

        MC.getMinecraft().mcProfiler.endSection();
        MC.getMinecraft().mcProfiler.endSection();
    }

}
