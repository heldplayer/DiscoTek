
package net.specialattack.discotek.client.gui;

import net.minecraft.client.gui.GuiButton;

public abstract class ModJamSlider extends GuiButton {

    /** The value of this slider control. */
    public float sliderValue = 1.0F;

    /** Is this slider control being dragged. */
    public boolean dragging;

    public GuiSliderCompat parent;

    public ModJamSlider(int par1, int par2, int par3, int par4, int par5, String par6Str) {
        super(par1, par2, par3, par4, par5, par6Str);
    }

}
