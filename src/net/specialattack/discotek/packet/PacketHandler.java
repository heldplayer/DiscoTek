
package net.specialattack.discotek.packet;

import net.specialattack.discotek.Objects;

public class PacketHandler extends me.heldplayer.util.HeldCore.packet.PacketHandler {

    public static PacketHandler instance;

    public PacketHandler() {
        super(Objects.MOD_CHANNEL);
        instance = this;
    }

}
