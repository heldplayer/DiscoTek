package net.specialattack.forge.discotek.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.specialattack.forge.core.client.RenderHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemRendererBlockColoredLamp implements IItemRenderer {

    private static RenderBlocks renderer = new RenderBlocks();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        switch (helper) {
            case BLOCK_3D:
                return true;
            case ENTITY_BOBBING:
                return true;
            case ENTITY_ROTATION:
                return true;
            case EQUIPPED_BLOCK:
                return true;
            case INVENTORY_BLOCK:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (block == null) {
            return;
        }

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        }

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        // Inner
        ItemRendererBlockColoredLamp.renderer.setRenderBounds(0.005D, 0.005D, 0.005D, 0.995D, 0.995D, 0.995D);

        if (stack.stackTagCompound != null) {
            if (stack.stackTagCompound.hasKey("color", 3)) {
                tessellator.setColorOpaque_I(stack.stackTagCompound.getInteger("color"));
            }
        }

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceYNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(0, 1), false));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceYPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(1, 1), false));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceZNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(5, 1), false));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceZPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(4, 1), false));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceXNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(2, 1), false));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceXPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(3, 1), false));

        // Overlay
        ItemRendererBlockColoredLamp.renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceYNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(0, 0), false));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceYPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(1, 0), false));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceZNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(5, 0), false));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceZPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(4, 0), false));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceXNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(2, 0), false));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        ItemRendererBlockColoredLamp.renderer.renderFaceXPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(3, 0), false));

        tessellator.draw();

        GL11.glPopMatrix();
    }

}
