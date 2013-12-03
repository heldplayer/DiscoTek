
package net.specialattack.discotek.lights;

import java.util.List;

// Fresnel
// Map
// MapLED
// Dimmer
// RadialLaser
public interface ILight {

    List<Channels> getChannels();

    boolean supportsLens();

    int getRedstonePower(int channelValue);

    String getIdentifier();

}
