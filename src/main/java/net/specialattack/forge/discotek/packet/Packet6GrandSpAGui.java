package net.specialattack.forge.discotek.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.forge.core.packet.SpACorePacket;
import net.specialattack.forge.discotek.Instruction;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.controller.instance.ControllerGrandSpAInstance;
import net.specialattack.forge.discotek.controller.instance.IControllerInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityController;

import java.io.IOException;

public class Packet6GrandSpAGui extends SpACorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public String[] instructions;
    public int[] arguments;

    public Packet6GrandSpAGui() {
        super(null);
    }

    public Packet6GrandSpAGui(ControllerGrandSpAInstance controller) {
        super(controller.tile.getWorldObj());

        this.posX = controller.tile.xCoord;
        this.posY = controller.tile.yCoord;
        this.posZ = controller.tile.zCoord;

        this.instructions = new String[controller.instructions.length];
        this.arguments = new int[controller.instructions.length];

        for (int i = 0; i < controller.instructions.length; i++) {
            Instruction instruction = controller.instructions[i];
            if (instruction != null) {
                this.instructions[i] = controller.instructions[i].identifier;
                this.arguments[i] = controller.instructions[i].argument;
            } else {
                this.instructions[i] = "NOOP";
                this.arguments[i] = 0;
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
        this.instructions = new String[length];
        this.arguments = new int[length];

        for (int i = 0; i < length; i++) {
            this.arguments[i] = in.readInt();
            byte[] data = new byte[in.readInt()];
            in.readBytes(data);
            this.instructions[i] = new String(data);
        }
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.instructions.length);
        for (int i = 0; i < this.instructions.length; i++) {
            out.writeInt(this.arguments[i]);
            byte[] data = this.instructions[i].getBytes();
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityController) {
            TileEntityController controller = (TileEntityController) tile;
            IControllerInstance instance = controller.getControllerInstance();

            if (instance != null && instance instanceof ControllerGrandSpAInstance) {
                ControllerGrandSpAInstance grandSpA = (ControllerGrandSpAInstance) instance;

                grandSpA.instructions = new Instruction[this.instructions.length];
                for (int i = 0; i < this.instructions.length; i++) {
                    grandSpA.instructions[i] = new Instruction();
                    grandSpA.instructions[i].identifier = this.instructions[i];
                    grandSpA.instructions[i].argument = this.arguments[i];
                }
            }

            ModDiscoTek.proxy.openControllerGui(controller);
        }
    }

}
