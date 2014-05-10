
package net.specialattack.forge.discotek;

import net.specialattack.forge.core.ModInfo;
import net.specialattack.forge.discotek.block.BlockColoredLamp;
import net.specialattack.forge.discotek.block.BlockController;
import net.specialattack.forge.discotek.block.BlockDecoration;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.block.BlockTruss;
import net.specialattack.forge.discotek.creativetabs.CreativeTabIcon;
import net.specialattack.forge.discotek.item.ItemCrafting;
import net.specialattack.forge.discotek.item.ItemDebug;
import net.specialattack.forge.discotek.item.ItemLens;
import net.specialattack.forge.discotek.item.ItemOrienter;
import net.specialattack.forge.discotek.item.ItemWirelessLinker;

import org.apache.logging.log4j.Logger;

public class Objects {

    public static final String MOD_ID = "discotek";
    public static final String MOD_NAME = "DiscoTek";
    public static final String MOD_DEPENCIES = "after:SpACore";

    public static final String COMMON_PROXY = "net.specialattack.forge.discotek.CommonProxy";
    public static final String CLIENT_PROXY = "net.specialattack.forge.discotek.client.ClientProxy";

    public static final ModInfo MOD_INFO = new ModInfo(Objects.MOD_ID, Objects.MOD_NAME);

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
