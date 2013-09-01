
package net.specialattack.discotek;

import me.heldplayer.util.HeldCore.HeldCoreProxy;
import me.heldplayer.util.HeldCore.crafting.ICraftingResultHandler;
import me.heldplayer.util.HeldCore.crafting.ShapedHeldCoreRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.specialattack.discotek.block.BlockColoredLamp;
import net.specialattack.discotek.block.BlockController;
import net.specialattack.discotek.block.BlockDecoration;
import net.specialattack.discotek.block.BlockLight;
import net.specialattack.discotek.block.BlockTruss;
import net.specialattack.discotek.creativetabs.CreativeTabIcon;
import net.specialattack.discotek.item.ItemBlockLight;
import net.specialattack.discotek.item.ItemBlockMulti;
import net.specialattack.discotek.item.ItemCrafting;
import net.specialattack.discotek.item.ItemDebug;
import net.specialattack.discotek.item.ItemLens;
import net.specialattack.discotek.item.ItemOrienter;
import net.specialattack.discotek.item.ItemWirelessLinker;
import net.specialattack.discotek.item.crafting.LightCraftingHandler;
import net.specialattack.discotek.item.crafting.RecipesLens;
import net.specialattack.discotek.tileentity.TileEntityController;
import net.specialattack.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy extends HeldCoreProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {}

    @Override
    public void init(FMLInitializationEvent event) {
        Objects.creativeTab = new CreativeTabIcon("discotek");

        Objects.blockLight = new BlockLight(ModDiscoTek.blockLightId.getValue());
        Objects.blockLight.setCreativeTab(Objects.creativeTab).setHardness(2.0F).setResistance(10.0F).func_111022_d("modjam:truss").setUnlocalizedName("light");
        GameRegistry.registerBlock(Objects.blockLight, ItemBlockLight.class, "ModJam2013.blockLight");

        Objects.blockTruss = new BlockTruss(ModDiscoTek.blockTrussId.getValue());
        Objects.blockTruss.setCreativeTab(Objects.creativeTab).setHardness(2.0F).setResistance(10.0F).func_111022_d("modjam:truss").setUnlocalizedName("truss");
        GameRegistry.registerBlock(Objects.blockTruss, ItemBlockMulti.class, "ModJam2013.blockTruss");

        Objects.blockDecoration = new BlockDecoration(ModDiscoTek.blockDecorationId.getValue());
        Objects.blockDecoration.setCreativeTab(Objects.creativeTab).setHardness(2.0F).setResistance(10.0F).setUnlocalizedName("decoration");
        GameRegistry.registerBlock(Objects.blockDecoration, ItemBlockMulti.class, "ModJam2013.blockDecoration");

        Objects.blockController = new BlockController(ModDiscoTek.blockControllerId.getValue());
        Objects.blockController.setCreativeTab(Objects.creativeTab).setHardness(2.0F).setResistance(10.0F).setUnlocalizedName("controller");
        GameRegistry.registerBlock(Objects.blockController, ItemBlockMulti.class, "ModJam2013.blockController");

        Objects.blockColoredLampOff = new BlockColoredLamp(ModDiscoTek.blockColoredLampOffId.getValue(), false);
        Objects.blockColoredLampOff.setCreativeTab(Objects.creativeTab).setHardness(0.3F).setStepSound(Block.soundGlassFootstep).setUnlocalizedName("lamp");
        GameRegistry.registerBlock(Objects.blockColoredLampOff, ItemBlockMulti.class, "ModJam2013.blockColoredLampOff");

        Objects.blockColoredLampOn = new BlockColoredLamp(ModDiscoTek.blockColoredLampOnId.getValue(), true);
        Objects.blockColoredLampOn.setHardness(0.3F).setStepSound(Block.soundGlassFootstep).setUnlocalizedName("lamp");
        GameRegistry.registerBlock(Objects.blockColoredLampOn, ItemBlockMulti.class, "ModJam2013.blockColoredLampOn");

        Objects.itemDebug = new ItemDebug(ModDiscoTek.itemDebugId.getValue());
        Objects.itemDebug.setCreativeTab(Objects.creativeTab).func_111206_d(Assets.DOMAIN + "debug").setUnlocalizedName("debug");
        GameRegistry.registerItem(Objects.itemDebug, "ModJam2013.itemDebug");

        Objects.itemLens = new ItemLens(ModDiscoTek.itemLensId.getValue());
        Objects.itemLens.setCreativeTab(Objects.creativeTab).func_111206_d(Assets.DOMAIN + "lens").setUnlocalizedName("lens");
        GameRegistry.registerItem(Objects.itemDebug, "ModJam2013.itemLens");

        Objects.itemWirelessLinker = new ItemWirelessLinker(ModDiscoTek.itemWirelessLinkerId.getValue());
        Objects.itemWirelessLinker.setCreativeTab(Objects.creativeTab).func_111206_d(Assets.DOMAIN + "wirelessLinker").setUnlocalizedName("wirelesslinker");
        GameRegistry.registerItem(Objects.itemWirelessLinker, "ModJam2013.itemWirelessLinker");

        Objects.itemOrienter = new ItemOrienter(ModDiscoTek.itemOrienterId.getValue());
        Objects.itemOrienter.setCreativeTab(Objects.creativeTab).setUnlocalizedName("orienter");
        GameRegistry.registerItem(Objects.itemOrienter, "ModJam2013.itemOrienter");

        Objects.itemCrafting = new ItemCrafting(ModDiscoTek.itemCraftingId.getValue());
        Objects.itemCrafting.setCreativeTab(Objects.creativeTab).setUnlocalizedName("crafting");
        GameRegistry.registerItem(Objects.itemCrafting, "ModJam2013.itemCrafting");

        Objects.creativeTab.setIconItemStack(new ItemStack(Objects.blockLight));

        TileEntity.addMapping(TileEntityLight.class, "ModJam2013.Light");
        TileEntity.addMapping(TileEntityController.class, "ModJam2013.Controller");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
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
        ItemStack piston = new ItemStack(Block.pistonBase, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack stick = new ItemStack(Item.stick, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack brick = new ItemStack(Item.brick, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack netherBrick = new ItemStack(Item.netherrackBrick, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack darkBrick = new ItemStack(Objects.itemCrafting, 1, 3);
        ItemStack stoneBricks = new ItemStack(Block.stoneBrick, 1, 0);
        ItemStack chiseledStoneBricks = new ItemStack(Block.stoneBrick, 1, 3);
        ItemStack chiseledQuartz = new ItemStack(Block.blockNetherQuartz, 1, 1);
        ItemStack redstoneLamp = new ItemStack(Block.redstoneLampIdle, 1, OreDictionary.WILDCARD_VALUE);

        // Basic Controller
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockController, 1, 0), "sss", "dqd", "HrH", 's', stick, 'd', "dyeBlue", 'q', quartz, 'H', hull, 'r', redstone));
        // Advanced Controller
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockController, 1, 1), "lll", "dqd", "HrH", 'l', led, 'd', "dyeRed", 'q', quartz, 'H', hull, 'r', redstone));
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
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockDecoration, 1, 4), "bb", "bb", 'b', darkBrick);
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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.itemCrafting, 8, 3), "bbb", "bdb", "bbb", 'b', netherBrick, 'd', "dyeBlack"));
        // Lens colouring
        GameRegistry.addRecipe(new RecipesLens());
        // Light Crafting Handler
        ICraftingResultHandler handler = new LightCraftingHandler();
        // Fresnel
        GameRegistry.addRecipe(new ShapedHeldCoreRecipe(handler, new ItemStack(Objects.blockLight, 1, 0), "HHH", "HbH", " l ", 'H', hull, 'b', bulb, 'l', lens));
        // SpA 250
        GameRegistry.addRecipe(new ShapedHeldCoreRecipe(handler, new ItemStack(Objects.blockLight, 1, 1), " l ", "sbs", "HsH", 'l', lens, 's', servo, 'b', bulb, 'H', hull));
        // SpA 250 LED
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 2), " G ", "sls", "HsH", 'G', glass, 's', servo, 'l', led, 'H', hull);
        // DMX To Redstone Converter
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 3), "HrH", "rqr", "HrH", 'H', hull, 'r', redstone, 'q', quartz);
        // Radial Laser Emitter
        GameRegistry.addShapedRecipe(new ItemStack(Objects.blockLight, 1, 4), "HlH", "lsl", "HlH", 'H', hull, 'l', led, 's', servo);

        String[] dyes = { "dyeWhite", "dyeOrange", "dyeMagenta", "dyeLightBlue", "dyeYellow", "dyeLime", "dyePink", "dyeGray", "dyeLightGray", "dyeCyan", "dyePurple", "dyeBlue", "dyeBrown", "dyeGreen", "dyeRed", "dyeBlack", };

        // Colored lights
        for (int i = 0; i < dyes.length; i++) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Objects.blockColoredLampOff, 8, i), "LLL", "LdL", "LLL", 'L', redstoneLamp, 'd', dyes[i]));
        }
    }

}
