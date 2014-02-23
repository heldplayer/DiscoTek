
package net.specialattack.forge.discotek.lights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.specialattack.forge.core.sync.ISyncableObjectOwner;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightRadialLaser implements ILight {

    private final List<Channels> channels;

    public LightRadialLaser() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.TILT, Channels.PAN, Channels.FOCUS, Channels.RED, Channels.GREEN, Channels.BLUE);
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
        return 0;
    }

    @Override
    public String getIdentifier() {
        return "radiallaser";
    }

    @Override
    public void setBlockBounds(BlockLight block, TileEntityLight tile) {
        switch (tile.getDirection()) {
        case 0:
            block.setBlockBounds(0.125F, 0.5625F, 0.125F, 0.875F, 1.0F, 0.875F);
        break;
        case 1:
            block.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.4375F, 0.875F);
        break;
        case 2:
            block.setBlockBounds(0.125F, 0.125F, 0.5625F, 0.875F, 0.875F, 1.0F);
        break;
        case 3:
            block.setBlockBounds(0.125F, 0.125F, 0.0F, 0.875F, 0.875F, 0.4375F);
        break;
        case 4:
            block.setBlockBounds(0.5625F, 0.125F, 0.125F, 1.0F, 0.875F, 0.875F);
        break;
        case 5:
            block.setBlockBounds(0.0F, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F);
        break;
        }
    }

}
