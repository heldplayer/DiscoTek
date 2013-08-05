
package net.specialattack.discotek.client;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.specialattack.discotek.CommonProxy;
import net.specialattack.discotek.Objects;
import net.specialattack.discotek.client.render.ItemRendererBlockLight;
import net.specialattack.discotek.client.render.ItemRendererLens;
import net.specialattack.discotek.client.render.tileentity.TileEntityLightRenderer;
import net.specialattack.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLight.class, new TileEntityLightRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        MinecraftForgeClient.registerItemRenderer(Objects.itemLens.itemID, new ItemRendererLens());
        MinecraftForgeClient.registerItemRenderer(Objects.blockLight.blockID, new ItemRendererBlockLight());
    }

}
