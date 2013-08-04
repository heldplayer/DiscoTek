
package net.specialattack.modjam;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.specialattack.modjam.block.BlockController;
import net.specialattack.modjam.block.BlockDecoration;
import net.specialattack.modjam.block.BlockLight;
import net.specialattack.modjam.block.BlockTruss;
import net.specialattack.modjam.creativetabs.CreativeTabIcon;
import net.specialattack.modjam.gui.GuiHandler;
import net.specialattack.modjam.item.ItemBlockController;
import net.specialattack.modjam.item.ItemBlockLight;
import net.specialattack.modjam.item.ItemCrafting;
import net.specialattack.modjam.item.ItemDebug;
import net.specialattack.modjam.item.ItemLens;
import net.specialattack.modjam.item.ItemOrienter;
import net.specialattack.modjam.item.ItemWirelessLinker;
import net.specialattack.modjam.item.crafting.RecipesLens;
import net.specialattack.modjam.item.crafting.RecipesLight;
import net.specialattack.modjam.tileentity.TileEntityController;
import net.specialattack.modjam.tileentity.TileEntityLight;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
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

        // FIXME: decoration block needs good dark textures
        Objects.blockDecoration = new BlockDecoration(Config.blockDecorationId);
        Objects.blockDecoration.setCreativeTab(Objects.creativeTab).setUnlocalizedName("decoration");
        GameRegistry.registerBlock(Objects.blockDecoration, "ModJam2013.blockDecoration");

        Objects.blockController = new BlockController(Config.blockControllerId);
        Objects.blockController.setCreativeTab(Objects.creativeTab).setUnlocalizedName("controller");
        GameRegistry.registerBlock(Objects.blockController, ItemBlockController.class, "ModJam2013.blockController");

        Objects.itemDebug = new ItemDebug(Config.itemDebugId);
        Objects.itemDebug.setCreativeTab(Objects.creativeTab).func_111206_d("modjam:debug").setUnlocalizedName("debug");
        GameRegistry.registerItem(Objects.itemDebug, "ModJam2013.itemDebug");

        Objects.itemLens = new ItemLens(Config.itemLensId);
        Objects.itemLens.setCreativeTab(Objects.creativeTab).func_111206_d("modjam:lens").setUnlocalizedName("lens");
        GameRegistry.registerItem(Objects.itemDebug, "ModJam2013.itemLens");

        Objects.itemWirelessLinker = new ItemWirelessLinker(Config.itemWirelessLinkerId);
        Objects.itemWirelessLinker.setCreativeTab(Objects.creativeTab).func_111206_d("modjam:wirelessLinker").setUnlocalizedName("wirelesslinker");
        GameRegistry.registerItem(Objects.itemWirelessLinker, "ModJam2013.itemWirelessLinker");

        Objects.itemOrienter = new ItemOrienter(Config.itemOrienterId);
        Objects.itemOrienter.setCreativeTab(Objects.creativeTab).setUnlocalizedName("orienter");
        GameRegistry.registerItem(Objects.itemOrienter, "ModJam2013.itemOrienter");

        Objects.itemCrafting = new ItemCrafting(Config.itemCraftingId);
        Objects.itemCrafting.setCreativeTab(Objects.creativeTab).setUnlocalizedName("crafting");
        GameRegistry.registerItem(Objects.itemCrafting, "ModJam2013.itemCrafting");

        Objects.creativeTab.setIconItemStack(new ItemStack(Objects.blockLight));

        TileEntity.addMapping(TileEntityLight.class, "ModJam2013.Light");
        TileEntity.addMapping(TileEntityController.class, "ModJam2013.Controller");

        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());

        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        ItemStack hull = new ItemStack(Objects.blockDecoration, 1, 0);
        ItemStack lens = new ItemStack(Objects.itemLens, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack glass = new ItemStack(Block.glass, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack glassPane = new ItemStack(Block.thinGlass, 1, 0);
        ItemStack bulb = new ItemStack(Objects.itemCrafting, 1, 0);
        ItemStack led = new ItemStack(Objects.itemCrafting, 1, 1);
        ItemStack servo = new ItemStack(Objects.itemCrafting, 1, 2);
        ItemStack iron = new ItemStack(Item.ingotIron, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack redstone = new ItemStack(Item.redstone, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack quartz = new ItemStack(Item.netherQuartz, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack glassBottle = new ItemStack(Item.glassBottle, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack glowstone = new ItemStack(Item.glowstone, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack dyeBlack = new ItemStack(Item.dyePowder, 1, 0);

        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockDecoration, 4, 0), " i ", "idi", " i ", 'i', iron, 'd', dyeBlack);
        GameRegistry.addShapedRecipe(new ItemStack(Objects.itemLens, 1, 0), " i ", "iPi", " i ", 'i', iron, 'P', glassPane);
        GameRegistry.addShapedRecipe(new ItemStack(Objects.itemCrafting, 1, 0), "i", "r", "b", 'i', iron, 'r', redstone, 'b', glassBottle);
        GameRegistry.addShapedRecipe(new ItemStack(Objects.itemCrafting, 1, 1), "r", "g", "b", 'r', redstone, 'g', glowstone, 'b', glassBottle);
        // TODO: Servo

        GameRegistry.addRecipe(new RecipesLens());

        // Fresnel
        GameRegistry.addRecipe(new RecipesLight(new ItemStack(Objects.blockLight, 1, 0), 7, new ItemStack[] { hull, hull, hull, hull, bulb, hull, null, lens, null }));
        // SpA 250
        GameRegistry.addRecipe(new RecipesLight(new ItemStack(Objects.blockLight, 1, 1), 1, new ItemStack[] { null, lens, null, servo, bulb, servo, hull, servo, hull }));
        // SpA 250 LED
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 2), " G ", "sls", "HsH", 'G', glass, 's', servo, 'l', led, 'H', hull);
        // DMX To Redstone Converter
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 3), "HrH", "rqr", "HrH", 'H', hull, 'r', redstone, 'q', quartz);
        // Radial Laser Emitter
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 4), "HlH", "lsl", "HlH", 'H', hull, 'l', led, 's', servo);
    }

}
