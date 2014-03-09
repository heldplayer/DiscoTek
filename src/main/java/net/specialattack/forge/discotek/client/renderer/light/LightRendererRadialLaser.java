
package net.specialattack.forge.discotek.client.renderer.light;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelLaserRound;
import net.specialattack.forge.discotek.client.renderer.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightRadialLaserInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererRadialLaser implements ILightRenderHandler {

    private ModelLaserRound modelLaserRound = new ModelLaserRound();

    private ILightInstance instance = new LightRadialLaserInstance(null);

    @Override
    public void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap) {
        Minecraft.getMinecraft().mcProfiler.startSection("transformations");

        int side = light.getInteger("direction", partialTicks);

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

        float red = (light.getInteger("red", partialTicks) & 0xFF) / 255.0F;
        float green = (light.getInteger("green", partialTicks) & 0xFF) / 255.0F;
        float blue = (light.getInteger("blue", partialTicks) & 0xFF) / 255.0F;
        float brightness = light.getFloat("brightness", partialTicks);
        float alpha = (0.9F * brightness) + 0.1F;

        float focus = light.getFloat("focus", partialTicks);
        float angle = (float) (focus * Math.PI / 64.0F);
        float length1 = (focus + 0.8F) * 2.0F + 1.0F;
        float length2 = length1 + 5.0F;
        float height1 = MathHelper.cos(angle) * length1;
        float height2 = MathHelper.cos(angle) * length2;
        float distance1 = MathHelper.sin(angle) * length1;
        float distance2 = MathHelper.sin(angle) * length2;
        float rotation = light.getFloat("rotation", partialTicks);

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        int side = light.getInteger("direction", partialTicks);

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glRotatef(rotation * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);

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

        float focus = light.getFloat("focus", partialTicks);
        float angle = (float) (focus * Math.PI / 64.0F);
        float length = (focus + 0.8F) * 2.0F + 6.0F;

        float xz = length * MathHelper.sin(angle);
        float y = length * MathHelper.cos(angle);

        return aabb.addCoord(xz, y, xz).addCoord(-xz, 0, -xz);
    }

    @Override
    public ILightInstance getRenderInstance() {
        return this.instance;
    }

}
