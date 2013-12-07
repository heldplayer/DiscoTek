
package net.specialattack.discotek.lights;

import java.util.List;

public interface ILight {

    List<Channels> getChannels();

    boolean supportsLens();

    int getRedstonePower(int channelValue);

    String getIdentifier();

}
