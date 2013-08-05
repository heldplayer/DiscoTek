
package net.specialattack.discotek;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class Config {

    public static int blockLightId = 2070;
    public static int blockTrussId = 2071;
    public static int blockDecorationId = 2072;
    public static int blockControllerId = 2073;
    public static int blockColoredLampOnId = 2074;
    public static int blockColoredLampOffId = 2075;
    public static int itemDebugId = 5070;
    public static int itemLensId = 5071;
    public static int itemWirelessLinkerId = 5072;
    public static int itemOrienterId = 5073;
    public static int itemCraftingId = 5074;

    public static void loadConfig(Configuration config) {
        config.load();

        boolean modified = false;

        Property property = config.getBlock("Blocklight", blockLightId);
        blockLightId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getBlock("BlockTruss", blockTrussId);
        blockTrussId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getBlock("BlockDecoration", blockDecorationId);
        blockDecorationId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getBlock("BlockController", blockControllerId);
        blockControllerId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getBlock("BlockColoredLampOn", blockColoredLampOnId);
        blockColoredLampOnId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getBlock("BlockColoredLampOff", blockColoredLampOffId);
        blockColoredLampOffId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getItem("ItemDebug", itemDebugId);
        itemDebugId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getItem("ItemLens", itemLensId);
        itemLensId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getItem("ItemWirelessLinker", itemWirelessLinkerId);
        itemWirelessLinkerId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getItem("ItemOrienter", itemOrienterId);
        itemOrienterId = property.getInt();
        modified = modified || property.hasChanged();

        property = config.getItem("ItemCrafting", itemCraftingId);
        itemCraftingId = property.getInt();
        modified = modified || property.hasChanged();

        if (modified) {
            config.save();
        }
    }

}
