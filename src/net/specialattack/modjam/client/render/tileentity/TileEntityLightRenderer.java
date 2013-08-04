
package net.specialattack.modjam.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.client.model.ModelLightMover;
import net.specialattack.modjam.client.model.ModelLightMoverBase;
import net.specialattack.modjam.client.model.ModelLightParCan;
import net.specialattack.modjam.client.model.ModelLightTiltArms;
import net.specialattack.modjam.client.model.ModelLightYoke;
import net.specialattack.modjam.tileentity.TileEntityLight;

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

    public static boolean disableLight = true;

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityLight)) {
            return;
        }

        TileEntityLight light = (TileEntityLight) tile;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        switch (light.getBlockMetadata() & 0xFF) {
        case 0:
            this.render1(light, x, y, z, partialTicks);
        break;
        case 1:
            this.render2(light, x, y, z, partialTicks);
        break;
        case 2:
            this.render2(light, x, y, z, partialTicks);
        break;
        }

        GL11.glPopMatrix();
    }

    public void render1(TileEntityLight light, double x, double y, double z, float partialTicks) {
        this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);

        float pitch = light.getPitch(partialTicks);
        float yaw = light.getYaw(partialTicks);
        this.modelLightYoke.setRotations(pitch, yaw);
        this.modelLightYoke.renderAll();
        this.modelLightParCan.setRotations(pitch, yaw);
        this.modelLightParCan.render();

        if (disableLight) {
            Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_DST_COLOR);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int color = light.getColor(partialTicks);
        float red = (float) ((color >> 16) & 0xFF) / 255.0F;
        float green = (float) ((color >> 8) & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float brightness = light.getBrightness(partialTicks);
        if (light.hasLens()) {
            float lensBrightness = brightness + 0.1f;
            GL11.glColor4f(red * lensBrightness, green * lensBrightness, blue * lensBrightness, 0.4F);

            this.modelLightParCan.renderLens();
        }
        red *= brightness;
        green *= brightness;
        blue *= brightness;

        if (disableLight) {
            float lightLength = (64f / ((light.getFocus(partialTicks) + 0.01f) * 0.7f));
            float alpha = (0.5F * brightness) + 0.1f;
            //            if (light.brightness > 0) {
            //                System.out.println("A:" + alpha + " | B: " + light.brightness);
            //            }
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
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (disableLight) {
            Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
        }
    }

    public void render2(TileEntityLight light, double x, double y, double z, float partialTicks) {
        this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);

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
        this.modelLightMoverBase.setRotations(0, 0);
        this.modelLightMoverBase.renderAll();
        this.modelLightTiltArms.setRotations(0, yaw);
        this.modelLightTiltArms.renderAll();
        GL11.glTranslatef((float) 0, (float) 0.15f, (float) 0);
        this.modelLightMover.setRotations(pitch, yaw);
        this.modelLightMover.render();

        if (disableLight) {
            Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_DST_COLOR);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int color = light.getColor(partialTicks);
        float red = (float) ((color >> 16) & 0xFF) / 255.0F;
        float green = (float) ((color >> 8) & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float brightness = light.getBrightness(partialTicks);
        //if (light.hasLens()) {
        float lensBrightness = brightness + 0.1f;
        GL11.glColor4f(red * lensBrightness, green * lensBrightness, blue * lensBrightness, 0.4F);

        this.modelLightMover.renderLens();
        //}
        red *= brightness;
        green *= brightness;
        blue *= brightness;

        if (disableLight) {
            float lightLength = (64f / ((light.getFocus(partialTicks) + 0.01f) * 0.7f));
            float alpha = (0.5F * brightness) + 0.1f;
            //            if (light.brightness > 0) {
            //                System.out.println("A:" + alpha + " | B: " + light.brightness);
            //            }
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
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (disableLight) {
            Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
        }
        GL11.glPopMatrix();
    }

}
