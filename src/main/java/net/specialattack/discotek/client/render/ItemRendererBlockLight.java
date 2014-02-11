
package net.specialattack.discotek.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.specialattack.discotek.client.render.tileentity.TileEntityLightRenderer;
import net.specialattack.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRendererBlockLight implements IItemRenderer {

    private TileEntityLight renderTile;

    public ItemRendererBlockLight() {
        this.renderTile = new TileEntityLight();
        this.renderTile.setDirection(1);
    }

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
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        this.renderTile.setColor(0xFFFFFF);
        if (Block.getBlockFromItem(item.getItem()) == null) {
            return;
        }
        this.renderTile.blockType = Block.getBlockFromItem(item.getItem());

        if (item.stackTagCompound != null) {
            if (item.stackTagCompound.hasKey("color")) {
                this.renderTile.setColor(item.stackTagCompound.getInteger("color"));
            }
        }
        this.renderTile.blockMetadata = item.getItemDamage();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPushMatrix();

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        }

        TileEntityLightRenderer.renderLight = false;
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.renderTile, 0.0D, 0.0D, 0.0D, 0.0F);
        TileEntityLightRenderer.renderLight = true;
        
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_BLEND);
    }

}
