
package net.specialattack.forge.discotek.controller.instance;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.client.gui.GuiControllerPixel;
import net.specialattack.forge.discotek.packet.Packet4PixelGui;
import net.specialattack.forge.discotek.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;

public class ControllerPixelInstance implements IControllerInstance {

    public TileEntityController tile;
    public int[] levels;

    public ControllerPixelInstance(TileEntityController tile) {
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
            ModDiscoTek.packetHandler.sendPacketToPlayer(new Packet4PixelGui(this), player);
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

    @Override
    public boolean onRightClick(EntityPlayer player, boolean sneaking) {
        return false;
    }

}
