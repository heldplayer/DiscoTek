
package net.specialattack.forge.discotek.lights;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import net.minecraft.util.StatCollector;
import net.specialattack.forge.core.sync.BaseSyncable;
import net.specialattack.forge.core.sync.ISyncableObjectOwner;
import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.core.sync.SInteger;

public final class Channels {

    private static HashMap<Integer, Channels> idMap = new HashMap<Integer, Channels>();
    private static HashMap<String, Channels> identifierMap = new HashMap<String, Channels>();

    public static Channels BRIGHTNESS = new Channels(0, "brightness", SFloat.class);
    public static Channels TILT = new Channels(1, "tilt", SFloat.class);
    public static Channels PAN = new Channels(2, "pan", SFloat.class);
    public static Channels FOCUS = new Channels(3, "focus", SFloat.class);
    public static Channels RED = new Channels(4, "red", SInteger.class);
    public static Channels GREEN = new Channels(5, "green", SInteger.class);
    public static Channels BLUE = new Channels(6, "blue", SInteger.class);
    public static Channels STRENGTH = new Channels(7, "strength", SFloat.class);

    public final String identifier;
    public final int id;
    public final Class<? extends BaseSyncable> typeClass;
    private final Constructor<? extends BaseSyncable> constructor;

    private Channels(int id, String identifier, Class<? extends BaseSyncable> clazz) {
        this.id = id;
        this.identifier = identifier;
        this.typeClass = clazz;
        try {
            this.constructor = clazz.getConstructor(ISyncableObjectOwner.class);
        }
        catch (Throwable e) {
            throw new IllegalArgumentException("clazz", e);
        }

        Channels.idMap.put(Integer.valueOf(id), this);
        Channels.identifierMap.put(identifier, this);
    }

    public String translateValue(int value) {
        return StatCollector.translateToLocalFormatted(this.identifier, value);
    }

    public static Channels getChannel(int id) {
        return Channels.idMap.get(Integer.valueOf(id));
    }

    public static Channels getChannel(String identifier) {
        return Channels.identifierMap.get(identifier);
    }

    public BaseSyncable createSyncable(ISyncableObjectOwner owner) {
        try {
            return this.constructor.newInstance(owner);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
