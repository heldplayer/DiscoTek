
package net.specialattack.forge.discotek.lights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.specialattack.forge.core.sync.ISyncableObjectOwner;
import net.specialattack.forge.core.sync.SBoolean;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightFresnel implements ILight {

    private final List<Channels> channels;

    public LightFresnel() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS);
    }

    @Override
    public boolean supportsLens() {
        return true;
    }

    @Override
    public List<ChannelSyncablePair> createSyncables(ISyncableObjectOwner owner) {
        ArrayList<ChannelSyncablePair> result = new ArrayList<ChannelSyncablePair>();
        for (Channels channel : this.channels) {
            result.add(new ChannelSyncablePair(channel.identifier, channel, channel.createSyncable(owner)));
        }
        result.add(new ChannelSyncablePair("hasLens", null, new SBoolean(owner)));
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<Channels> getChannels() {
        return this.channels;
    }

    @Override
    public int getRedstonePower(int channelValue) {
        return 0;
    }

    @Override
    public String getIdentifier() {
        return "fresnel";
    }

    @Override
    public void setBlockBounds(BlockLight block, TileEntityLight tile) {
        block.setBlockBounds(0.0625F, 0.125F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

}
