
package net.specialattack.forge.discotek.client.renderer.light;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelHologramPad;
import net.specialattack.forge.discotek.client.renderer.entity.RenderPlayerCustom;
import net.specialattack.forge.discotek.client.renderer.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.light.ILightRenderHandler;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererHologram implements ILightRenderHandler {

    private ModelHologramPad modelHologramPad = new ModelHologramPad();

    private static RenderPlayerCustom render = new RenderPlayerCustom();
    static {
        render.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap) {
        Minecraft.getMinecraft().mcProfiler.startSection("transformations");

        int side = light.getDirection();

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

        int color = light.getColor(partialTicks);
        float brightness = light.getBrightness(partialTicks);
        float red = ((color >> 16) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float green = ((color >> 8) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float blue = (color & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float alpha = (0.9F * brightness) + 0.1F;

        float yaw = light.getYaw(partialTicks) * (90F / (float) Math.PI);
        float pitch = light.getPitch(partialTicks) * 100.0F;
        float scale = light.getFocus(partialTicks) / 10.0F + 0.5F;
        float yawHead = light.getLength(partialTicks) * 9.0F - 90.0F;

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        int side = light.getDirection();

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.5F, 0.0F);
        GL11.glScalef(scale, scale, scale);

        Minecraft.getMinecraft().mcProfiler.endStartSection("entity");

        GL11.glColor4f(red, green, blue, alpha);

        if (light.getSpecialString() != null && !light.getSpecialString().isEmpty()) {
            if (light.lightObj == null || !(light.lightObj instanceof EntityOtherPlayerMP)) {
                light.lightObj = new EntityOtherPlayerMP(light.getWorld(), new GameProfile(null, light.getSpecialString()));
            }
            if (light.lightObj instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP player = (EntityOtherPlayerMP) light.lightObj;

                if (!player.getCommandSenderName().equals(light.getSpecialString())) {
                    light.lightObj = new EntityOtherPlayerMP(light.getWorld(), new GameProfile(null, light.getSpecialString()));
                }

                if (RenderManager.instance.livingPlayer != null) {
                    player.worldObj = light.getWorld();
                    player.rotationPitch = player.prevRotationPitch = pitch;
                    player.rotationYawHead = player.prevRotationYawHead = yawHead;

                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_BLEND);

                    LightRendererHologram.render.doRender(player, 0, 0, 0, 0, 1);

                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
                    GL11.glPopMatrix();
                }
            }
        }
        else {
            light.lightObj = null;
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

        double point = light.getFocus(partialTicks) / 20.0D;
        double height = light.getFocus(partialTicks) / 10.0D + 0.5D;

        return aabb.addCoord(point, height, point).addCoord(-point, height, -point);
    }

}
