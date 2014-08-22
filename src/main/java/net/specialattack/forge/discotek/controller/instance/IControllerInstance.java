package net.specialattack.forge.discotek.controller.instance;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IControllerInstance {

    void doTick();

    void writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    boolean doesTick();

    void openGuiServer(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    void openGuiClient(EntityPlayer player);

    void prepareServer();

    boolean onRightClick(EntityPlayer player, boolean sneaking);

    void resendChannels();

}
