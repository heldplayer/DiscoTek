
package net.specialattack.modjam;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class Config {

    public static int blockLightId = 2070;
    public static int blockTrussId = 2071;
    public static int itemDebugId = 5070;
    public static int itemLensId = 5071;

    public static void loadConfig(Configuration config) {
        config.load();

        boolean modified = false;

        Property property = config.getBlock("Blocklight", blockLightId);
        blockLightId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getBlock("BlockTruss", blockTrussId);
        blockTrussId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getItem("ItemDebug", itemDebugId);
        itemDebugId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getItem("ItemLens", itemLensId);
        itemLensId = property.getInt();
        modified = modified || property.hasChanged();

        if (modified) {
            config.save();
        }
    }

}
