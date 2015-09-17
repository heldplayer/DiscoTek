package net.specialattack.forge.discotek;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.specialattack.forge.core.SpACoreProxy;
import net.specialattack.forge.core.crafting.ICraftingResultHandler;
import net.specialattack.forge.discotek.block.BlockColoredLamp;
import net.specialattack.forge.discotek.block.BlockDecoration;
import net.specialattack.forge.discotek.block.BlockTruss;
import net.specialattack.forge.discotek.creativetabs.CreativeTabIcon;
import net.specialattack.forge.discotek.item.*;
import net.specialattack.forge.discotek.item.crafting.LightCraftingHandler;
import net.specialattack.forge.discotek.item.crafting.RecipesColoredLamp;
import net.specialattack.forge.discotek.item.crafting.RecipesLens;
import net.specialattack.forge.discotek.tileentity.TileEntityColoredLamp;

public class CommonProxy extends SpACoreProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        //Objects.blockLight = new BlockLight();
        //Objects.blockLight.setBlockTextureName(Assets.DOMAIN + "truss2");
        //Objects.blockLight.setLight(0, new LightFresnel());
        //Objects.blockLight.setLight(1, new LightMap());
        //Objects.blockLight.setLight(2, new LightMapLED());
        //Objects.blockLight.setLight(3, new LightDimmer());
        //Objects.blockLight.setLight(4, new LightRadialLaser());
        //Objects.blockLight.setLight(5, new LightHologram());
        //Objects.blockLight.setLight(6, new LightPositionableRadialLaser());
        //GameRegistry.registerBlock(Objects.blockLight, ItemBlockLight.class, "light");

        // TODO: create emitters
        // TODO: Display connectors: turns lamps into a display
        // TODO: Programmable animations on new screens
        // TODO: dyed "electric" glass

        Objects.blockTruss = new BlockTruss();
        Objects.blockTruss.setBlockTextureName(Assets.DOMAIN + "truss");
        GameRegistry.registerBlock(Objects.blockTruss, ItemBlockMulti.class, "truss");

        Objects.blockDecoration = new BlockDecoration();
        GameRegistry.registerBlock(Objects.blockDecoration, ItemBlockMulti.class, "decoration");

        //Objects.blockController = new BlockController();
        //GameRegistry.registerBlock(Objects.blockController, ItemBlockController.class, "controller");
        //Objects.blockController.setController(0, new ControllerPixel());
        //Objects.blockController.setController(1, new ControllerGrandSpa());

        Objects.blockColoredLamp = new BlockColoredLamp();
        GameRegistry.registerBlock(Objects.blockColoredLamp, ItemBlockColoredLamp.class, "colored_lamp");

        // Objects.blockColoredLampOn = new BlockColoredLamp(true);
        // GameRegistry.registerBlock(Objects.blockColoredLampOn, "colored_lamp_on");

        Objects.itemDebug = new ItemDebug();
        Objects.itemDebug.setTextureName(Assets.DOMAIN + "debug");
        GameRegistry.registerItem(Objects.itemDebug, "debug");

        Objects.itemLens = new ItemLens();
        Objects.itemLens.setTextureName(Assets.DOMAIN + "lens");
        GameRegistry.registerItem(Objects.itemLens, "lens");

        Objects.itemWirelessLinker = new ItemWirelessLinker();
        Objects.itemWirelessLinker.setTextureName(Assets.DOMAIN + "wirelessLinker");
        GameRegistry.registerItem(Objects.itemWirelessLinker, "wireless_linker");

        Objects.itemOrienter = new ItemOrienter();
        GameRegistry.registerItem(Objects.itemOrienter, "orienter");

        Objects.itemCrafting = new ItemCrafting();
        GameRegistry.registerItem(Objects.itemCrafting, "crafting");

        Objects.itemColorConfigurator = new ItemColorConfigurator();
        Objects.itemColorConfigurator.setTextureName(Assets.DOMAIN + "color-configurator");
        GameRegistry.registerItem(Objects.itemColorConfigurator, "color-configurator");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        Objects.creativeTab = new CreativeTabIcon("discotek");

        //Objects.blockLight.setCreativeTab(Objects.creativeTab).setHardness(2.0F).setResistance(10.0F).setBlockName("light");

        Objects.blockTruss.setCreativeTab(Objects.creativeTab).setHardness(2.0F).setResistance(10.0F).setBlockName("truss");

        Objects.blockDecoration.setCreativeTab(Objects.creativeTab).setHardness(2.0F).setResistance(10.0F).setBlockName("decoration");

        //Objects.blockController.setCreativeTab(Objects.creativeTab).setHardness(2.0F).setResistance(10.0F).setBlockName("controller");

        Objects.blockColoredLamp.setCreativeTab(Objects.creativeTab).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("lamp");

        // Objects.blockColoredLampOn.setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("lamp");

        Objects.itemDebug.setCreativeTab(Objects.creativeTab).setUnlocalizedName("debug");

        Objects.itemLens.setCreativeTab(Objects.creativeTab).setUnlocalizedName("lens");

        Objects.itemWirelessLinker.setCreativeTab(Objects.creativeTab).setUnlocalizedName("wirelesslinker");

        Objects.itemOrienter.setCreativeTab(Objects.creativeTab).setUnlocalizedName("orienter");

        Objects.itemCrafting.setCreativeTab(Objects.creativeTab).setUnlocalizedName("crafting");

        Objects.itemColorConfigurator.setCreativeTab(Objects.creativeTab).setUnlocalizedName("colorConfigurator");

        Objects.creativeTab.setIconItemStack(new ItemStack(Objects.blockTruss));

        //TileEntity.addMapping(TileEntityLight.class, "ModJam2013.Light"); // Compat
        //TileEntity.addMapping(TileEntityController.class, "ModJam2013.Controller"); // Compat
        //TileEntity.addMapping(TileEntityLight.class, "DiscoTek.Light");
        //TileEntity.addMapping(TileEntityController.class, "DiscoTek.Controller");
        TileEntity.addMapping(TileEntityColoredLamp.class, "DiscoTek.ColoredLamp");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        ItemStack hull = new ItemStack(Objects.blockDecoration, 1, 0);
        ItemStack lens = new ItemStack(Objects.itemLens, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack glass = new ItemStack(Blocks.glass, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack glassPane = new ItemStack(Blocks.glass_pane, 1, 0);
        ItemStack bulb = new ItemStack(Objects.itemCrafting, 1, 0);
        ItemStack led = new ItemStack(Objects.itemCrafting, 1, 1);
        ItemStack servo = new ItemStack(Objects.itemCrafting, 1, 2);
        ItemStack iron = new ItemStack(Items.iron_ingot, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack redstone = new ItemStack(Items.redstone, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack quartz = new ItemStack(Items.quartz, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack glassBottle = new ItemStack(Items.glass_bottle, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack glowstone = new ItemStack(Items.glowstone_dust, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack piston = new ItemStack(Blocks.piston, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack stick = new ItemStack(Items.stick, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack brick = new ItemStack(Items.brick, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack netherBrick = new ItemStack(Items.netherbrick, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack darkBrick = new ItemStack(Objects.itemCrafting, 1, 3);
        ItemStack darkNetherBrick = new ItemStack(Objects.itemCrafting, 1, 4);
        ItemStack stoneBricks = new ItemStack(Blocks.stonebrick, 1, 0);
        ItemStack chiseledStoneBricks = new ItemStack(Blocks.stonebrick, 1, 3);
        ItemStack chiseledQuartz = new ItemStack(Blocks.quartz_block, 1, 1);
        ItemStack brickBlock = new ItemStack(Blocks.brick_block, 1, 1);
        ItemStack netherBrickBlock = new ItemStack(Blocks.nether_brick, 1, 1);

        // Basic Controller
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockController, 1, 0), "sss", "dqd", "HrH", 's', stick, 'd', "dyeBlue", 'q', quartz, 'H', hull, 'r', redstone));
        // Advanced Controller
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockController, 1, 1), "lll", "dqd", "HrH", 'l', led, 'd', "dyeRed", 'q', quartz, 'H', hull, 'r', redstone));
        // Console
        // XXX: Not craftable yet, WIP
        // Wireless Linker
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.itemWirelessLinker, 1, 0), " qs", "idq", " i ", 'q', quartz, 's', stick, 'i', iron, 'd', "dyeBlue"));
        // Darker Truss
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockTruss, 8, 0), "TTT", "TdT", "TTT", 'T', new ItemStack(Objects.blockTruss, 1, 1), 'd', "dyeBlack"));
        // Silver truss
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockTruss, 8, 1), "isi", "s s", "isi", 'i', iron, 's', stick);
        // Darkest Truss
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockTruss, 8, 2), "TTT", "TdT", "TTT", 'T', new ItemStack(Objects.blockTruss, 1, 0), 'd', "dyeBlack"));
        // Orienter
        GameRegistry.addShapedRecipe(new ItemStack(Objects.itemOrienter, 1, 0), "s", "S", 's', servo, 'S', stick);
        // Lighting Hull
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockDecoration, 4, 0), " i ", "idi", " i ", 'i', iron, 'd', "dyeBlack"));
        // Dark Stone Bricks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockDecoration, 8, 1), "BBB", "BdB", "BBB", 'B', stoneBricks, 'd', "dyeBlack"));
        // Dark Chiseled Stone Bricks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockDecoration, 8, 2), "BBB", "BdB", "BBB", 'B', chiseledStoneBricks, 'd', "dyeBlack"));
        // Dark Chiseled Quartz Block
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockDecoration, 8, 3), "QQQ", "QdQ", "QQQ", 'Q', chiseledQuartz, 'd', "dyeBlack"));
        // Dark Bricks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockDecoration, 8, 4), "BBB", "BdB", "BBB", 'B', brickBlock, 'd', "dyeBlack"));
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockDecoration, 1, 4), "bb", "bb", 'b', darkBrick);
        // Dark Nether Bricks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockDecoration, 8, 5), "BBB", "BdB", "BBB", 'B', netherBrickBlock, 'd', "dyeBlack"));
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockDecoration, 1, 5), "bb", "bb", 'b', darkNetherBrick);
        // Dark Wood Planks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockDecoration, 8, 6), "PPP", "PdP", "PPP", 'P', "plankWood", 'd', "dyeBlack"));
        // Darkness
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockDecoration, 8, 7), "dd", "dd", 'd', "dyeBlack"));
        // Lens
        GameRegistry.addShapedRecipe(new ItemStack(Objects.itemLens, 1, 0), " i ", "iPi", " i ", 'i', iron, 'P', glassPane);
        // Light Bulb
        GameRegistry.addShapedRecipe(new ItemStack(Objects.itemCrafting, 1, 0), "i", "r", "b", 'i', iron, 'r', redstone, 'b', glassBottle);
        // Led
        GameRegistry.addShapedRecipe(new ItemStack(Objects.itemCrafting, 1, 1), "r", "g", "b", 'r', redstone, 'g', glowstone, 'b', glassBottle);
        // Servo
        GameRegistry.addShapedRecipe(new ItemStack(Objects.itemCrafting, 1, 2), " HH", "sPr", " HH", 'H', hull, 's', stick, 'P', piston, 'r', redstone);
        // Dark brick
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.itemCrafting, 8, 3), "bbb", "bdb", "bbb", 'b', brick, 'd', "dyeBlack"));
        // Dark nether brick
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.itemCrafting, 8, 4), "bbb", "bdb", "bbb", 'b', netherBrick, 'd', "dyeBlack"));
        // Lens colouring
        GameRegistry.addRecipe(new RecipesLens());
        // Light Crafting Handler
        ICraftingResultHandler handler = new LightCraftingHandler();
        // Fresnel
        //GameRegistry.addRecipe(new ShapedSpACoreRecipe(handler, new ItemStack(Objects.blockLight, 1, 0), "HHH", "HbH", " l ", 'H', hull, 'b', bulb, 'l', lens));
        // SpA 250
        //GameRegistry.addRecipe(new ShapedSpACoreRecipe(handler, new ItemStack(Objects.blockLight, 1, 1), " l ", "sbs", "HsH", 'l', lens, 's', servo, 'b', bulb, 'H', hull));
        // SpA 250 LED
        //GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 2), " G ", "sls", "HsH", 'G', glass, 's', servo, 'l', led, 'H', hull);
        // DMX To Redstone Converter
        //GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 3), "HrH", "rqr", "HrH", 'H', hull, 'r', redstone, 'q', quartz);
        // Radial Laser Emitter
        //GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 4), "HlH", "lsl", "HlH", 'H', hull, 'l', led, 's', servo);
        // Hologram Emitter
        //GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 5), "HlH", "qGq", "HlH", 'H', hull, 'l', led, 'q', quartz, 'G', glass);
        //GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 5), "HqH", "lGl", "HqH", 'H', hull, 'l', led, 'q', quartz, 'G', glass);
        // Lamp colouring
        GameRegistry.addRecipe(new RecipesColoredLamp());
    }

}
