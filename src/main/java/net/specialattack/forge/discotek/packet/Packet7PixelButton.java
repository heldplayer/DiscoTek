package net.specialattack.forge.discotek.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.controller.instance.ControllerPixelInstance;
import net.specialattack.forge.discotek.controller.instance.IControllerInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityController;

public class Packet7PixelButton extends DiscoTekPacket {

    public int posX;
    public int posY;
    public int posZ;

    public Packet7PixelButton() {
        super(null);
    }

    public Packet7PixelButton(ControllerPixelInstance controller) {
        super(controller.tile.getWorldObj());

        this.posX = controller.tile.xCoord;
        this.posY = controller.tile.yCoord;
        this.posZ = controller.tile.zCoord;
    }

    @Override
    public Side getSendingSide() {
        return Side.CLIENT;
    }

    @Override
    public void read(ChannelHandlerContext context, ByteBuf in) throws IOException {
        this.posX = in.readInt();
        this.posY = in.readInt();
        this.posZ = in.readInt();
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        out.writeInt(this.posX);
        out.writeInt(this.posY);
        out.writeInt(this.posZ);
    }

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        World world = player.worldObj;

        TileEntity tile = world.getTileEntity(this.posX, this.posY, this.posZ);

        if (tile != null && tile instanceof TileEntityController) {
            TileEntityController controller = (TileEntityController) tile;
            IControllerInstance instance = controller.getControllerInstance();

            if (instance != null && instance instanceof ControllerPixelInstance) {
                instance.resendChannels();
            }

            ModDiscoTek.proxy.openControllerGui(controller);
        }
    }

}
