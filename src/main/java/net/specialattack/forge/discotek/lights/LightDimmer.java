
package net.specialattack.forge.discotek.lights;

import java.util.Arrays;
import java.util.List;

import net.specialattack.forge.discotek.block.BlockLight;

public class LightDimmer implements ILight {

    private final List<Channels> channels;

    public LightDimmer() {
        this.channels = Arrays.asList(Channels.STRENGTH);
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
    public int getRedstonePower(int channelValue) {
        return (int) (channelValue / 16.0F);
    }

    @Override
    public String getIdentifier() {
        return "dimmer";
    }

    @Override
    public void setBlockBounds(BlockLight block, int direction) {
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
    }

}
