
package net.specialattack.modjam;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class Config {

    public static int blockMulti1Id;

    public static void loadConfig(Configuration config) {
        config.load();

        boolean modified = false;

        Property property = config.getBlock("BlockMulti1", 2070);
        blockMulti1Id = property.getInt();
        modified = modified || property.hasChanged();

        config.save();
    }

}
