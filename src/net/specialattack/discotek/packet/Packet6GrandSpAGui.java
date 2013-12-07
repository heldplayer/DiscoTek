
package net.specialattack.discotek.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.discotek.Instruction;
import net.specialattack.discotek.ModDiscoTek;
import net.specialattack.discotek.controllers.ControllerGrandSpa;
import net.specialattack.discotek.controllers.IControllerInstance;
import net.specialattack.discotek.tileentity.TileEntityController;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet6GrandSpAGui extends HeldCorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public String[] instructions;
    public int[] arguments;

    public Packet6GrandSpAGui(int packetId) {
        super(packetId, null);
    }

    public Packet6GrandSpAGui(ControllerGrandSpa.ControllerInstance controller) {
        super(6, null);

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
            }
            else {
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
    public void read(ByteArrayDataInput in) throws IOException {
        this.posX = in.readInt();
        this.posY = in.readInt();
        this.posZ = in.readInt();

        int length = in.readInt();
        this.instructions = new String[length];
        this.arguments = new int[length];

        for (int i = 0; i < length; i++) {
            this.arguments[i] = in.readInt();
            byte[] data = new byte[in.readInt()];
            in.readFully(data);
            this.instructions[i] = new String(data);
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.instructions.length);
        for (int i = 0; i < this.instructions.length; i++) {
            out.writeInt(this.arguments[i]);
            byte[] data = this.instructions[i].getBytes();
            out.writeInt(data.length);
            out.write(data);
        }
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getBlockTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityController) {
            TileEntityController controller = (TileEntityController) tile;
            IControllerInstance instance = controller.getControllerInstance();

            if (instance != null && instance instanceof ControllerGrandSpa.ControllerInstance) {
                ControllerGrandSpa.ControllerInstance grandSpA = (ControllerGrandSpa.ControllerInstance) instance;

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
