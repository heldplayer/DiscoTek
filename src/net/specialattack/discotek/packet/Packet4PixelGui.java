
package net.specialattack.discotek.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.discotek.ModDiscoTek;
import net.specialattack.discotek.controllers.ControllerPixel;
import net.specialattack.discotek.controllers.IControllerInstance;
import net.specialattack.discotek.tileentity.TileEntityController;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet4PixelGui extends HeldCorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public int[] levels;

    public Packet4PixelGui(int packetId) {
        super(packetId, null);
    }

    public Packet4PixelGui(ControllerPixel.ControllerInstance controller) {
        super(4, null);

        this.posX = controller.tile.xCoord;
        this.posY = controller.tile.yCoord;
        this.posZ = controller.tile.zCoord;

        this.levels = controller.levels;
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
        this.levels = new int[length];

        for (int i = 0; i < length; i++) {
            this.levels[i] = in.readInt();
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.levels.length);
        for (int i = 0; i < this.levels.length; i++) {
            out.writeInt(this.levels[i]);
        }
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getBlockTileEntity(posX, posY, posZ);

        if (tile != null && tile instanceof TileEntityController) {
            TileEntityController controller = (TileEntityController) tile;
            IControllerInstance instance = controller.getControllerInstance();

            if (instance != null && instance instanceof ControllerPixel.ControllerInstance) {
                ((ControllerPixel.ControllerInstance) instance).levels = this.levels;
            }

            ModDiscoTek.proxy.openControllerGui(controller);
        }
    }

}
