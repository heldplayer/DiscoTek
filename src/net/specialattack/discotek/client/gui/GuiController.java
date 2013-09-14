
package net.specialattack.discotek.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.PacketHandler;
import net.specialattack.discotek.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiController extends GuiScreen {

    private TileEntityController controller;
    private boolean initialized = false;
    private int guiHeight;
    private GuiInstructions instructions;

    public GuiController(TileEntityController controller) {
        this.controller = controller;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.initialized) {
            this.guiHeight = 164;

            if (this.instructions == null) {
                this.instructions = new GuiInstructions(this.controller, this.fontRenderer, this.width / 2 - 80, (this.height - this.guiHeight) / 2 + 28, 160, 11);
            }
            else {
                this.instructions.posX = this.width / 2 - 80;
                this.instructions.posY = (this.height - this.guiHeight) / 2 + 24;
            }

            this.buttonList.add(new GuiButton(1, this.width / 2 + 30, (this.height + this.guiHeight) / 2 - 20, 50, 20, I18n.getString("gui.controller.done")));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 80, (this.height + this.guiHeight) / 2 - 20, 40, 20, I18n.getString("gui.controller.up")));
            this.buttonList.add(new GuiButton(3, this.width / 2 - 30, (this.height + this.guiHeight) / 2 - 20, 40, 20, I18n.getString("gui.controller.down")));
            this.buttonList.add(new GuiButton(4, this.width / 2 + 60, (this.height - this.guiHeight) / 2 + 2, 20, 20, "?"));
        }
        else {
            this.guiHeight = 64;

            this.initialized = this.controller.instructions != null;

            if (this.initialized) {
                this.initGui();
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (this.initialized) {
            if (this.instructions.onKeyPressed(character, key)) {
                return;
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
            this.instructions.onClick(mouseX, mouseY, button);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(5, this.controller));
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
        if (button.id == 4) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiControllerHelp(this));
        }
    }

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

        if (this.controller.error != null) {
            title = I18n.getStringParams(this.controller.error, this.controller.errorIndex);
            y += 8;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0xFF4444);
        }

        if (this.initialized) {
            this.instructions.render();
        }
        else {
            title = I18n.getString("gui.controller.loading");
            y += 20;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (!this.initialized) {
            this.initialized = this.controller.instructions != null;

            if (this.initialized) {
                this.initGui();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
