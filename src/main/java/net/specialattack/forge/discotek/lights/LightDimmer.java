
package net.specialattack.forge.discotek.lights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.specialattack.forge.core.sync.ISyncableObjectOwner;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightDimmer implements ILight {

    private final List<Channels> channels;

    public LightDimmer() {
        this.channels = Arrays.asList(Channels.STRENGTH);
    }

    @Override
    public boolean supportsLens() {
        return false;
    }

    @Override
    public List<ChannelSyncablePair> createSyncables(ISyncableObjectOwner owner) {
        ArrayList<ChannelSyncablePair> result = new ArrayList<ChannelSyncablePair>();
        for (Channels channel : this.channels) {
            result.add(new ChannelSyncablePair(channel.identifier, channel, channel.createSyncable(owner)));
        }
        result.add(new ChannelSyncablePair("direction", null, new SInteger(owner)));
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<Channels> getChannels() {
        return this.channels;
    }

    @Override
    public int getRedstonePower(int channelValue) {
        return (int) (channelValue / 16.0F);
    }

    @Override
    public String getIdentifier() {
        return "dimmer";
    }

    @Override
    public void setBlockBounds(BlockLight block, TileEntityLight tile) {
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
    }

}
