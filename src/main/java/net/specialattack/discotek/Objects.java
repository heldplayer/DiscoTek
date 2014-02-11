
package net.specialattack.discotek;

import java.io.InputStream;
import java.util.Properties;

import me.heldplayer.util.HeldCore.ModInfo;
import net.specialattack.discotek.block.BlockColoredLamp;
import net.specialattack.discotek.block.BlockController;
import net.specialattack.discotek.block.BlockDecoration;
import net.specialattack.discotek.block.BlockLight;
import net.specialattack.discotek.block.BlockTruss;
import net.specialattack.discotek.creativetabs.CreativeTabIcon;
import net.specialattack.discotek.item.ItemCrafting;
import net.specialattack.discotek.item.ItemDebug;
import net.specialattack.discotek.item.ItemLens;
import net.specialattack.discotek.item.ItemOrienter;
import net.specialattack.discotek.item.ItemWirelessLinker;

import org.apache.logging.log4j.Logger;

public class Objects {

    public static final String MOD_VERSION;

    static {
        Properties prop = new Properties();

        String version = "";

        try {
            InputStream stream = Objects.class.getClassLoader().getResourceAsStream("version.properties");
            prop.load(stream);
            stream.close();
            version = prop.getProperty("version");
        }
        catch (Exception e) {
            e.printStackTrace();
            version = "Error";
        }
        finally {
            MOD_VERSION = version;
        }
    }

    public static final String MOD_ID = "discotek";
    public static final String MOD_NAME = "DiscoTek";
    public static final String MOD_DEPENCIES = "after:HeldCore";

    public static final String COMMON_PROXY = "net.specialattack.discotek.CommonProxy";
    public static final String CLIENT_PROXY = "net.specialattack.discotek.client.ClientProxy";

    public static final ModInfo MOD_INFO = new ModInfo(MOD_ID, MOD_NAME, MOD_VERSION);

    public static Logger log;

    public static BlockLight blockLight;
    public static BlockTruss blockTruss;
    public static BlockDecoration blockDecoration;
    public static BlockController blockController;
    public static BlockColoredLamp blockColoredLampOn;
    public static BlockColoredLamp blockColoredLampOff;

    public static ItemDebug itemDebug;
    public static ItemLens itemLens;
    public static ItemWirelessLinker itemWirelessLinker;
    public static ItemOrienter itemOrienter;
    public static ItemCrafting itemCrafting;

    public static CreativeTabIcon creativeTab;

}
