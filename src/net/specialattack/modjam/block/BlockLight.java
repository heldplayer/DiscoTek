
package net.specialattack.modjam.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.modjam.Objects;
import net.specialattack.modjam.client.render.BlockRendererLight;
import net.specialattack.modjam.tileentity.TileEntityLight;
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking()) {
            TileEntity te = world.getBlockTileEntity(x, y, z);
            if (te != null && te instanceof TileEntityLight) {
                TileEntityLight light = (TileEntityLight) te;
                if (light.hasGel()) {
                    ItemStack is = new ItemStack(Objects.itemLens);
                    NBTTagCompound cpnd = new NBTTagCompound();
                    cpnd.setInteger("color", light.color);
                    is.setTagCompound(cpnd);

                    Random rand = new Random();
                    EntityItem ent = player.entityDropItem(is, 1.0F);
                    ent.motionY += rand.nextFloat() * 0.05F;
                    ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    ent.delayBeforeCanPickup = 1;

                    light.color = 0xFFFFFF;
                    return true;
                }
            }
        }
        return false;
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
