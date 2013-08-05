
package net.specialattack.modjam.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.specialattack.modjam.Assets;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiChannelSlider extends ModJamSlider {
    public GuiChannelSlider(int par1, int par2, int par3, String par5Str, float par6, GuiSliderCompat parent) {
        super(par1, par2, par3, 16, 80, par5Str);
        this.sliderValue = par6;
        this.parent = parent;
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
                this.sliderValue = 1.0f - (float) (par3 - (this.yPosition + 1)) / (float) (this.height - 4);

                if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
                }

                this.displayString = "" + (this.sliderValue == 1 ? "FF" : (int) (this.sliderValue * 100));
                this.parent.slideActionPerformed(this);
            }
            par1Minecraft.func_110434_K().func_110577_a(Assets.SMALL_GUI);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + 1, this.yPosition + this.height - 4 - (int) (this.sliderValue * (float) (this.height - 4)), 209, 0, 17, 4);
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
            this.sliderValue = (float) (par3 + (this.yPosition + 4)) / (float) (this.height - 8);

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

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (this.drawButton) {
            FontRenderer fontrenderer = par1Minecraft.fontRenderer;
            par1Minecraft.func_110434_K().func_110577_a(Assets.SMALL_GUI);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 192, 0, this.width, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);
            int l = 14737632;

            if (!this.enabled) {
                l = -6250336;
            }
            else if (this.field_82253_i) {
                l = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2 + 1, this.yPosition + (this.height - 8) / 2, l);
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
