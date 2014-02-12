
package net.specialattack.forge.discotek.lights;

import java.util.List;

import net.specialattack.forge.discotek.block.BlockLight;

public interface ILight {

    List<Channels> getChannels();

    boolean supportsLens();

    int getRedstonePower(int channelValue);

    String getIdentifier();

    void setBlockBounds(BlockLight block, int direction);

}
