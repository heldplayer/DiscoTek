
package net.specialattack.modjam.client;

import net.specialattack.modjam.CommonProxy;
import net.specialattack.modjam.block.TileEntityLight;
import net.specialattack.modjam.client.render.tileentity.TileEntityLightRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLight.class, new TileEntityLightRenderer());
    }

}
