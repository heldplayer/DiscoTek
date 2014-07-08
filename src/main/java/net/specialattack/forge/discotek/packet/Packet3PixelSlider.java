package net.specialattack.forge.discotek.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.forge.core.packet.SpACorePacket;
import net.specialattack.forge.discotek.controller.instance.ControllerPixelInstance;
import net.specialattack.forge.discotek.controller.instance.IControllerInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityController;

import java.io.IOException;

public class Packet3PixelSlider extends SpACorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public int id;
    public int level;

    public Packet3PixelSlider() {
        super(null);
    }

    public Packet3PixelSlider(ControllerPixelInstance controller, int id, int level) {
        super(controller.tile.getWorldObj());

        this.posX = controller.tile.xCoord;
        this.posY = controller.tile.yCoord;
        this.posZ = controller.tile.zCoord;

        this.id = id;
        this.level = level;
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

        this.id = in.readInt();
        this.level = in.readInt();
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.id);
        out.writeInt(this.level);
    }

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityController) {
            IControllerInstance controller = ((TileEntityController) tile).getControllerInstance();

            if (controller != null && controller instanceof ControllerPixelInstance) {
                ((ControllerPixelInstance) controller).doSlider(this.id, this.level);
            }

            tile.markDirty();
        }
    }

}
