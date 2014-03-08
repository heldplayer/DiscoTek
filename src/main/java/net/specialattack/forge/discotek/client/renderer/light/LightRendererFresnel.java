
package net.specialattack.forge.discotek.client.renderer.light;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelLightParCan;
import net.specialattack.forge.discotek.client.model.ModelLightYoke;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightFresnelInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererFresnel implements ILightRenderHandler {

    private ModelLightParCan modelLightParCan = new ModelLightParCan();
    private ModelLightYoke modelLightYoke = new ModelLightYoke();

    private ILightInstance instance = new LightFresnelInstance(null);

    @Override
    public void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap) {
        Minecraft.getMinecraft().mcProfiler.startSection("calculations");

        float pitch = light.getFloat("pitch", partialTicks);
        float yaw = light.getFloat("yaw", partialTicks);

        float red = (light.getInteger("red", partialTicks) & 0xFF) / 255.0F;
        float green = (light.getInteger("green", partialTicks) & 0xFF) / 255.0F;
        float blue = (light.getInteger("blue", partialTicks) & 0xFF) / 255.0F;
        float brightness = light.getFloat("brightness", partialTicks);

        Minecraft.getMinecraft().mcProfiler.endStartSection("model");

        RenderHelper.bindTexture(Assets.FRESNEL_TEXTURE);

        this.modelLightYoke.setRotations(pitch, yaw);
        this.modelLightYoke.renderAll();
        this.modelLightParCan.setRotations(pitch, yaw);
        this.modelLightParCan.render();

        if (light.getBoolean("hasLens", partialTicks)) {
            if (disableLightmap) {
                Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
            }

            float lensBrightness = brightness * 0.9F + 0.1F;
            GL11.glColor4f(red * lensBrightness, green * lensBrightness, blue * lensBrightness, 0.4F);

            this.modelLightParCan.renderLens();

            if (disableLightmap) {
                Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
            }
        }

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public void renderLight(TileEntityLight light, float partialTicks) {
        Minecraft.getMinecraft().mcProfiler.startSection("calculations");

        float pitch = light.getFloat("pitch", partialTicks);
        float yaw = light.getFloat("yaw", partialTicks);

        float red = (light.getInteger("red", partialTicks) & 0xFF) / 255.0F;
        float green = (light.getInteger("green", partialTicks) & 0xFF) / 255.0F;
        float blue = (light.getInteger("blue", partialTicks) & 0xFF) / 255.0F;
        float brightness = light.getFloat("brightness", partialTicks);
        float alpha = (0.5F * brightness) + 0.1F;

        float focus = light.getFloat("focus", partialTicks);
        float length = MathHelper.min((64.0F / ((focus + 0.01F) * 0.7F)), 128.0F);

        float angle = (float) (focus * Math.PI / 200.0F);
        float lengthb = MathHelper.cos(angle) * length;
        float distance = MathHelper.sin(angle) * length;

        float rend = red * 0.3F;
        float gend = green * 0.3F;
        float bend = blue * 0.3F;

        Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glRotatef(yaw * (180.0F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(pitch * (180.0F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

        GL11.glBegin(GL11.GL_QUADS);

        // Inside First

        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, -0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(0.15F + distance, -0.15F - distance, -lengthb);
        GL11.glVertex3f(-0.15F - distance, -0.15F - distance, -lengthb);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(-0.15F, -0.15F, -0.5F);

        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(-0.15F - distance, 0.15F + distance, -lengthb);
        GL11.glVertex3f(0.15F + distance, 0.15F + distance, -lengthb);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, 0.15F, -0.5F);
        GL11.glVertex3f(-0.15F, 0.15F, -0.5F);

        GL11.glVertex3f(0.15F, 0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(0.15F + distance, 0.15F + distance, -lengthb);
        GL11.glVertex3f(0.15F + distance, -0.15F - distance, -lengthb);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, -0.15F, -0.5F);

        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(-0.15F - distance, -0.15F - distance, -lengthb);
        GL11.glVertex3f(-0.15F - distance, 0.15F + distance, -lengthb);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(-0.15F, 0.15F, -0.5F);
        GL11.glVertex3f(-0.15F, -0.15F, -0.5F);

        // Outside

        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(-0.15F, -0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(-0.15F - distance, -0.15F - distance, -lengthb);
        GL11.glVertex3f(0.15F + distance, -0.15F - distance, -lengthb);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, -0.15F, -0.5F);

        GL11.glVertex3f(-0.15F, 0.15F, -0.5F);
        GL11.glVertex3f(0.15F, 0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(0.15F + distance, 0.15F + distance, -lengthb);
        GL11.glVertex3f(-0.15F - distance, 0.15F + distance, -lengthb);

        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, -0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(0.15F + distance, -0.15F - distance, -lengthb);
        GL11.glVertex3f(0.15F + distance, 0.15F + distance, -lengthb);
        GL11.glColor4f(red, green, blue, alpha); // Origin
        GL11.glVertex3f(0.15F, 0.15F, -0.5F);

        GL11.glVertex3f(-0.15F, -0.15F, -0.5F);
        GL11.glVertex3f(-0.15F, 0.15F, -0.5F);
        GL11.glColor4f(rend, gend, bend, 0.0F); // End
        GL11.glVertex3f(-0.15F - distance, 0.15F + distance, -lengthb);
        GL11.glVertex3f(-0.15F - distance, -0.15F - distance, -lengthb);

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

        float pitch = light.getFloat("pitch", partialTicks); // +-Y
        float yaw = light.getFloat("yaw", partialTicks); // +-XZ
        float focus = light.getFloat("focus", partialTicks);
        float length = MathHelper.min((64.0F / ((focus + 0.01F) * 0.7F)), 128.0F);
        float angle = (float) (focus * Math.PI / 200.0F);
        float lightLength = MathHelper.cos(angle) * length / 2.0F + 1.0F;
        float distance = MathHelper.sin(angle) * length;

        float x = lightLength * -net.minecraft.util.MathHelper.sin(yaw) * net.minecraft.util.MathHelper.cos(pitch);
        float z = lightLength * -net.minecraft.util.MathHelper.cos(yaw) * net.minecraft.util.MathHelper.cos(pitch);
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
