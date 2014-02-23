
package net.specialattack.forge.discotek.lights;

import java.util.List;

import net.specialattack.forge.core.sync.ISyncableObjectOwner;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public interface ILight {

    boolean supportsLens();

    List<ChannelSyncablePair> createSyncables(ISyncableObjectOwner owner);

    List<Channels> getChannels();

    int getRedstonePower(int channelValue);

    String getIdentifier();

    void setBlockBounds(BlockLight block, TileEntityLight light);

}
