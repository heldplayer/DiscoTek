
package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;

import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightHologramInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightHologram implements ILight {

    private final List<Channels> channels;

    public LightHologram() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.PITCH, Channels.HEAD_ROTATION, Channels.YAW, Channels.SIZE, Channels.RED, Channels.GREEN, Channels.BLUE, Channels.NAME);
    }

    @Override
    public ILightInstance createInstance(TileEntityLight tile) {
        return new LightHologramInstance(tile);
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
        return "hologram";
    }

}
