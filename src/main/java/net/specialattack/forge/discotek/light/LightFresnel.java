
package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;

import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightFresnelInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightFresnel implements ILight {

    private final List<Channels> channels;

    public LightFresnel() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS);
    }

    @Override
    public ILightInstance createInstance(TileEntityLight tile) {
        return new LightFresnelInstance(tile);
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
        return "fresnel";
    }

}
