
package net.specialattack.modjam.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.specialattack.modjam.Instruction;
import net.specialattack.modjam.tileentity.TileEntityController;

public class GuiInstructions extends Gui {

    public FontRenderer font;
    public int posX;
    public int posY;
    public int width;
    public int height;

    public int rows;
    public int scroll;
    public int selected;
    private TileEntityController controller;

    public GuiInstructions(TileEntityController controller, FontRenderer font, int posX, int posY, int width, int rows) {
        this.font = font;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = rows * 10;
        this.rows = rows;
        this.controller = controller;
    }

    public void render() {
        drawRect(this.posX - 1, this.posY - 1, this.posX + this.width + 1, this.posY + this.height + 1, 0xFFA0A0A0);
        drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, 0xFF000000);

        if (this.rows > this.rows) {
            drawRect(this.posX + this.width - 9, this.posY, this.posX + this.width, this.posY + this.height, 0xFFA0A0A0);

            float percent = (float) this.rows / (float) this.controller.instructions.length;
            int height = (int) (percent * this.height);

            int offset = (int) (((float) this.height / (float) this.controller.instructions.length) * (float) this.scroll);

            drawRect(this.posX + this.width - 8, this.posY + offset, this.posX + this.width, this.posY + height + offset, 0xFFE0E0E0);
        }

        for (int i = this.scroll; i < this.controller.instructions.length && i < this.rows + this.scroll; i++) {
            Instruction instruction = this.controller.instructions[i];

            String display = (i + 1) + ": ";

            if (instruction == null) {
                display += "NOOP";
            }
            else {

            }

            this.font.drawString(display, this.posX + 1, this.posY + (i - this.scroll) * 10 + 1, 0xFFFFFF);
        }

    }

}
