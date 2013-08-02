
package net.specialattack.modjam;

import java.io.File;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.specialattack.modjam.block.BlockLight;
import net.specialattack.modjam.block.BlockTruss;
import net.specialattack.modjam.block.TileEntityLight;
import net.specialattack.modjam.creativetabs.CreativeTabIcon;
import net.specialattack.modjam.item.ItemBlockLight;
import net.specialattack.modjam.item.ItemDebug;
import net.specialattack.modjam.item.ItemLens;
import net.specialattack.modjam.item.crafting.RecipesLens;
import net.specialattack.modjam.item.crafting.RecipesLight;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { Constants.MOD_CHANNEL }, packetHandler = PacketHandler.class)
public class ModjamMod {

    @Instance(Constants.MOD_ID)
    public ModjamMod instance;

    @SidedProxy(serverSide = Constants.COMMON_PROXY, clientSide = Constants.CLIENT_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();

        Configuration config = new Configuration(configFile);
        Config.loadConfig(config);

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Objects.creativeTab = new CreativeTabIcon("modjam2013");

        Objects.blockLight = new BlockLight(Config.blockLightId);
        Objects.blockLight.setCreativeTab(Objects.creativeTab).func_111022_d("modjam:truss").setUnlocalizedName("light");
        GameRegistry.registerBlock(Objects.blockLight, ItemBlockLight.class, "ModJam2013.blockLight");

        Objects.blockTruss = new BlockTruss(Config.blockTrussId);
        Objects.blockTruss.setCreativeTab(Objects.creativeTab).func_111022_d("modjam:truss").setUnlocalizedName("truss");
        GameRegistry.registerBlock(Objects.blockTruss, "ModJam2013.blockTruss");

        Objects.itemDebug = new ItemDebug(Config.itemDebugId);
        Objects.itemDebug.setCreativeTab(Objects.creativeTab).func_111206_d("modjam:debug").setUnlocalizedName("debug").setFull3D();
        GameRegistry.registerItem(Objects.itemDebug, "ModJam2013.itemDebug");

        Objects.itemLens = new ItemLens(Config.itemLensId);
        Objects.itemLens.setCreativeTab(Objects.creativeTab).func_111206_d("modjam:lens").setUnlocalizedName("lens");
        GameRegistry.registerItem(Objects.itemDebug, "ModJam2013.itemLens");

        Objects.creativeTab.setIconItemStack(new ItemStack(Objects.blockLight));

        TileEntity.addMapping(TileEntityLight.class, "ModJam2013.Light");

        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        GameRegistry.addRecipe(new RecipesLens());
        ItemStack iron = new ItemStack(Item.ingotIron.itemID, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack lens = new ItemStack(Objects.itemLens);
        GameRegistry.addRecipe(new RecipesLight(new ItemStack(Objects.blockLight), 4, new ItemStack[] { iron, iron, iron, iron, lens, iron, null, iron, null }));
    }

}
