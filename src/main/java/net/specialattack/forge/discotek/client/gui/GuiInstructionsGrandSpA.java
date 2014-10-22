package net.specialattack.forge.discotek.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import net.specialattack.forge.discotek.Instruction;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.controller.instance.ControllerGrandSpAInstance;
import net.specialattack.forge.discotek.packet.Packet5GrandSpAInstruction;

@SideOnly(Side.CLIENT)
public class GuiInstructionsGrandSpA extends Gui {

    public FontRenderer font;
    public int posX;
    public int posY;
    public int width;
    public int height;

    public int rows;
    public int scroll;
    public int selected = -1;
    public String editingString;
    private boolean editing;
    private ControllerGrandSpAInstance controller;

    public GuiInstructionsGrandSpA(ControllerGrandSpAInstance controller, FontRenderer font, int posX, int posY, int width, int rows) {
        this.font = font;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = rows * 10;
        this.rows = rows;
        this.controller = controller;
    }

    public void render() {
        Gui.drawRect(this.posX - 1, this.posY - 1, this.posX + this.width + 1, this.posY + this.height + 1, 0xFFA0A0A0);
        Gui.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, 0xFF000000);

        if (this.rows > this.rows) {
            Gui.drawRect(this.posX + this.width - 9, this.posY, this.posX + this.width, this.posY + this.height, 0xFFA0A0A0);

            float percent = (float) this.rows / (float) this.controller.instructions.length;
            int height = (int) (percent * this.height);

            int offset = (int) (((float) this.height / (float) this.controller.instructions.length) * this.scroll);

            Gui.drawRect(this.posX + this.width - 8, this.posY + offset, this.posX + this.width, this.posY + height + offset, 0xFFE0E0E0);
        }

        for (int i = this.scroll; i < this.controller.instructions.length && i < this.rows + this.scroll; i++) {
            Instruction instruction = this.controller.instructions[i];

            String display = (i + 1) + ": ";
            if (i < 9) {
                display = "0" + display;
            }

            if (!this.editing || this.selected != i) {
                if (instruction == null || instruction.identifier.equals("NOOP")) {
                    display += "NULL";
                } else {
                    display += instruction.identifier + "_" + instruction.argument;
                }
            }

            if (this.selected == i) {
                if (this.editing) {
                    this.font.drawString(display + this.editingString, this.posX + 1, this.posY + (i - this.scroll) * 10 + 1, 0x88FF88);
                } else {
                    this.font.drawString(display, this.posX + 1, this.posY + (i - this.scroll) * 10 + 1, 0xFFFF88);
                }
            } else {
                this.font.drawString(display, this.posX + 1, this.posY + (i - this.scroll) * 10 + 1, 0xFFFFFF);
            }
        }
    }

    public void onClick(int mouseX, int mouseY, int button) {
        for (int i = this.scroll; i < this.controller.instructions.length && i < this.rows + this.scroll; i++) {
            int y = this.posY + (i - this.scroll) * 10;
            if (mouseX > this.posX && mouseX < this.posX + this.width && mouseY > y && mouseY < y + 10) {
                if (this.selected == i) {
                    if (this.editing) {
                        this.stopEditing();
                    } else {
                        this.beginEditing();
                    }
                } else {
                    if (!this.editing) {
                        this.selected = i;
                    }
                    break;
                }
            }
        }
    }

    public boolean stopEditing() {
        if (this.editingString.length() == 0) {
            this.controller.instructions[this.selected] = null;
            this.editing = false;
            return true;
        }

        if (this.editingString.indexOf('_') < 1) {
            return false;
        }
        String first = this.editingString.substring(0, this.editingString.indexOf('_')).toUpperCase();
        String last = this.editingString.substring(this.editingString.indexOf('_') + 1);

        int arg;

        try {
            arg = Integer.parseInt(last);
        } catch (Exception e) {
            return false;
        }

        if (arg < 0 || arg > 255) {
            return false;
        }

        Instruction instruction = this.controller.instructions[this.selected];

        if (instruction == null) {
            instruction = this.controller.instructions[this.selected] = new Instruction();
        }

        instruction.identifier = first;
        instruction.argument = arg;

        ModDiscoTek.packetHandler.sendPacketToServer(new Packet5GrandSpAInstruction(this.controller, this.selected, instruction.identifier, instruction.argument));

        this.editing = false;

        return true;
    }

    public void beginEditing() {
        this.editing = true;
        Instruction instruction = this.controller.instructions[this.selected];
        this.editingString = instruction == null || instruction.identifier.equals("NOOP") ? "" : instruction.identifier + "_" + instruction.argument;
    }

    public boolean onKeyPressed(char character, int key) {
        if (this.editing) {
            switch (key) {
                case 1:
                    this.editing = false;

                    return true;
                case 14:
                    if (this.editingString.length() > 0) {
                        this.editingString = this.editingString.substring(0, this.editingString.length() - 1);
                    }

                    return true;
                case 28:
                    if (this.stopEditing()) {
                        if (this.selected < this.controller.instructions.length - 1) {
                            this.selected++;
                            this.beginEditing();
                        }
                        if (this.selected == this.rows + this.scroll && this.scroll < this.controller.instructions.length - this.rows) {
                            this.scroll++;
                        }
                    }

                    return true;
                case 211:
                    if (this.editingString.length() > 0) {
                        this.editingString = this.editingString.substring(0, this.editingString.length() - 1);
                    }

                    return true;
                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                        this.editingString = this.editingString + Character.toString(character);
                        return true;
                    }
                    return true;
            }
        }
        switch (key) {
            case 28:
                this.beginEditing();

                return true;
            case 200:
                if (this.selected > 0) {
                    this.selected--;
                }
                if (this.selected == this.scroll - 1 && this.scroll > 0) {
                    this.scroll--;
                }

                return true;
            case 208:
                if (this.selected < this.controller.instructions.length - 1) {
                    this.selected++;
                }
                if (this.selected == this.rows + this.scroll && this.scroll < this.controller.instructions.length - this.rows) {
                    this.scroll++;
                }

                return true;
        }

        return false;
    }
}
