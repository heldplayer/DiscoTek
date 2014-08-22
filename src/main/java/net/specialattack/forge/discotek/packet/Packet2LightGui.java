package net.specialattack.forge.discotek.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.forge.core.sync.SBoolean;
import net.specialattack.forge.core.sync.SString;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import java.io.IOException;

public class Packet2LightGui extends DiscoTekPacket {

    public int posX;
    public int posY;
    public int posZ;
    public int[] channels;
    public int[] iValues;
    public String[] sValues;
    public boolean[] bValues;
    public int[] types;

    public Packet2LightGui() {
        super(null);
    }

    public Packet2LightGui(TileEntityLight tile) {
        super(tile.getWorldObj());

        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.channels = new int[tile.channels.length];
        this.iValues = new int[tile.channels.length];
        this.sValues = new String[tile.channels.length];
        this.bValues = new boolean[tile.channels.length];
        this.types = new int[tile.channels.length];

        for (int i = 0; i < tile.channels.length; i++) {
            this.channels[i] = tile.channels[i].channel.id;
            this.types[i] = tile.channels[i].channel.type;
            switch (tile.channels[i].channel.type) {
                case 0:
                    this.iValues[i] = tile.channels[i].port;
                    break;
                case 1:
                    this.sValues[i] = ((SString) tile.channels[i].syncable).getValue();
                    break;
                case 2:
                    this.bValues[i] = ((SBoolean) tile.channels[i].syncable).getValue();
                    break;
            }
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
        this.iValues = new int[length];
        this.sValues = new String[length];
        this.bValues = new boolean[length];
        this.types = new int[length];

        for (int i = 0; i < length; i++) {
            this.channels[i] = in.readInt();
            this.types[i] = in.readInt();
            switch (this.types[i]) {
                case 0:
                    this.iValues[i] = in.readInt();
                    break;
                case 1:
                    this.iValues[i] = in.readInt();
                    byte[] data = new byte[this.iValues[i]];
                    in.readBytes(data);
                    this.sValues[i] = new String(data);
                    break;
                case 2:
                    this.bValues[i] = in.readBoolean();
                    break;
            }
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
            out.writeInt(this.types[i]);
            switch (this.types[i]) {
                case 0:
                    out.writeInt(this.iValues[i]);
                    break;
                case 1:
                    byte[] data = this.sValues[i].getBytes();
                    out.writeInt(data.length);
                    out.writeBytes(data);
                    break;
                case 2:
                    out.writeBoolean(this.bValues[i]);
                    break;
            }
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
                switch (this.types[i]) {
                    case 0:
                        light.setPort(this.channels[i], this.iValues[i]);
                        break;
                    case 1:
                        light.channels[i].setValue(this.sValues);
                        break;
                    case 2:
                        light.channels[i].setValue(this.bValues);
                        break;
                }
            }

            ModDiscoTek.proxy.openLightGui(light);
        }
    }

}
