package net.specialattack.forge.discotek.packet;

import net.minecraft.world.World;
import net.specialattack.forge.core.packet.SpACorePacket;

public abstract class DiscoTekPacket extends SpACorePacket {

    public DiscoTekPacket(World world) {
        super(world);
    }

}
