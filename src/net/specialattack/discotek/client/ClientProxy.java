
package net.specialattack.discotek.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.specialattack.discotek.CommonProxy;
import net.specialattack.discotek.Objects;
import net.specialattack.discotek.client.gui.GuiBasicController;
import net.specialattack.discotek.client.gui.GuiController;
import net.specialattack.discotek.client.gui.GuiFancyController;
import net.specialattack.discotek.client.gui.GuiLight;
import net.specialattack.discotek.client.render.DistanceComparator;
import net.specialattack.discotek.client.render.ItemRendererBlockLight;
import net.specialattack.discotek.client.render.ItemRendererLens;
import net.specialattack.discotek.client.render.tileentity.TileEntityLightRenderer;
import net.specialattack.discotek.tileentity.TileEntityController;
import net.specialattack.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        MinecraftForgeClient.registerItemRenderer(Objects.itemLens.itemID, new ItemRendererLens());
        MinecraftForgeClient.registerItemRenderer(Objects.blockLight.blockID, new ItemRendererBlockLight());
    }

    private static HashSet<TileEntityLight> lights = new HashSet<TileEntityLight>();
    private static TreeSet<TileEntityLight> reusableLights = new TreeSet<TileEntityLight>(new DistanceComparator());

    public static void openLightGui(TileEntityLight light) {
        FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new GuiLight(light));
    }

    public static void openControllerGui(int type, TileEntityController controller) {
        if (type == 0) {
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new GuiBasicController(controller));
        }
        else if (type == 1) {
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new GuiController(controller));
        }
        else if (type == 2) {
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new GuiFancyController(controller));
        }
    }

    public static void addTile(TileEntityLight light) {
        lights.add(light);
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
        Minecraft.getMinecraft().mcProfiler.startSection("discotek");
        if (lights.isEmpty()) {
            Minecraft.getMinecraft().mcProfiler.endSection();
            return;
        }
        Minecraft.getMinecraft().mcProfiler.startSection("invalidation");
        Iterator<TileEntityLight> iterator = lights.iterator();

        while (iterator.hasNext()) {
            TileEntityLight light = iterator.next();

            if (light.isNotValid()) {
                iterator.remove();
            }
        }

        if (lights.isEmpty()) {
            Minecraft.getMinecraft().mcProfiler.endSection();
            Minecraft.getMinecraft().mcProfiler.endSection();
            return;
        }

        Minecraft.getMinecraft().mcProfiler.endStartSection("sorting");

        reusableLights.addAll(lights);

        Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

        TileEntityLightRenderer.lightOnly = true;

        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.partialTicks;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
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
        GL11.glDepthMask(true);
        GL11.glPopMatrix();

        TileEntityLightRenderer.lightOnly = false;
        reusableLights.clear();

        Minecraft.getMinecraft().mcProfiler.endSection();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

}
