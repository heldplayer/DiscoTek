
package net.specialattack.forge.discotek.controller;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.controller.instance.ControllerGrandSpAInstance;
import net.specialattack.forge.discotek.controller.instance.IControllerInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ControllerGrandSpa implements IController {

    @SideOnly(Side.CLIENT)
    private IIcon bottom;
    @SideOnly(Side.CLIENT)
    private IIcon top;
    @SideOnly(Side.CLIENT)
    private IIcon side;

    @Override
    public IControllerInstance createInstance(TileEntityController tile) {
        return new ControllerGrandSpAInstance(tile);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.top = register.registerIcon(Assets.DOMAIN + "controller-top1");
        this.bottom = register.registerIcon(Assets.DOMAIN + "controller-bottom1");
        this.side = register.registerIcon(Assets.DOMAIN + "controller-side1");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side) {
        if (side == 0) {
            return this.bottom;
        }
        else if (side == 1) {
            return this.top;
        }
        return this.side;
    }

    @Override
    public String getIdentifier() {
        return "grandspa";
    }

    public static class ControllerException extends Exception {

        private static final long serialVersionUID = 7486507998690757872L;
        public final int index;

        public ControllerException(String message, int index) {
            super(message);
            this.index = index;
        }

    }

}
