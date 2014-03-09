
package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;

import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightMapInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightMap implements ILight {

    private final List<Channels> channels;

    public LightMap() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.PITCH, Channels.ROTATION, Channels.FOCUS);
    }

    @Override
    public ILightInstance createInstance(TileEntityLight tile) {
        return new LightMapInstance(tile);
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
    public String getIdentifier() {
        return "map";
    }

}
