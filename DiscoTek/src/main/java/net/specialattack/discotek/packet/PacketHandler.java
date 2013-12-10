
package net.specialattack.discotek.packet;

import net.specialattack.discotek.Objects;

public class PacketHandler extends me.heldplayer.util.HeldCore.packet.PacketHandler {

    public static PacketHandler instance;

    public PacketHandler() {
        super(Objects.MOD_CHANNEL);
        this.registerPacket(1, Packet1LightPort.class);
        this.registerPacket(2, Packet2LightGui.class);
        this.registerPacket(3, Packet3PixelSlider.class);
        this.registerPacket(4, Packet4PixelGui.class);
        this.registerPacket(5, Packet5GrandSpAInstruction.class);
        this.registerPacket(6, Packet6GrandSpAGui.class);
        instance = this;
    }

}
