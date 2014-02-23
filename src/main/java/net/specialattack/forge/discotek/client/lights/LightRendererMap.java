
package net.specialattack.forge.discotek.client.lights;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelLightMover;
import net.specialattack.forge.discotek.client.model.ModelLightMoverBase;
import net.specialattack.forge.discotek.client.model.ModelLightTiltArms;
import net.specialattack.forge.discotek.client.render.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.lights.ILightRenderHandler;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererMap implements ILightRenderHandler {

    private ModelLightMover modelLightMover = new ModelLightMover();
    private ModelLightMoverBase modelLightMoverBase = new ModelLightMoverBase();
    private ModelLightTiltArms modelLightTiltArms = new ModelLightTiltArms();

    private boolean isLED;

    public LightRendererMap(boolean isLED) {
        this.isLED = isLED;
    }

    @Override
    public void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap) {
        Minecraft.getMinecraft().mcProfiler.startSection("calculations");

        int color = light.getColor(partialTicks);
        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float brightness = light.getBrightness(partialTicks);

        float pitch = light.getPitch(partialTicks);
        float yaw = light.getYaw(partialTicks);

        int side = light.getDirection();

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("model");

        RenderHelper.bindTexture(Assets.MAP_TEXTURE);

        this.modelLightMoverBase.setRotations(0, 0);
        this.modelLightMoverBase.renderAll();
        this.modelLightTiltArms.setRotations(0, yaw);
        this.modelLightTiltArms.renderAll();

        GL11.glTranslatef(0, 0.15F, 0);
        this.modelLightMover.setRotations(pitch, yaw);
        this.modelLightMover.render();

        if (this.isLED || light.hasLens()) {
            if (disableLightmap) {
                Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
            }

            float lensBrightness = brightness + 0.1F;
            GL11.glColor4f(red * lensBrightness, green * lensBrightness, blue * lensBrightness, 0.4F);

            this.modelLightMover.renderLens();

            if (disableLightmap) {
                Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
            }
        }

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public void renderLight(TileEntityLight light, float partialTicks) {
        Minecraft.getMinecraft().mcProfiler.startSection("calculations");

        float pitch = light.getPitch(partialTicks);
        float yaw = light.getYaw(partialTicks);

        int color = light.getColor(partialTicks);
        float brightness = light.getBrightness(partialTicks);
        float red = ((color >> 16) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float green = ((color >> 8) & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float blue = (color & 0xFF) / 255.0F * (brightness * 0.5F + 0.5F);
        float alpha = (0.5F * brightness) + 0.1F;

        float lightLength = MathHelper.min((64.0F / ((light.getFocus(partialTicks) + 0.01F) * 0.7F)), 128.0F);

        float lightangle = light.getFocus(partialTicks);
        float downDiff = (float) (lightLength * Math.tan(Math.toRadians(lightangle)));

        float rend = red * 0.3F;
        float gend = green * 0.3F;
        float bend = blue * 0.3F;

        Minecraft.getMinecraft().mcProfiler.endStartSection("transformations");

        int side = light.getDirection();

        GL11.glRotatef(TileEntityLightRenderer.pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(TileEntityLightRenderer.rollRotations[side], 0.0F, 0.0F, 1.0F);

        Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glTranslatef(0.0F, 0.15F, 0.0F);
        GL11.glRotatef(yaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(pitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, 0.35F);

        GL11.glBegin(GL11.GL_QUADS);

        // Inside First

        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, -0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(0.15F + downDiff, -0.15F - downDiff, -lightLength);
        GL11.glVertex3f(-0.15F - downDiff, -0.15F - downDiff, -lightLength);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(-0.15F, -0.15F, -0.5F);

        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(-0.15F - downDiff, 0.15F + downDiff, -lightLength);
        GL11.glVertex3f(0.15F + downDiff, 0.15F + downDiff, -lightLength);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, 0.15F, -0.5F);
        GL11.glVertex3f(-0.15F, 0.15F, -0.5F);

        GL11.glVertex3f(0.15F, 0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(0.15F + downDiff, 0.15F + downDiff, -lightLength);
        GL11.glVertex3f(0.15F + downDiff, -0.15F - downDiff, -lightLength);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, -0.15F, -0.5F);

        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(-0.15F - downDiff, -0.15F - downDiff, -lightLength);
        GL11.glVertex3f(-0.15F - downDiff, 0.15F + downDiff, -lightLength);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(-0.15F, 0.15F, -0.5F);
        GL11.glVertex3f(-0.15F, -0.15F, -0.5F);

        // Outside

        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(-0.15F, -0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(-0.15F - downDiff, -0.15F - downDiff, -lightLength);
        GL11.glVertex3f(0.15F + downDiff, -0.15F - downDiff, -lightLength);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, -0.15F, -0.5F);

        GL11.glVertex3f(-0.15F, 0.15F, -0.5F);
        GL11.glVertex3f(0.15F, 0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(0.15F + downDiff, 0.15F + downDiff, -lightLength);
        GL11.glVertex3f(-0.15F - downDiff, 0.15F + downDiff, -lightLength);

        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, -0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(0.15F + downDiff, -0.15F - downDiff, -lightLength);
        GL11.glVertex3f(0.15F + downDiff, 0.15F + downDiff, -lightLength);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, 0.15F, -0.5F);

        GL11.glVertex3f(-0.15F, -0.15F, -0.5F);
        GL11.glVertex3f(-0.15F, 0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(-0.15F - downDiff, 0.15F + downDiff, -lightLength);
        GL11.glVertex3f(-0.15F - downDiff, -0.15F - downDiff, -lightLength);

        GL11.glEnd();

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

        float yaw = light.getYaw(partialTicks); // +-XZ
        float pitch = light.getPitch(partialTicks); // +-Y
        float lightLength = MathHelper.min((64.0F / ((light.getFocus(partialTicks) + 0.01F) * 0.7F)), 128.0F);

        float x = lightLength * -net.minecraft.util.MathHelper.sin(yaw) * net.minecraft.util.MathHelper.cos(pitch);
        float z = lightLength * -net.minecraft.util.MathHelper.cos(yaw) * net.minecraft.util.MathHelper.cos(pitch);
        float y = lightLength * net.minecraft.util.MathHelper.sin(pitch);

        return aabb.addCoord(x, y, z);
    }

}
