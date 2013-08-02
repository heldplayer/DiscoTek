
package net.specialattack.modjam.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityLight extends TileEntity {

    public int color = 0xFFFFFFFF;
    public float pitch;
    public float yaw;
    public ForgeDirection direction;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.color = compound.getInteger("color");
        this.pitch = compound.getFloat("pitch");
        this.yaw = compound.getFloat("yaw");
        this.direction = ForgeDirection.getOrientation(compound.getInteger("direction"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("color", this.color);
        compound.setFloat("pitch", this.pitch);
        compound.setInteger("direction", this.direction.ordinal());
    }
    
    public boolean hasGel(){
           return !(color == 0xFFFFFFFF);
    }

}