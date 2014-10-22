package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightDimmerInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightDimmer implements ILight {

    private final List<Channels> channels;

    public LightDimmer() {
        this.channels = Arrays.asList(Channels.REDSTONE);
    }

    @Override
    public ILightInstance createInstance(TileEntityLight tile) {
        return new LightDimmerInstance(tile);
    }

    @Override
    public List<Channels> getChannels() {
        return this.channels;
    }

    @Override
    public boolean supportsLens() {
        return false;
    }

    @Override
    public String getIdentifier() {
        return "dimmer";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean extra) {
    }

}
