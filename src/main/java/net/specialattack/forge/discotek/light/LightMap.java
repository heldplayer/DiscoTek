
package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;

import net.specialattack.forge.discotek.block.BlockLight;

public class LightMap implements ILight {

    private final List<Channels> channels;

    private boolean isLED;

    public LightMap(boolean isLED) {
        if (isLED) {
            this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.TILT, Channels.PAN, Channels.FOCUS, Channels.RED, Channels.GREEN, Channels.BLUE);
        }
        else {
            this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.TILT, Channels.PAN, Channels.FOCUS);
        }
        this.isLED = isLED;
    }

    @Override
    public List<Channels> getChannels() {
        return this.channels;
    }

    @Override
    public boolean supportsLens() {
        return !this.isLED;
    }

    @Override
    public int getRedstonePower(int channelValue) {
        return 0;
    }

    @Override
    public String getIdentifier() {
        return this.isLED ? "mapLED" : "map";
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
