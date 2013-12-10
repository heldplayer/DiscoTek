
package net.specialattack.discotek.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.discotek.tileentity.TileEntityLight;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet1LightPort extends HeldCorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public int channelId;
    public int port;

    public Packet1LightPort(int packetId) {
        super(packetId, null);
    }

    public Packet1LightPort(TileEntityLight tile, int channelId, int port) {
        super(1, null);

        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.channelId = channelId;
        this.port = port;
    }

    @Override
    public Side getSendingSide() {
        return null;
    }

    @Override
    public void read(ByteArrayDataInput in) throws IOException {
        this.posX = in.readInt();
        this.posY = in.readInt();
        this.posZ = in.readInt();

        this.channelId = in.readInt();
        this.port = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.channelId);
        out.writeInt(this.port);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getBlockTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityLight) {
            TileEntityLight light = (TileEntityLight) tile;

            light.channels[this.channelId].port = this.port;

            light.onInventoryChanged();
        }
    }

}
