
package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;

import net.specialattack.forge.discotek.block.BlockLight;

public class LightPositionableLaser implements ILight {

    private final List<Channels> channels;

    public LightPositionableLaser() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.LENGTH, Channels.TILT, Channels.PAN, Channels.FOCUS, Channels.RED, Channels.GREEN, Channels.BLUE);
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
        return 0;
    }

    @Override
    public String getIdentifier() {
        return "radiallaser";
    }

    @Override
    public void setBlockBounds(BlockLight block, int direction) {
        switch (direction) {
        case 0:
            block.setBlockBounds(0.0625F, 0.0625F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
        break;
        case 1:
            block.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.9375F, 0.9375F);
        break;
        case 2:
            block.setBlockBounds(0.0625F, 0.0625F, 0.0625F, 0.9375F, 0.9375F, 1.0F);
        break;
        case 3:
            block.setBlockBounds(0.0625F, 0.0625F, 0.0F, 0.9375F, 0.9375F, 0.9375F);
        break;
        case 4:
            block.setBlockBounds(0.0625F, 0.0625F, 0.0625F, 1.0F, 0.9375F, 0.9375F);
        break;
        case 5:
            block.setBlockBounds(0.0F, 0.0625F, 0.0625F, 0.9375F, 0.9375F, 0.9375F);
        break;
        }
    }

}
