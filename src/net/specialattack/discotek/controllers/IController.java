
package net.specialattack.discotek.controllers;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.specialattack.discotek.tileentity.TileEntityController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IController {

    IControllerInstance createInstance(TileEntityController tile);

    @SideOnly(Side.CLIENT)
    void registerIcons(IconRegister register);

    @SideOnly(Side.CLIENT)
    Icon getIcon(int side);

    String getIdentifier();

}
