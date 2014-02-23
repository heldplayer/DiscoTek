
package net.specialattack.forge.discotek.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.forge.core.packet.SpACorePacket;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.relauncher.Side;

public class Packet2LightGui extends SpACorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public int[] channels;
    public int[] ports;

    public Packet2LightGui() {
        super(null);
    }

    public Packet2LightGui(TileEntityLight tile) {
        super(tile.getWorldObj());

        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.channels = new int[tile.channels.size()];
        this.ports = new int[tile.channels.size()];

        for (int i = 0; i < tile.channels.size(); i++) {
            this.channels[i] = tile.channels.get(i).channel.id;
            this.ports[i] = tile.channels.get(i).port;
        }
    }

    @Override
    public Side getSendingSide() {
        return Side.SERVER;
    }

    @Override
    public void read(ChannelHandlerContext context, ByteBuf in) throws IOException {
        this.posX = in.readInt();
        this.posY = in.readInt();
        this.posZ = in.readInt();

        int length = in.readInt();
        this.channels = new int[length];
        this.ports = new int[length];

        for (int i = 0; i < length; i++) {
            this.channels[i] = in.readInt();
            this.ports[i] = in.readInt();
        }
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.channels.length);
        for (int i = 0; i < this.channels.length; i++) {
            out.writeInt(this.channels[i]);
            out.writeInt(this.ports[i]);
        }
    }

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityLight) {
            TileEntityLight light = (TileEntityLight) tile;

            if (light.channels == null) {
                light.setupChannels();
            }

            for (int i = 0; i < this.channels.length; i++) {
                light.setPort(this.channels[i], this.ports[i]);
            }

            ModDiscoTek.proxy.openLightGui(light);
        }
    }

}
