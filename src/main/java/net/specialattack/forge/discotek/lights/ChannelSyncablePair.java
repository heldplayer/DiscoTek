
package net.specialattack.forge.discotek.lights;

import net.specialattack.forge.core.sync.BaseSyncable;

public class ChannelSyncablePair {

    public final String name;
    public final Channels channel;
    public final BaseSyncable syncable;
    public Object prevValue;

    public ChannelSyncablePair(String name, Channels channel, BaseSyncable syncable) {
        this.name = name;
        this.channel = channel;
        this.syncable = syncable;
    }

}
