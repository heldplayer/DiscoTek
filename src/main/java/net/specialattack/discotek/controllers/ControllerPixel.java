
package net.specialattack.discotek.controllers;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.client.gui.GuiControllerPixel;
import net.specialattack.discotek.packet.Packet4PixelGui;
import net.specialattack.discotek.packet.PacketHandler;
import net.specialattack.discotek.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ControllerPixel implements IController {

    private Icon bottom;
    private Icon top;
    private Icon side;

    @Override
    public IControllerInstance createInstance(TileEntityController tile) {
        return new ControllerInstance(tile);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.top = register.registerIcon(Assets.DOMAIN + "controller-top0");
        this.bottom = register.registerIcon(Assets.DOMAIN + "controller-bottom0");
        this.side = register.registerIcon(Assets.DOMAIN + "controller-side0");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side) {
        if (side == 0) {
            return this.bottom;
        }
        else if (side == 1) {
            return this.top;
        }
        return this.side;
    }

    @Override
    public String getIdentifier() {
        return "pixel";
    }

    public static class ControllerInstance implements IControllerInstance {

        public TileEntityController tile;
        public int[] levels;

        public ControllerInstance(TileEntityController tile) {
            this.tile = tile;
        }

        @Override
        public void doTick() {}

        @Override
        public void writeToNBT(NBTTagCompound compound) {
            compound.setIntArray("levels", this.levels);
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            this.levels = compound.getIntArray("levels");
        }

        @Override
        public boolean doesTick() {
            return false;
        }

        @Override
        public void openGui(EntityPlayer player, Side side) {
            if (side == Side.CLIENT) {
                FMLClientHandler.instance().displayGuiScreen(player, new GuiControllerPixel(this));
            }
            else {
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(PacketHandler.instance.createPacket(new Packet4PixelGui(this)));
                }
            }
        }

        @Override
        public void prepareServer() {
            if (this.levels != null && this.levels.length != 24) {
                this.levels = new int[24];
            }
            else if (this.levels == null) {
                this.levels = new int[24];
            }
        }

        public void doSlider(int id, int level) {
            this.levels[id] = level;
            this.tile.transmitLevelChange(id, level);
        }

    }

}
