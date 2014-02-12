
package net.specialattack.forge.discotek.controllers;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.specialattack.forge.discotek.tileentity.TileEntityController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IController {

    IControllerInstance createInstance(TileEntityController tile);

    @SideOnly(Side.CLIENT)
    void registerIcons(IIconRegister register);

    @SideOnly(Side.CLIENT)
    IIcon getIcon(int side);

    String getIdentifier();

}
