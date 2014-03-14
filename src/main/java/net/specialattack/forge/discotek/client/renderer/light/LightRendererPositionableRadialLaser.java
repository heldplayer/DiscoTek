
package net.specialattack.forge.discotek.client.renderer.light;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelLightMoverBase;
import net.specialattack.forge.discotek.client.model.ModelLightMoverLaser;
import net.specialattack.forge.discotek.client.model.ModelLightTiltArms;
import net.specialattack.forge.discotek.client.renderer.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightPositionableRadialLaserInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererPositionableRadialLaser implements ILightRenderHandler {

    private ModelLightMoverLaser modelLightMoverLaser = new ModelLightMoverLaser();
    private ModelLightMoverBase modelLightMoverBase = new ModelLightMoverBase();
    private ModelLightTiltArms modelLightTiltArms = new ModelLightTiltArms();

    private ILightInstance instance = new LightPositionableRadialLaserInstance(null);

    @Override
    public void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap) {
        Minecraft.getMinecraft().mcProfiler.startSection("calculations");

        float pitch = light.getFloat("pitch", partialTicks);
        float rotation = light.getFloat("rotation", partialTicks);

        int side = light.getInteger("direction", partialTicks) % 6;

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("model");

        RenderHelper.bindTexture(Assets.POSITIONAL_LASER_TEXTURE);

        this.modelLightMoverBase.setRotations(0, 0);
        this.modelLightMoverBase.renderAll();
        this.modelLightTiltArms.setRotations(0, rotation);
        this.modelLightTiltArms.renderAll();

        GL11.glTranslatef(0, 0.15F, 0);
        this.modelLightMoverLaser.setRotations(pitch, rotation);
        this.modelLightMoverLaser.render();

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
        float angle = (float) (focus * Math.PI / 128.0F);
        float length1 = (focus + 0.8F) * 2.0F + 1.0F;
        float length2 = length1 + 6.0F;
        float length1b = MathHelper.cos(angle) * length1;
        float length2b = MathHelper.cos(angle) * length2;
        float distance1 = MathHelper.sin(angle) * length1;
        float distance2 = MathHelper.sin(angle) * length2;
        float pitch = light.getFloat("pitch", partialTicks);
        float rotation = light.getFloat("rotation", partialTicks);

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        int side = light.getInteger("direction", partialTicks) % 6;

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glTranslatef(0.0F, 0.15F, 0.0F);
        GL11.glRotatef(rotation * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(pitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

        GL11.glBegin(GL11.GL_LINES);
        int max = 8;
        for (int i = 0; i < max; i++) {
            float currentAngle = (float) i / (float) max * 4.0F;
            float startX = MathHelper.cos(currentAngle) * 0.2F;
            float startZ = MathHelper.sin(currentAngle) * 0.2F;
            float posXZ1 = MathHelper.cos(currentAngle) * (distance1 + 0.2F);
            float posY1 = MathHelper.sin(currentAngle) * (distance1 + 0.2F);
            float posXZ2 = MathHelper.cos(currentAngle) * (distance2 + 0.2F);
            float posY2 = MathHelper.sin(currentAngle) * (distance2 + 0.2F);
            GL11.glColor4f(red, green, blue, alpha);
            GL11.glVertex3f(startX, startZ, 0.0F);
            GL11.glVertex3f(posXZ1, posY1, -length1b);
            GL11.glVertex3f(posXZ1, posY1, -length1b);
            GL11.glColor4f(red, green, blue, 0.0F);
            GL11.glVertex3f(posXZ2, posY2, -length2b);
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

        float pitch = light.getFloat("pitch", partialTicks); // +-Y
        float rotation = light.getFloat("rotation", partialTicks); // +-XZ
        float angle = (float) (light.getFloat("focus", partialTicks) * Math.PI / 128.0F);
        float length = (light.getFloat("length", partialTicks) + 0.8F) * 2.0F + 6.0F;
        float lightLength = MathHelper.cos(angle) * length;
        float distance = MathHelper.sin(angle) * length;

        float x = lightLength * -net.minecraft.util.MathHelper.sin(rotation) * net.minecraft.util.MathHelper.cos(pitch);
        float z = lightLength * -net.minecraft.util.MathHelper.cos(rotation) * net.minecraft.util.MathHelper.cos(pitch);
        float y = lightLength * net.minecraft.util.MathHelper.sin(pitch);

        float x1 = x + distance;
        float z1 = z + distance;
        float y1 = y + distance;

        float x2 = x - distance;
        float z2 = z - distance;
        float y2 = y - distance;

        return aabb.addCoord(x1, y1, z1).addCoord(x2, y2, z2);
    }

    @Override
    public ILightInstance getRenderInstance() {
        return this.instance;
    }

}
