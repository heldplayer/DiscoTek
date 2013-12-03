
package net.specialattack.discotek.lights;

import java.util.Arrays;
import java.util.List;

public class LightMap implements ILight {

    private final List<Channels> channels;

    private boolean isLED;

    public LightMap(boolean isLED) {
        if (isLED) {
            this.channels = Arrays.asList((Channels) Channels.BRIGHTNESS, Channels.TILT, Channels.PAN, Channels.FOCUS);
        }
        else {
            this.channels = Arrays.asList((Channels) Channels.BRIGHTNESS, Channels.TILT, Channels.PAN, Channels.FOCUS, Channels.RED, Channels.GREEN, Channels.BLUE);
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

}
