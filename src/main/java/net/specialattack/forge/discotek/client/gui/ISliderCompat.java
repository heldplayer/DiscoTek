package net.specialattack.forge.discotek.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISliderCompat {

    void slideActionPerformed(GuiSlider slider);

}
