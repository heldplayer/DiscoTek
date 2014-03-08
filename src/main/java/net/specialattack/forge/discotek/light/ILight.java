
package net.specialattack.forge.discotek.light;

import java.util.List;

import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public interface ILight {

    ILightInstance createInstance(TileEntityLight tile);

    List<Channels> getChannels();

    boolean supportsLens();

    String getIdentifier();

}
