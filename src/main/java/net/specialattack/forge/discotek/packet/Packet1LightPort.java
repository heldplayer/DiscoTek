
package net.specialattack.forge.discotek.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.forge.core.packet.SpACorePacket;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.relauncher.Side;

public class Packet1LightPort extends SpACorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public int channelId;
    public boolean isString;
    public int port;
    public String value;

    public Packet1LightPort() {
        super(null);
    }

    public Packet1LightPort(TileEntityLight tile, int channelId, int port) {
        super(tile.getWorldObj());

        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.channelId = channelId;
        this.isString = false;
        this.port = port;
    }

    public Packet1LightPort(TileEntityLight tile, int channelId, String value) {
        super(tile.getWorldObj());

        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.channelId = channelId;
        this.isString = true;
        this.value = value;
    }

    @Override
    public Side getSendingSide() {
        return null;
    }

    @Override
    public void read(ChannelHandlerContext context, ByteBuf in) throws IOException {
        this.posX = in.readInt();
        this.posY = in.readInt();
        this.posZ = in.readInt();

        this.channelId = in.readInt();
        this.isString = in.readBoolean();
        this.port = in.readInt();
        if (this.isString) {
            byte[] data = new byte[this.port];
            in.readBytes(data);
            this.value = new String(data);
        }
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.channelId);
        out.writeBoolean(this.isString);
        if (this.isString) {
            byte[] data = this.value.getBytes();
            out.writeInt(data.length);
            out.writeBytes(data);
        }
        else {
            out.writeInt(this.port);
        }
    }

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityLight) {
            TileEntityLight light = (TileEntityLight) tile;

            if (this.isString) {
                light.channels[this.channelId].setValue(this.value);
            }
            else {
                light.channels[this.channelId].port = this.port;
            }

            light.markDirty();
        }
    }

}
