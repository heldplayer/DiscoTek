
package net.specialattack.discotek.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.controllers.ControllerGrandSpa;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiControllerGrandSpA extends GuiScreen {

    private ControllerGrandSpa.ControllerInstance controller;
    private int guiHeight;
    private int guiWidth;
    private GuiInstructionsGrandSpA instructions;

    public GuiControllerGrandSpA(ControllerGrandSpa.ControllerInstance controller) {
        this.controller = controller;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        this.guiHeight = 164;
        this.guiWidth = 192;

        if (this.instructions == null) {
            this.instructions = new GuiInstructionsGrandSpA(this.controller, this.fontRenderer, this.width / 2 - 80, (this.height - this.guiHeight) / 2 + 28, 160, 11);
        }
        else {
            this.instructions.posX = this.width / 2 - 80;
            this.instructions.posY = (this.height - this.guiHeight) / 2 + 24;
        }

        this.buttonList.add(new GuiButton(1, this.width / 2 + 30, (this.height + this.guiHeight) / 2 - 20, 50, 20, StatCollector.translateToLocal("gui.controller.done")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 80, (this.height + this.guiHeight) / 2 - 20, 40, 20, StatCollector.translateToLocal("gui.controller.up")));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 30, (this.height + this.guiHeight) / 2 - 20, 40, 20, StatCollector.translateToLocal("gui.controller.down")));

        this.buttonList.add(new GuiButton(-1, (this.width + this.guiWidth) / 2 - 25, (this.height - this.guiHeight) / 2 + 5, 20, 20, "?"));
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (this.instructions.onKeyPressed(character, key)) {
            return;
        }

        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        this.instructions.onClick(mouseX, mouseY, button);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            // FIXME: Packet 5
            // FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(5, this.controller));
        }
        if (button.id == 2) {
            if (this.instructions.scroll > 0) {
                this.instructions.scroll--;
            }
        }
        if (button.id == 3) {
            if (this.instructions.scroll < this.controller.instructions.length - this.instructions.rows) {
                this.instructions.scroll++;
            }
        }

        if (button.id == -1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiHelp(this, Assets.HELP_GRANDSPA));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - this.guiWidth) / 2;
        int y = (this.height - this.guiHeight) / 2;
        this.mc.getTextureManager().bindTexture(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, this.guiWidth, this.guiHeight);
        this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, this.guiWidth, 8);

        String title = StatCollector.translateToLocal("gui.controller.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        if (this.controller.error != null) {
            title = StatCollector.translateToLocalFormatted(this.controller.error, this.controller.errorIndex);
            y += 8;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0xFF4444);
        }

        this.instructions.render();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
