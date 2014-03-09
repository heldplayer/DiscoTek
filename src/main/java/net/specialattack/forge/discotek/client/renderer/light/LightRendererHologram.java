
package net.specialattack.forge.discotek.client.renderer.light;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelHologramPad;
import net.specialattack.forge.discotek.client.renderer.entity.RenderPlayerCustom;
import net.specialattack.forge.discotek.client.renderer.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightHologramInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererHologram implements ILightRenderHandler {

    private ModelHologramPad modelHologramPad = new ModelHologramPad();

    private ILightInstance instance = new LightHologramInstance(null);

    private static RenderPlayerCustom render = new RenderPlayerCustom();
    static {
        LightRendererHologram.render.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap) {
        Minecraft.getMinecraft().mcProfiler.startSection("transformations");

        int side = light.getInteger("direction", partialTicks);

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("model");
        RenderHelper.bindTexture(Assets.HOLOGRAM_TEXTURE);
        this.modelHologramPad.renderAll();

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public void renderLight(TileEntityLight light, float partialTicks) {
        Minecraft.getMinecraft().mcProfiler.startSection("calculations");

        float brightness = light.getFloat("brightness", partialTicks);
        float red = (light.getInteger("red", partialTicks) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float green = (light.getInteger("green", partialTicks) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float blue = (light.getInteger("blue", partialTicks) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float alpha = (0.9F * brightness) + 0.1F;

        float pitch = light.getFloat("pitch", partialTicks) * 90.0F;
        float rotation = light.getFloat("rotation", partialTicks) * 90.0F;
        float size = light.getFloat("size", partialTicks);
        float scale = size / 10.0F + 0.5F;
        float rotationHead = light.getFloat("headRotation", partialTicks);

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        int side = light.getInteger("direction", partialTicks);

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.5F, 0.0F);
        GL11.glScalef(scale, scale, scale);

        Minecraft.getMinecraft().mcProfiler.endStartSection("entity");

        ILightInstance instance = light.getLightInstance();

        if (instance == null || !(instance instanceof LightHologramInstance)) {
            Minecraft.getMinecraft().mcProfiler.endSection();
            return;
        }

        LightHologramInstance hologram = (LightHologramInstance) instance;

        GL11.glColor4f(red, green, blue, alpha);

        if (hologram.player != null) {
            if (RenderManager.instance.livingPlayer != null) {
                hologram.player.worldObj = light.getWorld();
                hologram.player.rotationPitch = hologram.player.prevRotationPitch = pitch;
                hologram.player.rotationYawHead = hologram.player.prevRotationYawHead = rotationHead;

                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);

                LightRendererHologram.render.doRender(hologram.player, 0, 0, 0, 0, 1);

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
                GL11.glPopMatrix();
            }
        }

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public boolean rendersLight() {
        return true;
    }

    @Override
    public boolean rendersFirst() {
        return false;
    }

    @Override
    public AxisAlignedBB getRenderingAABB(TileEntityLight light, float partialTicks) {
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

        float focus = light.getFloat("focus", partialTicks);
        double point = focus / 20.0D;
        double height = focus / 10.0D + 0.5D;

        return aabb.addCoord(point, height, point).addCoord(-point, height, -point);
    }

    @Override
    public ILightInstance getRenderInstance() {
        return this.instance;
    }

}
