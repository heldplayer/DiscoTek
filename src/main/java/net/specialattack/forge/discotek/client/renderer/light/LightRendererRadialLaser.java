
package net.specialattack.forge.discotek.client.renderer.light;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelLaserRound;
import net.specialattack.forge.discotek.client.renderer.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.light.ILightRenderHandler;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererRadialLaser implements ILightRenderHandler {

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
        float brightness = light.getBrightness(partialTicks);
        float red = ((color >> 16) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float green = ((color >> 8) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float blue = (color & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float alpha = (0.9F * brightness) + 0.1F;

        float angle = (float) (light.getFocus(partialTicks) * Math.PI / 64.0F);
        float length1 = (light.getLength(partialTicks) + 0.8F) * 10.0F + 1.0F;
        float length2 = (light.getLength(partialTicks) + 0.8F) * 10.0F + 6.0F;
        float height1 = MathHelper.cos(angle) * length1;
        float height2 = MathHelper.cos(angle) * length2;
        float distance1 = MathHelper.sin(angle) * length1;
        float distance2 = MathHelper.sin(angle) * length2;
        float rotation = light.getYaw(partialTicks) * (90F / (float) Math.PI);

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        int side = light.getDirection();

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glBegin(GL11.GL_LINES);
        int max = 32;
        for (int i = 0; i < max; i++) {
            float currentAngle = (float) i / (float) max * 4.0F;
            float startX = MathHelper.cos(currentAngle) * 0.2F;
            float startZ = MathHelper.sin(currentAngle) * 0.2F;
            float posX1 = MathHelper.cos(currentAngle) * (distance1 + 0.2F);
            float posZ1 = MathHelper.sin(currentAngle) * (distance1 + 0.2F);
            float posX2 = MathHelper.cos(currentAngle) * (distance2 + 0.2F);
            float posZ2 = MathHelper.sin(currentAngle) * (distance2 + 0.2F);
            GL11.glColor4f(red, green, blue, alpha);
            GL11.glVertex3f(startX, 0.0F, startZ);
            GL11.glVertex3f(posX1, height1, posZ1);
            GL11.glVertex3f(posX1, height1, posZ1);
            GL11.glColor4f(red, green, blue, 0.0F);
            GL11.glVertex3f(posX2, height2, posZ2);
        }
        GL11.glEnd();

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
