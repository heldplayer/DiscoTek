
package net.specialattack.discotek.lights;

import java.util.HashMap;

import net.minecraft.util.StatCollector;

public final class Channels {

    private static HashMap<Integer, Channels> idMap = new HashMap<Integer, Channels>();
    private static HashMap<String, Channels> identifierMap = new HashMap<String, Channels>();

    public static Channels BRIGHTNESS = new Channels(0, "brightness");
    public static Channels TILT = new Channels(1, "tilt");
    public static Channels PAN = new Channels(2, "pan");
    public static Channels FOCUS = new Channels(3, "focus");
    public static Channels RED = new Channels(4, "red");
    public static Channels GREEN = new Channels(5, "green");
    public static Channels BLUE = new Channels(6, "blue");
    public static Channels STRENGTH = new Channels(7, "strength");

    public final String identifier;
    public final int id;

    private Channels(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;

        idMap.put(Integer.valueOf(id), this);
        identifierMap.put(identifier, this);
    }

    public String translateValue(int value) {
        return StatCollector.translateToLocalFormatted(this.identifier, value);
    }

    public static Channels getChannel(int id) {
        return idMap.get(Integer.valueOf(id));
    }

    public static Channels getChannel(String identifier) {
        return identifierMap.get(identifier);
    }

}
