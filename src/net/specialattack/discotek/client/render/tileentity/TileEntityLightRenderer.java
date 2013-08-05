
package net.specialattack.discotek.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.client.model.ModelDMXRedstone;
import net.specialattack.discotek.client.model.ModelLaserRound;
import net.specialattack.discotek.client.model.ModelLightMover;
import net.specialattack.discotek.client.model.ModelLightMoverBase;
import net.specialattack.discotek.client.model.ModelLightParCan;
import net.specialattack.discotek.client.model.ModelLightTiltArms;
import net.specialattack.discotek.client.model.ModelLightYoke;
import net.specialattack.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityLightRenderer extends TileEntitySpecialRenderer {

    private ModelLightParCan modelLightParCan = new ModelLightParCan();
    private ModelLightYoke modelLightYoke = new ModelLightYoke();
    private ModelLightMover modelLightMover = new ModelLightMover();
    private ModelLightMoverBase modelLightMoverBase = new ModelLightMoverBase();
    private ModelLightTiltArms modelLightTiltArms = new ModelLightTiltArms();
    private ModelDMXRedstone modelDMXRedstone = new ModelDMXRedstone();
    private ModelLaserRound modelLaserRound = new ModelLaserRound();

    public static boolean renderLight = true;
    public static boolean lightOnly = false;

    public static int[] yawRotations = { 180, 0, 180, 180, 0, 0 };
    public static int[] pitchRotations = { 180, 0, 270, 90, 0, 0 };
    public static int[] rollRotations = { 0, 0, 0, 0, 90, 270 };

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityLight)) {
            return;
        }
        if (!lightOnly) {
            Minecraft.getMinecraft().mcProfiler.startSection("discotek");
        }

        TileEntityLight light = (TileEntityLight) tile;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        switch (light.getBlockMetadata()) {
        case 0:
            Minecraft.getMinecraft().mcProfiler.startSection("fresnel");
            this.render1(light, x, y, z, partialTicks);
            Minecraft.getMinecraft().mcProfiler.endSection();
        break;
        case 1:
            Minecraft.getMinecraft().mcProfiler.startSection("spa");
            this.render2(light, x, y, z, partialTicks);
            Minecraft.getMinecraft().mcProfiler.endSection();
        break;
        case 2:
            Minecraft.getMinecraft().mcProfiler.startSection("spaLED");
            this.render2(light, x, y, z, partialTicks);
            Minecraft.getMinecraft().mcProfiler.endSection();
        break;
        case 3:
            Minecraft.getMinecraft().mcProfiler.startSection("dimmer");
            this.render3(light, x, y, z, partialTicks);
            Minecraft.getMinecraft().mcProfiler.endSection();
        break;
        case 4:
            Minecraft.getMinecraft().mcProfiler.startSection("radialLaser");
            this.render4(light, x, y, z, partialTicks);
            Minecraft.getMinecraft().mcProfiler.endSection();
        break;
        }

        GL11.glPopMatrix();

        if (!lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
    }

    private void render3(TileEntityLight light, double x, double y, double z, float partialTicks) {
        if (!lightOnly) {
            Minecraft.getMinecraft().mcProfiler.startSection("model");
            this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);
            this.modelDMXRedstone.renderAll();
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
    }

    private void render4(TileEntityLight light, double x, double y, double z, float partialTicks) {
        Minecraft.getMinecraft().mcProfiler.startSection("transformations");

        int side = light.getDirection();

        GL11.glPushMatrix();
        GL11.glRotatef(pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rollRotations[side], 0.0F, 0.0F, 1.0F);

        if (!lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endStartSection("model");
            this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);
            this.modelLaserRound.renderAll();
        }

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endStartSection("beam");
            Minecraft.getMinecraft().mcProfiler.startSection("calculations");

            int color = light.getColor(partialTicks);
            float red = (float) ((color >> 16) & 0xFF) / 255.0F;
            float green = (float) ((color >> 8) & 0xFF) / 255.0F;
            float blue = (float) (color & 0xFF) / 255.0F;
            float brightness = light.getBrightness(partialTicks);
            float alpha = (0.9F * brightness) + 0.1F;
            red *= brightness;
            green *= brightness;
            blue *= brightness;

            float angle = (float) (light.getFocus(partialTicks) * Math.PI / 42.0F);
            float length1 = (light.getPitch(partialTicks) + 0.8F) * 10.0F + 1.0F;
            float length2 = (light.getPitch(partialTicks) + 0.8F) * 10.0F + 6.0F;
            float height1 = MathHelper.cos(angle) * length1;
            float height2 = MathHelper.cos(angle) * length2;
            float distance1 = MathHelper.sin(angle) * length1;
            float distance2 = MathHelper.sin(angle) * length2;

            Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

            Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);

            for (int i = 0; i < 32; i++) {
                float currentAngle = (float) ((float) i * Math.PI / 16.0F);
                float startX = MathHelper.cos(currentAngle) * 0.2F;
                float startZ = MathHelper.sin(currentAngle) * 0.2F;
                float posX1 = MathHelper.cos(currentAngle) * (distance1 + 0.2F);
                float posZ1 = MathHelper.sin(currentAngle) * (distance1 + 0.2F);
                float posX2 = MathHelper.cos(currentAngle) * (distance2 + 0.2F);
                float posZ2 = MathHelper.sin(currentAngle) * (distance2 + 0.2F);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glColor4f(red, green, blue, alpha);
                GL11.glVertex3f(startX, 0.0F, startZ);
                GL11.glVertex3f(posX1, height1, posZ1);
                GL11.glVertex3f(posX1, height1, posZ1);
                GL11.glColor4f(red, green, blue, 0.0F);
                GL11.glVertex3f(posX2, height2, posZ2);
                GL11.glEnd();
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);

            Minecraft.getMinecraft().mcProfiler.endSection();
        }

        GL11.glPopMatrix();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    public void render1(TileEntityLight light, double x, double y, double z, float partialTicks) {
        Minecraft.getMinecraft().mcProfiler.startSection("calculations");
        float pitch = light.getPitch(partialTicks);
        float yaw = light.getYaw(partialTicks);

        int color = light.getColor(partialTicks);
        float red = (float) ((color >> 16) & 0xFF) / 255.0F;
        float green = (float) ((color >> 8) & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float brightness = light.getBrightness(partialTicks);

        if (!lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endStartSection("model");
            this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);

            this.modelLightYoke.setRotations(pitch, yaw);
            this.modelLightYoke.renderAll();
            this.modelLightParCan.setRotations(pitch, yaw);
            this.modelLightParCan.render();
        }

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        if (!lightOnly) {
            if (light.hasLens()) {
                float lensBrightness = brightness + 0.1F;
                GL11.glColor4f(red * lensBrightness, green * lensBrightness, blue * lensBrightness, 0.4F);

                this.modelLightParCan.renderLens();
            }
        }

        red *= brightness;
        green *= brightness;
        blue *= brightness;

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endStartSection("beam");
            Minecraft.getMinecraft().mcProfiler.startSection("calculations");

            float lightLength = (64.0F / ((light.getFocus(partialTicks) + 0.01F) * 0.7F));
            float alpha = (0.5F * brightness) + 0.1F;

            //HUzzah! I'm a wizard
            float lightangle = light.getFocus(partialTicks);
            float downDiff = (float) (lightLength * Math.tan(Math.toRadians(lightangle)));

            float rend = red * 0.3F;
            float gend = green * 0.3F;
            float bend = blue * 0.3F;

            Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

            GL11.glRotatef(yaw * (180.0F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(pitch * (180.0F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

            GL11.glShadeModel(GL11.GL_SMOOTH);

            GL11.glBegin(GL11.GL_QUADS);
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

            // Inside

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

            GL11.glEnd();

            Minecraft.getMinecraft().mcProfiler.endSection();
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
        }

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    public void render2(TileEntityLight light, double x, double y, double z, float partialTicks) {
        Minecraft.getMinecraft().mcProfiler.startSection("transformations");

        int side = light.getDirection();

        GL11.glPushMatrix();
        GL11.glRotatef(pitchRotations[side], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(yawRotations[side], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rollRotations[side], 0.0F, 0.0F, 1.0F);

        float pitch = light.getPitch(partialTicks);
        float yaw = light.getYaw(partialTicks);

        if (!lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endStartSection("model");
            this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);

            this.modelLightMoverBase.setRotations(0, 0);
            this.modelLightMoverBase.renderAll();
            this.modelLightTiltArms.setRotations(0, yaw);
            this.modelLightTiltArms.renderAll();

            GL11.glTranslatef((float) 0, (float) 0.15F, (float) 0);
            this.modelLightMover.setRotations(pitch, yaw);
            this.modelLightMover.render();
        }

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
        }

        Minecraft.getMinecraft().mcProfiler.endStartSection("calculations");

        int color = light.getColor(partialTicks);
        float red = (float) ((color >> 16) & 0xFF) / 255.0F;
        float green = (float) ((color >> 8) & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float brightness = light.getBrightness(partialTicks);
        float lensBrightness = brightness + 0.1F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        if (!lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endStartSection("model");

            GL11.glColor4f(red * lensBrightness, green * lensBrightness, blue * lensBrightness, 0.4F);

            this.modelLightMover.renderLens();
        }

        red *= brightness;
        green *= brightness;
        blue *= brightness;

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endStartSection("beam");
            Minecraft.getMinecraft().mcProfiler.startSection("calculations");

            float lightLength = (64.0F / ((light.getFocus(partialTicks) + 0.01F) * 0.7F));
            float alpha = (0.5F * brightness) + 0.1F;

            float lightangle = light.getFocus(partialTicks);
            float downDiff = (float) (lightLength * Math.tan(Math.toRadians(lightangle)));

            float rend = red * 0.3F;
            float gend = green * 0.3F;
            float bend = blue * 0.3F;

            Minecraft.getMinecraft().mcProfiler.endStartSection("rendering");

            GL11.glRotatef(yaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(pitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0, 0, 0.35F);

            GL11.glShadeModel(GL11.GL_SMOOTH);

            GL11.glBegin(GL11.GL_QUADS);
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

            // Inside

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

            GL11.glEnd();
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
        }
        GL11.glPopMatrix();

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

}
