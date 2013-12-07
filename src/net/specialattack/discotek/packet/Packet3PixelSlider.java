
package net.specialattack.discotek.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.discotek.controllers.ControllerPixel;
import net.specialattack.discotek.controllers.IControllerInstance;
import net.specialattack.discotek.tileentity.TileEntityController;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet3PixelSlider extends HeldCorePacket {

    public int posX;
    public int posY;
    public int posZ;
    public int id;
    public int level;

    public Packet3PixelSlider(int packetId) {
        super(packetId, null);
    }

    public Packet3PixelSlider(ControllerPixel.ControllerInstance controller, int id, int level) {
        super(3, null);

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
    public void read(ByteArrayDataInput in) throws IOException {
        this.posX = in.readInt();
        this.posY = in.readInt();
        this.posZ = in.readInt();

        this.id = in.readInt();
        this.level = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);

        out.writeInt(this.id);
        out.writeInt(this.level);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getBlockTileEntity(posX, posY, posZ);

        if (tile != null && tile instanceof TileEntityController) {
            IControllerInstance controller = ((TileEntityController) tile).getControllerInstance();

            if (controller != null && controller instanceof ControllerPixel.ControllerInstance) {
                ((ControllerPixel.ControllerInstance) controller).doSlider(id, level);
            }

            tile.onInventoryChanged();
        }
    }

}
