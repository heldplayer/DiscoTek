
package net.specialattack.modjam.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.PacketHandler;
import net.specialattack.modjam.controllerLogic.Instruction;
import net.specialattack.modjam.controllerLogic.InstructionParser;
import net.specialattack.modjam.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiFancyController extends GuiScreen {

    private TileEntityController controller;
    private boolean initialized = false;
    private int guiHeight;
    private InstructionParser parser;
    private GuiTextField commands;
    private List<Integer> selected;
    private String error = "";
    private long errorTime;

    public GuiFancyController(TileEntityController controller) {
        this.controller = controller;
        this.parser = new InstructionParser();
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.initialized = true;
        if (this.initialized) {
            this.guiHeight = 164;
        }
        this.commands = new GuiTextField(this.fontRenderer, 30, 30, 100, 18);

    }

    @Override
    protected void keyTyped(char character, int key) {
        if (this.initialized) {
            this.commands.textboxKeyTyped(character, key);
            if (key == org.lwjgl.input.Keyboard.KEY_RETURN) {
                Instruction inst = this.parser.validateCommand(this.commands.getText());
                this.commands.setText("");
                if (inst.hasError() && !inst.isNeedsPreSelected()) {
                    this.error = inst.getError();
                    this.errorTime = System.currentTimeMillis();
                }
                else if (inst.isNeedsPreSelected()) {
                    if (this.selected != null && this.selected.size() > 0) {
                        for (int i = 0; i < this.selected.size(); i++) {
                            this.controller.setChannelLevel(this.selected.get(i) - 1, (short) (inst.getValue()));
                        }
                        FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(6, this.controller));
                    }
                }
                else {
                    for (int i = 0; i < inst.getSelectedCount(); i++) {
                        this.controller.setChannelLevel(inst.getSelectedAt(i), (short) (inst.getValue()));
                    }
                    FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(6, this.controller));
                    if (inst.getSelectedCount() > 0) {
                        this.selected = inst.getSelected();
                    }
                }
            }
        }

        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (this.initialized) {
            this.commands.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - 192) / 2;
        int y = (this.height - this.guiHeight) / 2;
        this.mc.func_110434_K().func_110577_a(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, 192, this.guiHeight);
        this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, 192, 8);

        String title = I18n.func_135053_a("gui.controller.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        if (this.initialized) {

        }
        else {
            title = I18n.func_135052_a("gui.controller.loading");
            y += 20;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
        }
        if (System.currentTimeMillis() < this.errorTime + 8000) {
            this.fontRenderer.drawString(this.error, 10, 10, 0xFFFFFFFF);
        }
        this.commands.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (!this.initialized) {
            this.initialized = true;

            if (this.initialized) {
                this.initGui();
                this.commands.updateCursorCounter();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
