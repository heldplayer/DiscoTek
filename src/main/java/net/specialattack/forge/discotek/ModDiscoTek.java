
package net.specialattack.forge.discotek;

import net.specialattack.forge.core.ModInfo;
import net.specialattack.forge.core.SpACoreMod;
import net.specialattack.forge.core.SpACoreProxy;
import net.specialattack.forge.core.config.Config;
import net.specialattack.forge.core.packet.PacketHandler;
import net.specialattack.forge.discotek.packet.Packet1LightPort;
import net.specialattack.forge.discotek.packet.Packet2LightGui;
import net.specialattack.forge.discotek.packet.Packet3PixelSlider;
import net.specialattack.forge.discotek.packet.Packet4PixelGui;
import net.specialattack.forge.discotek.packet.Packet5GrandSpAInstruction;
import net.specialattack.forge.discotek.packet.Packet6GrandSpAGui;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Objects.MOD_ID, name = Objects.MOD_NAME, dependencies = Objects.MOD_DEPENCIES)
public class ModDiscoTek extends SpACoreMod {

    @Instance(Objects.MOD_ID)
    public static ModDiscoTek instance;
    @SidedProxy(serverSide = Objects.COMMON_PROXY, clientSide = Objects.CLIENT_PROXY)
    public static CommonProxy proxy;
    public static PacketHandler packetHandler;

    // SpACore Objects
    //    public static ConfigValue<Integer> blockLightId;
    //    public static ConfigValue<Integer> blockTrussId;
    //    public static ConfigValue<Integer> blockDecorationId;
    //    public static ConfigValue<Integer> blockControllerId;
    //    public static ConfigValue<Integer> blockColoredLampOnId;
    //    public static ConfigValue<Integer> blockColoredLampOffId;
    //    public static ConfigValue<Integer> itemDebugId;
    //    public static ConfigValue<Integer> itemLensId;
    //    public static ConfigValue<Integer> itemWirelessLinkerId;
    //    public static ConfigValue<Integer> itemOrienterId;
    //    public static ConfigValue<Integer> itemCraftingId;

    @Override
    @SuppressWarnings("unchecked")
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();
        event.getModMetadata().version = Objects.MOD_VERSION;

        ModDiscoTek.packetHandler = new PacketHandler("DiscoTek", Packet1LightPort.class, Packet2LightGui.class, Packet3PixelSlider.class, Packet4PixelGui.class, Packet5GrandSpAInstruction.class, Packet6GrandSpAGui.class);

        // Config
        //        blockLightId = new ConfigValue<Integer>("blockLightId", Configuration.CATEGORY_BLOCK, null, 2080, "");
        //        blockTrussId = new ConfigValue<Integer>("blockTrussId", Configuration.CATEGORY_BLOCK, null, 2081, "");
        //        blockDecorationId = new ConfigValue<Integer>("blockDecorationId", Configuration.CATEGORY_BLOCK, null, 2082, "");
        //        blockControllerId = new ConfigValue<Integer>("blockControllerId", Configuration.CATEGORY_BLOCK, null, 2083, "");
        //        blockColoredLampOnId = new ConfigValue<Integer>("blockColoredLampOnId", Configuration.CATEGORY_BLOCK, null, 2084, "");
        //        blockColoredLampOffId = new ConfigValue<Integer>("blockColoredLampOffId", Configuration.CATEGORY_BLOCK, null, 2085, "");
        //        itemDebugId = new ConfigValue<Integer>("itemDebugId", Configuration.CATEGORY_ITEM, null, 5070, "");
        //        itemLensId = new ConfigValue<Integer>("itemLensId", Configuration.CATEGORY_ITEM, null, 5071, "");
        //        itemWirelessLinkerId = new ConfigValue<Integer>("itemWirelessLinkerId", Configuration.CATEGORY_ITEM, null, 5072, "");
        //        itemOrienterId = new ConfigValue<Integer>("itemOrienterId", Configuration.CATEGORY_ITEM, null, 5073, "");
        //        itemCraftingId = new ConfigValue<Integer>("itemCraftingId", Configuration.CATEGORY_ITEM, null, 5074, "");

        this.config = new Config(event.getSuggestedConfigurationFile());

        //        this.config.addConfigKey(blockLightId);
        //        this.config.addConfigKey(blockTrussId);
        //        this.config.addConfigKey(blockDecorationId);
        //        this.config.addConfigKey(blockControllerId);
        //        this.config.addConfigKey(blockColoredLampOnId);
        //        this.config.addConfigKey(blockColoredLampOffId);
        //        this.config.addConfigKey(itemDebugId);
        //        this.config.addConfigKey(itemLensId);
        //        this.config.addConfigKey(itemWirelessLinkerId);
        //        this.config.addConfigKey(itemOrienterId);
        //        this.config.addConfigKey(itemCraftingId);

        super.preInit(event);
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public ModInfo getModInfo() {
        return Objects.MOD_INFO;
    }

    @Override
    public SpACoreProxy getProxy() {
        return ModDiscoTek.proxy;
    }

}