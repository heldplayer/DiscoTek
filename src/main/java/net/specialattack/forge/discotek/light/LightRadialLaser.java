
package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;

import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightRadialLaserInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightRadialLaser implements ILight {

    private final List<Channels> channels;

    public LightRadialLaser() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.LENGTH, Channels.ROTATION, Channels.FOCUS, Channels.RED, Channels.GREEN, Channels.BLUE);
    }

    @Override
    public ILightInstance createInstance(TileEntityLight tile) {
        return new LightRadialLaserInstance(tile);
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
        return "radiallaser";
    }

}
