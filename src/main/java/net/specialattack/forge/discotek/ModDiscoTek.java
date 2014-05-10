
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
import net.specialattack.forge.discotek.packet.Packet7PixelButton;
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
    // public static ConfigValue<Integer> blockLightId;

    @Override
    @SuppressWarnings("unchecked")
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();

        ModDiscoTek.packetHandler = new PacketHandler("DiscoTek", Packet1LightPort.class, Packet2LightGui.class, Packet3PixelSlider.class, Packet4PixelGui.class, Packet5GrandSpAInstruction.class, Packet6GrandSpAGui.class, Packet7PixelButton.class);

        // Config
        // blockLightId = new ConfigValue<Integer>("blockLightId", Configuration.CATEGORY_BLOCK, null, 2080, "");

        this.config = new Config(event.getSuggestedConfigurationFile());

        // this.config.addConfigKey(blockLightId);

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
