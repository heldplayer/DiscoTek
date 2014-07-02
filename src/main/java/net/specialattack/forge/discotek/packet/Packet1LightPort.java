
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
    public int type;

    public int iValue;
    public String sValue;
    public boolean bValue;

    public Packet1LightPort() {
        super(null);
    }

    public Packet1LightPort(TileEntityLight tile, int channelId, int value) {
        super(tile.getWorldObj());

        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.channelId = channelId;
        this.type = 0;
        this.iValue = value;
    }

    public Packet1LightPort(TileEntityLight tile, int channelId, String value) {
        super(tile.getWorldObj());

        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.channelId = channelId;
        this.type = 1;
        this.sValue = value;
    }

    public Packet1LightPort(TileEntityLight tile, int channelId, boolean value) {
        super(tile.getWorldObj());

        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.channelId = channelId;
        this.type = 2;
        this.bValue = value;
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
        this.type = in.readInt();
        switch (this.type) {
        case 0:
            this.iValue = in.readInt();
        break;
        case 1:
            this.iValue = in.readInt();
            byte[] data = new byte[this.iValue];
            in.readBytes(data);
            this.sValue = new String(data);
        break;
        case 2:
            this.bValue = in.readBoolean();
        break;
        }
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.channelId);
        out.writeInt(this.type);
        switch (this.type) {
        case 0:
            out.writeInt(this.iValue);
        break;
        case 1:
            byte[] data = this.sValue.getBytes();
            out.writeInt(data.length);
            out.writeBytes(data);
        break;
        case 2:
            out.writeBoolean(this.bValue);
        break;
        }
    }

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityLight) {
            TileEntityLight light = (TileEntityLight) tile;

            switch (this.type) {
            case 0:
                light.channels[this.channelId].port = this.iValue;
            break;
            case 1:
                light.channels[this.channelId].setValue(this.sValue);
            break;
            case 2:
                light.channels[this.channelId].setValue(this.bValue);
            break;
            }

            light.markDirty();
        }
    }

}
