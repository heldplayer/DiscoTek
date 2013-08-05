
package net.specialattack.discotek.client.gui;

import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiSliderCompat extends GuiScreen {

    public abstract void slideActionPerformed(ModJamSlider slider);

}
