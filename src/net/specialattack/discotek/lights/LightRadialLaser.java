
package net.specialattack.discotek.lights;

import java.util.Arrays;
import java.util.List;

public class LightRadialLaser implements ILight {

    private final List<Channels> channels;

    public LightRadialLaser() {
        this.channels = Arrays.asList((Channels) Channels.BRIGHTNESS, Channels.TILT, Channels.PAN, Channels.FOCUS, Channels.RED, Channels.GREEN, Channels.BLUE);
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

}
