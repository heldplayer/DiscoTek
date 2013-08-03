
package net.specialattack.modjam.client.render;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.specialattack.modjam.client.render.tileentity.TileEntityLightRenderer;
import net.specialattack.modjam.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

public class ItemRendererBlockLight implements IItemRenderer {

    private TileEntityLight renderTile = new TileEntityLight();

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
        this.renderTile.color = 0xFFFFFF;
        if (item.stackTagCompound != null) {
            if (item.stackTagCompound.hasKey("color")) {
                this.renderTile.color = item.stackTagCompound.getInteger("color");
            }
        }

        GL11.glPushMatrix();

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        }

        TileEntityLightRenderer.disableLight = false;
        TileEntityRenderer.instance.renderTileEntityAt(this.renderTile, 0.0D, 0.0D, 0.0D, 0.0F);
        TileEntityLightRenderer.disableLight = true;
        GL11.glPopMatrix();
    }

}
