
package net.specialattack.forge.discotek.client.lights;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelLaserRound;
import net.specialattack.forge.discotek.client.render.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.lights.ILightRenderHandler;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererHologram implements ILightRenderHandler {

    private ModelLaserRound modelLaserRound = new ModelLaserRound();

    @Override
    public void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap) {
        Minecraft.getMinecraft().mcProfiler.startSection("transformations");

        int side = light.getDirection();

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("model");
        RenderHelper.bindTexture(Assets.RADIAL_LASER_TEXTURE);
        this.modelLaserRound.renderAll();

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public void renderLight(TileEntityLight light, float partialTicks) {
        Minecraft.getMinecraft().mcProfiler.startSection("calculations");

        int color = light.getColor(partialTicks);
        float alpha = light.getBrightness(partialTicks);
        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        int side = light.getDirection();

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

        // Apply shader
        // Start rendering
        // Remove shader

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public boolean rendersLight() {
        return true;
    }

    @Override
    public boolean rendersFirst() {
        return true;
    }

    @Override
    public AxisAlignedBB getRenderingAABB(TileEntityLight light, float partialTicks) {
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

        float angle = (float) (light.getFocus(partialTicks) * Math.PI / 64.0F);
        float pitch = light.getPitch(partialTicks);

        float length = (pitch + 0.8F) * 10.0F + 6.0F;

        float xz = length * MathHelper.sin(angle);
        float y = length * MathHelper.cos(angle);

        return aabb.addCoord(xz, y, xz).addCoord(-xz, 0, -xz);
    }

}
