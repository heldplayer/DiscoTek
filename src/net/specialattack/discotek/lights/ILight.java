
package net.specialattack.discotek.lights;

import java.util.List;

import net.specialattack.discotek.block.BlockLight;

public interface ILight {

    List<Channels> getChannels();

    boolean supportsLens();

    int getRedstonePower(int channelValue);

    String getIdentifier();

    void setBlockBounds(BlockLight block, int direction);

}
