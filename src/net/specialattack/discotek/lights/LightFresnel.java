
package net.specialattack.discotek.lights;

import java.util.Arrays;
import java.util.List;

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

}
