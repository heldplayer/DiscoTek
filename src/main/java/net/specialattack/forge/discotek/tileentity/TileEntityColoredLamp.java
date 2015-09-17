package net.specialattack.forge.discotek.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.core.sync.SyncTileEntity;
import net.specialattack.forge.core.sync.object.SyncBool;
import net.specialattack.forge.core.sync.object.SyncInt;

public class TileEntityColoredLamp extends SyncTileEntity {

    public SyncInt color;
    public SyncBool lit;

    public TileEntityColoredLamp() {
        this.syncables.put("color", this.color = new SyncInt(0xFFFFFF, this, "color"));
        this.syncables.put("lit", this.lit = new SyncBool(false, this, "lit"));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.color.value = compound.getInteger("color");
        this.lit.value = compound.getBoolean("lit");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("color", this.color.value);
        compound.setBoolean("lit", this.lit.value);
    }

    @Override
    public boolean canPlayerTrack(EntityPlayerMP entityPlayerMP) {
        return true;
    }

    @Override
    public String getDebugDisplay() {
        return String.format("Colored Lamp: %8h", this.color.value);
    }

    @Override
    public void syncableChanged(ISyncable syncable) {
        if (syncable == this.lit) {
            this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
        }
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
}
