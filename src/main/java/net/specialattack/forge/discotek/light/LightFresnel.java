
package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;

import net.specialattack.forge.discotek.block.BlockLight;

public class LightFresnel implements ILight {

    private final List<Channels> channels;

    public LightFresnel() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS);
    }

    @Override
    public List<Channels> getChannels() {
        return this.channels;
    }

    @Override
    public boolean supportsLens() {
        return true;
    }

    @Override
    public int getRedstonePower(int channelValue) {
        return 0;
    }

    @Override
    public String getIdentifier() {
        return "fresnel";
    }

    @Override
    public void setBlockBounds(BlockLight block, int direction) {
        block.setBlockBounds(0.0625F, 0.125F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

}
