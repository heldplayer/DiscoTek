
package net.specialattack.modjam;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.specialattack.modjam.block.BlockLight;
import net.specialattack.modjam.block.TileEntityLight;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION)
public class ModjamMod {

    @Instance(Constants.MOD_ID)
    public ModjamMod instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();

        Configuration config = new Configuration(configFile);
        Config.loadConfig(config);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Objects.blockLight = new BlockLight(Config.blockLightId);
        Objects.blockLight.setCreativeTab(CreativeTabs.tabRedstone);
        GameRegistry.registerBlock(Objects.blockLight, "ModJam2013.blockMulti1");

        TileEntity.addMapping(TileEntityLight.class, "ModJam2013.Light");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

}
