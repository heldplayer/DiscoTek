
package net.specialattack.modjam.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.modjam.client.render.BlockRendererLight;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockLight extends Block {

    private final int renderId;

    public BlockLight(int blockId) {
        super(blockId, Material.piston);
        this.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this.renderId, new BlockRendererLight(this.renderId));
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);

        TileEntityLight tile = (TileEntityLight) world.getBlockTileEntity(x, y, z);

        if (stack.stackTagCompound != null) {
            if (stack.stackTagCompound.hasKey("color")) {
                tile.color = stack.stackTagCompound.getInteger("color");
            }
        }

        tile.prevYaw = tile.yaw = (float) (-entity.rotationYawHead * Math.PI / 180.0D);
        tile.prevPitch = tile.pitch = (float) (entity.rotationPitch * Math.PI / 180.0D);

        if (tile.pitch > 0.8F) {
            tile.prevPitch = tile.pitch = 0.8F;
        }
        if (tile.pitch < -0.8F) {
            tile.prevPitch = tile.pitch = -0.8F;
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityLight();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return this.renderId;
    }

}
