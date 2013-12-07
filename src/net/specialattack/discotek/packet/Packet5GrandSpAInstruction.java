
package net.specialattack.discotek.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.discotek.Instruction;
import net.specialattack.discotek.controllers.ControllerGrandSpa;
import net.specialattack.discotek.controllers.IControllerInstance;
import net.specialattack.discotek.tileentity.TileEntityController;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet5GrandSpAInstruction extends HeldCorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public int line;
    public String instruction;
    public int argument;

    public Packet5GrandSpAInstruction(int packetId) {
        super(packetId, null);
    }

    public Packet5GrandSpAInstruction(ControllerGrandSpa.ControllerInstance controller, int line, String value, int argument) {
        super(5, null);

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
    public void read(ByteArrayDataInput in) throws IOException {
        this.posX = in.readInt();
        this.posY = in.readInt();
        this.posZ = in.readInt();

        this.line = in.readInt();
        byte[] data = new byte[in.readInt()];
        in.readFully(data);
        this.instruction = new String(data);
        this.argument = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.line);
        byte[] data = this.instruction.getBytes();
        out.writeInt(data.length);
        out.write(data);
        out.writeInt(this.argument);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getBlockTileEntity(posX, posY, posZ);

        if (tile != null && tile instanceof TileEntityController) {
            IControllerInstance controller = ((TileEntityController) tile).getControllerInstance();

            if (controller != null && controller instanceof ControllerGrandSpa.ControllerInstance) {
                ControllerGrandSpa.ControllerInstance grandSpA = (ControllerGrandSpa.ControllerInstance) controller;

                grandSpA.instructions[this.line] = new Instruction();
                grandSpA.instructions[this.line].identifier = this.instruction;
                grandSpA.instructions[this.line].argument = this.argument;
            }

            tile.onInventoryChanged();
        }
    }

}
