package net.specialattack.forge.discotek.light;

import java.util.HashMap;
import net.minecraft.util.StatCollector;

public final class Channels {

    private static HashMap<Integer, Channels> idMap = new HashMap<Integer, Channels>();
    private static HashMap<String, Channels> identifierMap = new HashMap<String, Channels>();

    public static Channels BRIGHTNESS = new Channels(0, "brightness");
    public static Channels PITCH = new Channels(1, "pitch", -0.8F, 0.8F);
    public static Channels ROTATION = new Channels(2, "rotation", 0.0F, (float) Math.PI * 4.0F);
    public static Channels FOCUS = new Channels(3, "focus", 0.0F, 20.0F);
    public static Channels RED = new Channels(4, "red");
    public static Channels GREEN = new Channels(5, "green");
    public static Channels BLUE = new Channels(6, "blue");
    public static Channels REDSTONE = new Channels(7, "redstone", 0.0F, 0.15F);
    public static Channels NAME = new Channels(8, "name", 1);
    public static Channels SIZE = new Channels(9, "size", 0.0F, 20.0F);
    public static Channels LENGTH = new Channels(10, "length", 0.0F, 20.0F);
    public static Channels HEAD_ROTATION = new Channels(11, "headRotation", -72.0F, 72.0F);
    public static Channels BEAT = new Channels(8, "beat", 2);
    public final String identifier;
    public final int id;
    public final int type;
    public final float min;
    public final float max;

    private Channels(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
        this.type = 0;
        this.min = 0.0F;
        this.max = 1.0F;

        Channels.idMap.put(id, this);
        Channels.identifierMap.put(identifier, this);
    }

    private Channels(int id, String identifier, float min, float max) {
        this.id = id;
        this.identifier = identifier;
        this.type = 0;
        this.min = min;
        this.max = max;

        Channels.idMap.put(id, this);
        Channels.identifierMap.put(identifier, this);
    }

    private Channels(int id, String identifier, int type) {
        this.id = id;
        this.identifier = identifier;
        this.type = type;
        this.min = 0.0F;
        this.max = 1.0F;

        Channels.idMap.put(id, this);
        Channels.identifierMap.put(identifier, this);
    }

    public static Channels getChannel(int id) {
        return Channels.idMap.get(Integer.valueOf(id));
    }

    public static Channels getChannel(String identifier) {
        return Channels.identifierMap.get(identifier);
    }

    public String translateValue(int value) {
        return StatCollector.translateToLocalFormatted(this.identifier, value);
    }

}
