
package net.specialattack.discotek.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.controllerLogic.Instruction;
import net.specialattack.discotek.controllerLogic.InstructionParser;
import net.specialattack.discotek.tileentity.TileEntityController;
import net.specialattack.discotek.tileentity.TileEntitySpAGuo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFancyController extends GuiScreen {
    // Fancy

    private TileEntitySpAGuo controller;
    private boolean initialized = false;
    private int guiHeight;
    private InstructionParser parser;
    private GuiTextField commands;
    private List<Integer> selected;
    private String error = "";
    private long errorTime;

    public GuiFancyController(TileEntityController controller) {
        this.controller = (TileEntitySpAGuo) controller;
        this.parser = new InstructionParser();
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.initialized = true;
        if (this.initialized) {
            this.guiHeight = 64;
        }
        this.commands = new GuiTextField(this.fontRenderer, this.width / 2 - 80, (this.height - this.guiHeight) / 2 + 28, 160, 18);

    }

    @Override
    protected void keyTyped(char character, int key) {
        if (this.initialized) {
            this.commands.textboxKeyTyped(character, key);
            if (key == org.lwjgl.input.Keyboard.KEY_RETURN) {
                Instruction inst = this.parser.validateCommand(this.commands.getText());
                this.commands.setText("");
                if (inst.hasError() && !inst.isNeedsPreSelected()) {
                    this.controller.println(inst.getError());

                    this.error = inst.getError();
                    this.errorTime = System.currentTimeMillis();
                }
                else if (inst.isNeedsPreSelected()) {
                    if (this.selected != null && this.selected.size() > 0) {
                        for (int i = 0; i < this.selected.size(); i++) {
                            this.controller.setChannelLevel(this.selected.get(i) - 1, (short) (inst.getValue()));
                        }
                        // FIXME: Packet 6
                        // FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(6, this.controller));
                    }
                }
                else {
                    for (int i = 0; i < inst.getSelectedCount(); i++) {
                        this.controller.setChannelLevel(inst.getSelectedAt(i), (short) (inst.getValue()));
                    }
                    // FIXME: Packet 6
                    // FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(6, this.controller));
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
        this.mc.getTextureManager().bindTexture(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, 192, this.guiHeight);
        this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, 192, 8);

        String title = I18n.getString("gui.controller.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        if (this.initialized) {
            if (System.currentTimeMillis() < this.errorTime + 8000) {
                this.fontRenderer.drawString(this.error, 10, 10, 0xFFFFFFFF);
            }
        }
        else {
            title = I18n.getString("gui.controller.loading");
            y += 20;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
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
