
package net.specialattack.discotek.lights;

import java.util.Arrays;
import java.util.List;

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
        return (int) ((float) channelValue / 16.0F);
    }

    @Override
    public String getIdentifier() {
        return "dimmer";
    }

}
