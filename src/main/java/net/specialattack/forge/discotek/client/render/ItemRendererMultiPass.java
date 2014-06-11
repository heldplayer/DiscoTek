
package net.specialattack.forge.discotek.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRendererMultiPass implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        switch (helper) {
        case BLOCK_3D:
            return false;
        case ENTITY_BOBBING:
            return true;
        case ENTITY_ROTATION:
            return true;
        case EQUIPPED_BLOCK:
            return false;
        case INVENTORY_BLOCK:
            return false;
        default:
            return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int color = item.getItem().getColorFromItemStack(item, 0);
        double red = ((color >> 16) & 0xFF) / 255.0F;
        double green = ((color >> 8) & 0xFF) / 255.0F;
        double blue = (color & 0xFF) / 255.0F;
        GL11.glColor3d(red, green, blue);
        this.renderIcon(item, item.getItem().getIcon(item, 0), type);

        color = item.getItem().getColorFromItemStack(item, 1);
        red = ((color >> 16) & 0xFF) / 255.0F;
        green = ((color >> 8) & 0xFF) / 255.0F;
        blue = (color & 0xFF) / 255.0F;
        GL11.glColor3d(red, green, blue);
        this.renderIcon(item, item.getItem().getIcon(item, 1), type);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }

    private void renderIcon(ItemStack item, IIcon icon, ItemRenderType type) {
        if (type == ItemRenderType.INVENTORY) {
            Tessellator tes = Tessellator.instance;
            tes.startDrawingQuads();
            tes.addVertexWithUV(0.0D, 16.0D, 0.0D, icon.getMinU(), icon.getMaxV());
            tes.addVertexWithUV(16.0D, 16.0D, 0.0D, icon.getMaxU(), icon.getMaxV());
            tes.addVertexWithUV(16.0D, 0.0D, 0.0D, icon.getMaxU(), icon.getMinV());
            tes.addVertexWithUV(0.0D, 0.0D, 0.0D, icon.getMinU(), icon.getMinV());
            tes.draw();
        }
        else {
            GL11.glPushMatrix();
            TextureManager textureManager = Minecraft.getMinecraft().renderEngine;

            textureManager.bindTexture(textureManager.getResourceLocation(item.getItemSpriteNumber()));
            Tessellator tess = Tessellator.instance;
            float minU = icon.getMinU();
            float maxU = icon.getMaxU();
            float minV = icon.getMinV();
            float maxV = icon.getMaxV();
            if (type == ItemRenderType.ENTITY) {
                GL11.glTranslatef(-0.5F, -0.3F, 0.0F);
            }

            this.renderItemIn3D(tess, maxU, minV, minU, maxV, icon.getIconHeight(), icon.getIconWidth(), 0.0625F);

            GL11.glPopMatrix();
        }
    }

    private void renderItemIn3D(Tessellator tess, float maxU, float minV, float minU, float maxV, int originX, int originY, float scale) {
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, 1.0F);
        tess.addVertexWithUV(0.0D, 0.0D, 0.0D, maxU, maxV);
        tess.addVertexWithUV(1.0D, 0.0D, 0.0D, minU, maxV);
        tess.addVertexWithUV(1.0D, 1.0D, 0.0D, minU, minV);
        tess.addVertexWithUV(0.0D, 1.0D, 0.0D, maxU, minV);
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, -1.0F);
        tess.addVertexWithUV(0.0D, 1.0D, 0.0F - scale, maxU, minV);
        tess.addVertexWithUV(1.0D, 1.0D, 0.0F - scale, minU, minV);
        tess.addVertexWithUV(1.0D, 0.0D, 0.0F - scale, minU, maxV);
        tess.addVertexWithUV(0.0D, 0.0D, 0.0F - scale, maxU, maxV);
        tess.draw();
        float f5 = 0.5F * (maxU - minU) / originX;
        float f6 = 0.5F * (maxV - minV) / originY;
        tess.startDrawingQuads();
        tess.setNormal(-1.0F, 0.0F, 0.0F);
        int k;
        float f7;
        float f8;

        for (k = 0; k < originX; ++k) {
            f7 = (float) k / (float) originX;
            f8 = maxU + (minU - maxU) * f7 - f5;
            tess.addVertexWithUV(f7, 0.0D, 0.0F - scale, f8, maxV);
            tess.addVertexWithUV(f7, 0.0D, 0.0D, f8, maxV);
            tess.addVertexWithUV(f7, 1.0D, 0.0D, f8, minV);
            tess.addVertexWithUV(f7, 1.0D, 0.0F - scale, f8, minV);
        }

        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(1.0F, 0.0F, 0.0F);
        float f9;

        for (k = 0; k < originX; ++k) {
            f7 = (float) k / (float) originX;
            f8 = maxU + (minU - maxU) * f7 - f5;
            f9 = f7 + 1.0F / originX;
            tess.addVertexWithUV(f9, 1.0D, 0.0F - scale, f8, minV);
            tess.addVertexWithUV(f9, 1.0D, 0.0D, f8, minV);
            tess.addVertexWithUV(f9, 0.0D, 0.0D, f8, maxV);
            tess.addVertexWithUV(f9, 0.0D, 0.0F - scale, f8, maxV);
        }

        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 1.0F, 0.0F);

        for (k = 0; k < originY; ++k) {
            f7 = (float) k / (float) originY;
            f8 = maxV + (minV - maxV) * f7 - f6;
            f9 = f7 + 1.0F / originY;
            tess.addVertexWithUV(0.0D, f9, 0.0D, maxU, f8);
            tess.addVertexWithUV(1.0D, f9, 0.0D, minU, f8);
            tess.addVertexWithUV(1.0D, f9, 0.0F - scale, minU, f8);
            tess.addVertexWithUV(0.0D, f9, 0.0F - scale, maxU, f8);
        }

        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, -1.0F, 0.0F);

        for (k = 0; k < originY; ++k) {
            f7 = (float) k / (float) originY;
            f8 = maxV + (minV - maxV) * f7 - f6;
            tess.addVertexWithUV(1.0D, f7, 0.0D, minU, f8);
            tess.addVertexWithUV(0.0D, f7, 0.0D, maxU, f8);
            tess.addVertexWithUV(0.0D, f7, 0.0F - scale, maxU, f8);
            tess.addVertexWithUV(1.0D, f7, 0.0F - scale, minU, f8);
        }

        tess.draw();
    }

}
