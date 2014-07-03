
package net.specialattack.forge.discotek.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.specialattack.forge.discotek.Assets;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiVerticalSlider extends GuiSlider {

    public GuiVerticalSlider(int id, int posX, int posY, String displayString, float value, ISliderCompat parent) {
        super(id, posX, posY, 16, 80, displayString);
        this.sliderValue = value;
        this.parent = parent;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over
     * this button and 2 if it IS hovering over
     * this button.
     */
    @Override
    public int getHoverState(boolean mouseOver) {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of
     * MouseListener.mouseDragged(MouseEvent e).
     */
    @Override
    protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = 1.0F - (float) (mouseY - (this.yPosition + 1)) / (float) (this.height - 4);

                if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
                }

                this.displayString = "" + (this.sliderValue == 1 ? "FF" : (int) (this.sliderValue * 100));
                this.parent.slideActionPerformed(this);
            }
            minecraft.getTextureManager().bindTexture(Assets.SMALL_GUI);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + 1, this.yPosition + this.height - 4 - (int) (this.sliderValue * (this.height - 4)), 209, 0, 17, 4);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of
     * MouseListener.mousePressed(MouseEvent
     * e).
     */
    @Override
    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
        if (super.mousePressed(minecraft, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseY + (this.yPosition + 4)) / (float) (this.height - 8);

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
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = minecraft.fontRenderer;
            minecraft.getTextureManager().bindTexture(Assets.SMALL_GUI);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 192, 0, this.width, this.height);
            this.mouseDragged(minecraft, mouseX, mouseY);
            int color = 0xFFE0E0E0;

            if (!this.enabled) {
                color = 0xFFA0A0A0;
            }
            else if (this.field_146123_n) {
                color = 0xFFFFFFA0;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2 + 1, this.yPosition + (this.height - 8) / 2, color);
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of
     * MouseListener.mouseReleased(MouseEvent e).
     */
    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }
}
