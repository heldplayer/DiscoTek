
package net.specialattack.modjam.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.specialattack.modjam.Assets;

public class GuiControllerHelp extends GuiScreen {

    private GuiScreen parent;
    private int guiHeight = 164;
    private String[][] lines;
    private int page;
    private GuiButton prev;
    private GuiButton next;

    public GuiControllerHelp(GuiScreen parent) {
        this.parent = parent;
        this.lines = new String[][] { //
        new String[] { // Page 1
        "Thank you for buying this device!", //
        "This manual aims at teaching you", //
        "how to operate the machine.", //
        "", //
        "The syntax used to program using", //
        "this device is \u00a79IDENTIFIER\u00a7c_\u00a79PARAM", //
        "If you try to write anything that", //
        "does not follow that syntax the", //
        "device will not accept it!", //
        "", //
        "The parameter is a number that", //
        "can range from 0 to 255", //
        }, //
        new String[] { // Page 2
        "This device has a built-in stack", //
        "that can store up to 16 entries.", //
        "If more than 16 entries are put", //
        "on the stack, then the device will", //
        "halt with an error. If the stack is", //
        "empty and something attempts to", //
        "read from the stack, the machine", //
        "will halt with an error as well.", //
        "", //
        "These are called stack overflow", //
        "and stack underflow errors", //
        "respectively.", //
        }, //
        new String[] { // Page 3
        "When a number is called $1 it is the", //
        "last inserted number in the stack.", //
        "When it is called $2 it is the", //
        "second last number on the stack.", //
        "This can go up to $16 due to the", //
        "limitations of the stack, as that", //
        "is the maximum allowed values.", //
        "", //
        "You can not however, use a $", //
        "number as a parameter. ", //
        }, //
        new String[] { // Page 4
        "List of identifiers:", //
        "", //
        "\u00a79SLEEP_PARAM", //
        "Sleeps for 'PARAM' ticks, there are", //
        "20 seconds in a tick", //
        "", //
        "\u00a79PUSH_PARAM", //
        "Pushes the value 'PARAM' to the", //
        "stack", //
        "", //
        "\u00a79POP_0", //
        "Pops a value off of the stack", //
        }, //
        new String[] { // Page 5
        "\u00a79LEV_PARAM", //
        "Sets channel $1 to 'PARAM'", //
        "", //
        "\u00a79LEV2_PARAM", //
        "Sets channel $1 to 'PARAM' and", //
        "sets channel $3 to $2", //
        "", //
        "\u00a79LEV3_PARAM", //
        "Sets channel $1 to 'PARAM',", //
        "sets channel $3 to $2 and", //
        "sets channel $5 to $4", //
        }, //
        new String[] { // Page 6
        "\u00a79MOT_PARAM", //
        "Changes channel $2 to $1 over", //
        "a period of 'PARAM' ticks", //
        "", //
        "\u00a79MOT2_PARAM", //
        "Changes channel $2 to $1 and", //
        "channel $4 to $3 over a period", //
        "of 'PARAM' ticks", //
        }, //
        new String[] { // Page 7
        "\u00a79MOT3_PARAM", //
        "Changes channel $2 to $1,", //
        "channel $4 to $3 and channel $6", //
        "to $5 over a period of 'PARAM' ticks", //
        "", //
        "\u00a79GOTO_PARAM", //
        "Goes to the instruction specified", //
        "by 'PARAM', minimum value is 1", //
        "", //
        "\u00a79CLEAR_0", //
        "Clears the stack", //
        }, //
        };

        //"The following identifiers can be", //
        //"used with this device.", //
        //"If the identifier is unknown or",
        //"the stack is extended outside of",
        //"its limits, the machine will halt",
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        this.buttonList.clear();

        this.buttonList.add(prev = new GuiButton(0, this.width / 2 - 80, (this.height + this.guiHeight) / 2 - 20, 50, 20, I18n.func_135053_a("gui.controller.help.previous")));
        this.buttonList.add(next = new GuiButton(1, this.width / 2 + 30, (this.height + this.guiHeight) / 2 - 20, 50, 20, I18n.func_135053_a("gui.controller.help.next")));

        this.prev.enabled = false;
        if (this.lines.length == 1) {
            this.next.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            if (this.page > 0) {
                this.page--;
                this.next.enabled = true;
            }
            if (this.page <= 0) {
                this.prev.enabled = false;
            }
        }
        else if (button.id == 1) {
            if (this.page <= this.lines.length) {
                this.page++;
                this.prev.enabled = true;
            }
            if (this.page >= this.lines.length - 1) {
                this.next.enabled = false;
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - 192) / 2;
        int y = (this.height - this.guiHeight) / 2;
        this.mc.func_110434_K().func_110577_a(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, 192, this.guiHeight);
        this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, 192, 8);

        String title = I18n.func_135053_a("gui.controller.help.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        y += 12;
        x = (this.width - 192) / 2 + 6;

        for (String str : this.lines[page]) {
            this.fontRenderer.drawString(str, x, y, 0x4F4F4F);
            y += 10;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
