
package net.specialattack.forge.discotek.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.client.renderer.light.ILightRenderHandler;
import net.specialattack.forge.discotek.client.renderer.tileentity.TileEntityLightRenderer;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRendererBlockLight implements IItemRenderer {

    private TileEntityLight renderTile;

    public ItemRendererBlockLight() {
        this.renderTile = new TileEntityLight();
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
        Block block = Block.getBlockFromItem(item.getItem());
        if (block == null || !(block instanceof BlockLight)) {
            return;
        }
        BlockLight blockLight = (BlockLight) block;
        ILightRenderHandler renderHandler = blockLight.getLightRenderer(item.getItemDamage());
        this.renderTile.setLightInstance(renderHandler.getRenderInstance());
        this.renderTile.setValue("color", 0xFFFFFF);
        this.renderTile.setBlockType(Block.getBlockFromItem(item.getItem()));
        this.renderTile.blockMetadata = item.getItemDamage();
        this.renderTile.blockType = Block.getBlockFromItem(item.getItem());

        if (item.stackTagCompound != null) {
            if (item.stackTagCompound.hasKey("color")) {
                this.renderTile.setValue("color", item.stackTagCompound.getInteger("color"));
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
