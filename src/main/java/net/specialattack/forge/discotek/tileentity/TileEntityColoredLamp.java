
package net.specialattack.forge.discotek.tileentity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.specialattack.forge.core.SpACore;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.core.sync.ISyncableObjectOwner;
import net.specialattack.forge.core.sync.SBoolean;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.core.sync.packet.Packet1TrackingStatus;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityColoredLamp extends TileEntity implements ISyncableObjectOwner {

    public SInteger color;
    public SBoolean lit;

    private List<ISyncable> syncables;

    public TileEntityColoredLamp() {
        this.color = new SInteger(this, 0xFFFFFF);
        this.lit = new SBoolean(this, false);
        this.syncables = Arrays.asList((ISyncable) this.color, this.lit);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.color.setValue(compound.getInteger("color"));
        this.lit.setValue(compound.getBoolean("lit"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("color", this.color.getValue());
        compound.setBoolean("lit", this.lit.getValue());
    }

    @Override
    public void updateEntity() {}

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("tracking", true);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager netManager, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(netManager, packet);

        if (packet.func_148857_g().hasKey("tracking", 1) && packet.func_148857_g().getBoolean("tracking")) {
            SpACore.packetHandler.sendPacketToServer(new Packet1TrackingStatus(this, true));
        }
    }

    // ISyncableObjectOwner

    @Override
    public boolean isNotValid() {
        return this.isInvalid();
    }

    @Override
    public List<ISyncable> getSyncables() {
        return this.syncables;
    }

    @Override
    public void readSetup(ByteArrayDataInput in) throws IOException {
        List<ISyncable> syncables = this.getSyncables();
        for (int i = 0; i < syncables.size(); i++) {
            ISyncable syncable = syncables.get(i);
            syncable.setId(in.readInt());
            syncable.read(in);
        }
    }

    @Override
    public void writeSetup(DataOutputStream out) throws IOException {
        List<ISyncable> syncables = this.getSyncables();
        for (int i = 0; i < syncables.size(); i++) {
            ISyncable syncable = syncables.get(i);
            out.writeInt(syncable.getId());
            syncable.write(out);
        }
    }

    @Override
    public String getIdentifier() {
        return "TileEntityColoredLamp_" + this.xCoord + ";" + this.yCoord + ";" + this.zCoord;
    }

    @Override
    public boolean isWorldBound() {
        return true;
    }

    @Override
    public World getWorld() {
        return this.getWorldObj();
    }

    @Override
    public int getPosX() {
        return this.xCoord;
    }

    @Override
    public int getPosY() {
        return this.yCoord;
    }

    @Override
    public int getPosZ() {
        return this.zCoord;
    }

    @Override
    public void onDataChanged(ISyncable syncable) {
        if (syncable == this.lit) {
            this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

}
