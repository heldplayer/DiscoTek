package net.specialattack.forge.discotek.light;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public interface ILight {

    ILightInstance createInstance(TileEntityLight tile);

    List<Channels> getChannels();

    boolean supportsLens();

    String getIdentifier();

    void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean extra);

}
