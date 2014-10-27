package net.specialattack.forge.discotek.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.forge.core.packet.Attributes;
import net.specialattack.forge.discotek.Instruction;
import net.specialattack.forge.discotek.controller.instance.ControllerGrandSpAInstance;
import net.specialattack.forge.discotek.controller.instance.IControllerInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityController;

public class Packet5GrandSpAInstruction extends DiscoTekPacket {

    public int posX;
    public int posY;
    public int posZ;
    public int line;
    public String instruction;
    public int argument;

    public Packet5GrandSpAInstruction() {
        super(null);
    }

    public Packet5GrandSpAInstruction(ControllerGrandSpAInstance controller, int line, String value, int argument) {
        super(controller.tile.getWorldObj());

        this.posX = controller.tile.xCoord;
        this.posY = controller.tile.yCoord;
        this.posZ = controller.tile.zCoord;

        this.line = line;
        this.instruction = value;
        this.argument = argument;
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

        this.line = in.readInt();
        byte[] data = new byte[in.readInt()];
        in.readBytes(data);
        this.instruction = new String(data);
        this.argument = in.readInt();
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.line);
        byte[] data = this.instruction.getBytes();
        out.writeInt(data.length);
        out.writeBytes(data);
        out.writeInt(this.argument);
    }

    @Override
    public void onData(ChannelHandlerContext context) {
        if (context.attr(Attributes.SENDING_PLAYER).get() == null) {
            return;
        }
        World world = context.attr(Attributes.SENDING_PLAYER).get().worldObj;

        TileEntity tile = world.getTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityController) {
            IControllerInstance controller = ((TileEntityController) tile).getControllerInstance();

            if (controller != null && controller instanceof ControllerGrandSpAInstance) {
                ControllerGrandSpAInstance grandSpA = (ControllerGrandSpAInstance) controller;

                grandSpA.instructions[this.line] = new Instruction();
                grandSpA.instructions[this.line].identifier = this.instruction;
                grandSpA.instructions[this.line].argument = this.argument;
            }

            tile.markDirty();
        }
    }

}
