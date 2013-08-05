
package net.specialattack.discotek.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHorizontalSlider extends ModJamSlider {

    private String baseDisplayString;

    public GuiHorizontalSlider(int id, int posX, int posY, int width, int height, String display, float value, GuiSliderCompat parent) {
        super(id, posX, posY, width, height, display);
        this.sliderValue = value;
        this.parent = parent;
        this.baseDisplayString = display;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over
     * this button and 2 if it IS hovering over
     * this button.
     */
    @Override
    protected int getHoverState(boolean par1) {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of
     * MouseListener.mouseDragged(MouseEvent e).
     */
    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        if (this.drawButton) {
            if (this.dragging) {
                this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);

                if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
                }

            }

            this.displayString = I18n.func_135052_a(this.baseDisplayString, (int) (this.sliderValue * 255.0F));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
            this.parent.slideActionPerformed(this);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of
     * MouseListener.mousePressed(MouseEvent
     * e).
     */
    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);

            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }

            this.dragging = true;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of
     * MouseListener.mouseReleased(MouseEvent e).
     */
    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }
}
