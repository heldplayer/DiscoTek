
package net.specialattack.discotek.lights;

import java.util.HashMap;

public final class Channels {

    private static HashMap<Integer, Channels> idMap = new HashMap<Integer, Channels>();
    private static HashMap<String, Channels> identifierMap = new HashMap<String, Channels>();

    public static Channels BRIGHTNESS = new Channels(0, "gui.light.brightness");
    public static Channels TILT = new Channels(1, "gui.light.tilt");
    public static Channels PAN = new Channels(2, "gui.light.pan");
    public static Channels FOCUS = new Channels(3, "gui.light.focus");
    public static Channels RED = new Channels(4, "gui.light.red");
    public static Channels GREEN = new Channels(5, "gui.light.green");
    public static Channels BLUE = new Channels(6, "gui.light.blue");
    public static Channels STRENGTH = new Channels(7, "gui.light.strength");

    public final String identifier;
    public final int id;

    private Channels(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;

        idMap.put(Integer.valueOf(id), this);
        identifierMap.put(identifier, this);
    }

    public static Channels getChannel(int id) {
        return idMap.get(Integer.valueOf(id));
    }

    public static Channels getChannel(String identifier) {
        return identifierMap.get(Integer.valueOf(identifier));
    }

}
