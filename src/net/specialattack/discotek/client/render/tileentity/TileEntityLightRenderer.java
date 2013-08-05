
package net.specialattack.discotek.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
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

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityLight)) {
            return;
        }
        Minecraft.getMinecraft().mcProfiler.startSection("discotek");

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

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    private void render3(TileEntityLight light, double x, double y, double z, float partialTicks) {
        if (!lightOnly) {
            this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);
            this.modelDMXRedstone.renderAll();
        }
    }

    private void render4(TileEntityLight light, double x, double y, double z, float partialTicks) {
        int[] yawRotations = { 180, 0, 180, 180, 0, 0 };
        int[] pitchRotations = { 180, 0, 270, 90, 0, 0 };
        int[] rollRotations = { 0, 0, 0, 0, 90, 270 };
        int side = light.getDirection();

        GL11.glPushMatrix();
        GL11.glRotatef(pitchRotations[side], 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yawRotations[side], 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(rollRotations[side], 0.0f, 0.0f, 1.0f);

        if (!lightOnly) {
            this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);
            this.modelLaserRound.renderAll();
        }

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            int color = light.getColor(partialTicks);
            float red = (float) ((color >> 16) & 0xFF) / 255.0F;
            float green = (float) ((color >> 8) & 0xFF) / 255.0F;
            float blue = (float) (color & 0xFF) / 255.0F;
            float brightness = light.getBrightness(partialTicks);
            float alpha = (0.9F * brightness) + 0.1F;
            red *= brightness;
            green *= brightness;
            blue *= brightness;

            GL11.glRotatef((float) (light.getYaw(partialTicks) * 180.0F / Math.PI), 0.0F, 1.0F, 0.0F);

            GL11.glShadeModel(GL11.GL_SMOOTH);

            for (int i = 0; i < 32; i++) {
                GL11.glPushMatrix();
                GL11.glRotatef(light.getFocus(partialTicks) * 4.4F, 1.0F, 0.0F, -1.0F);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glColor4f(red, green, blue, alpha);
                GL11.glVertex3f(0.15F, 0.0F, 0.15F);
                GL11.glVertex3f(0.15F, (light.getPitch(partialTicks) + 0.8F) * 10.0F + 1.0F, 0.15F);
                GL11.glVertex3f(0.15F, (light.getPitch(partialTicks) + 0.8F) * 10.0F + 1.0F, 0.15F);
                GL11.glColor4f(red, green, blue, 0.0F);
                GL11.glVertex3f(0.15F, (light.getPitch(partialTicks) + 0.8F) * 10.0F + 6.0F, 0.15F);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glRotatef(11.25F, 0.0F, 1.0F, 0.0F);
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);

            Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
        }

        GL11.glPopMatrix();
    }

    public void render1(TileEntityLight light, double x, double y, double z, float partialTicks) {
        float pitch = light.getPitch(partialTicks);
        float yaw = light.getYaw(partialTicks);

        if (!lightOnly) {
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
        //GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int color = light.getColor(partialTicks);
        float red = (float) ((color >> 16) & 0xFF) / 255.0F;
        float green = (float) ((color >> 8) & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float brightness = light.getBrightness(partialTicks);

        if (!lightOnly) {
            if (light.hasLens()) {
                float lensBrightness = brightness + 0.1f;
                GL11.glColor4f(red * lensBrightness, green * lensBrightness, blue * lensBrightness, 0.4F);

                this.modelLightParCan.renderLens();
            }
        }

        red *= brightness;
        green *= brightness;
        blue *= brightness;

        if (renderLight && lightOnly) {
            float lightLength = (64f / ((light.getFocus(partialTicks) + 0.01f) * 0.7f));
            float alpha = (0.5F * brightness) + 0.1f;

            GL11.glRotatef(yaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(pitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

            //HUzzah! I'm a wizard
            float lightangle = light.getFocus(partialTicks);
            float downDiff = (float) (lightLength * Math.tan(Math.toRadians(lightangle)));

            float rend = red * 0.3f;
            float gend = green * 0.3f;
            float bend = blue * 0.3f;
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
            //GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
        }
    }

    public void render2(TileEntityLight light, double x, double y, double z, float partialTicks) {
        int[] yawRotations = { 180, 0, 180, 180, 0, 0 };
        int[] pitchRotations = { 180, 0, 270, 90, 0, 0 };
        int[] rollRotations = { 0, 0, 0, 0, 90, 270 };
        int side = light.getDirection();
        GL11.glPushMatrix();
        GL11.glRotatef(pitchRotations[side], 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yawRotations[side], 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(rollRotations[side], 0.0f, 0.0f, 1.0f);

        float pitch = light.getPitch(partialTicks);
        float yaw = light.getYaw(partialTicks);

        if (!lightOnly) {
            this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);

            this.modelLightMoverBase.setRotations(0, 0);
            this.modelLightMoverBase.renderAll();
            this.modelLightTiltArms.setRotations(0, yaw);
            this.modelLightTiltArms.renderAll();

            GL11.glTranslatef((float) 0, (float) 0.15f, (float) 0);
            this.modelLightMover.setRotations(pitch, yaw);
            this.modelLightMover.render();
        }

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        //GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int color = light.getColor(partialTicks);
        float red = (float) ((color >> 16) & 0xFF) / 255.0F;
        float green = (float) ((color >> 8) & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float brightness = light.getBrightness(partialTicks);
        float lensBrightness = brightness + 0.1f;

        if (!lightOnly) {
            GL11.glColor4f(red * lensBrightness, green * lensBrightness, blue * lensBrightness, 0.4F);

            this.modelLightMover.renderLens();
        }

        red *= brightness;
        green *= brightness;
        blue *= brightness;

        if (renderLight && lightOnly) {
            float lightLength = (64f / ((light.getFocus(partialTicks) + 0.01f) * 0.7f));
            float alpha = (0.5F * brightness) + 0.1f;

            GL11.glRotatef(yaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(pitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0, 0, 0.35f);
            //HUzzah! I'm a wizard
            float lightangle = light.getFocus(partialTicks);
            float downDiff = (float) (lightLength * Math.tan(Math.toRadians(lightangle)));

            float rend = red * 0.3f;
            float gend = green * 0.3f;
            float bend = blue * 0.3f;
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
            //GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (renderLight && lightOnly) {
            Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
        }
        GL11.glPopMatrix();
    }

}
