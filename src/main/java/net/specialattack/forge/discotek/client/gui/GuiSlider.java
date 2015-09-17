package net.specialattack.forge.discotek.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;

@SideOnly(Side.CLIENT)
public abstract class GuiSlider extends GuiButton {

    /**
     * The value of this slider control.
     */
    public float sliderValue = 1.0F;

    /**
     * Is this slider control being dragged.
     */
    public boolean dragging;

    public ISliderCompat parent;

    public GuiSlider(int id, int posX, int posY, int width, int height, String displayString) {
        super(id, posX, posY, width, height, displayString);
    }
}
