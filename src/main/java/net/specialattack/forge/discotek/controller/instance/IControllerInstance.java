
package net.specialattack.forge.discotek.controller.instance;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;

public interface IControllerInstance {

    void doTick();

    void writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    boolean doesTick();

    void openGui(EntityPlayer player, Side side);

    void prepareServer();

    boolean onRightClick(EntityPlayer player, boolean sneaking);

}
