
package net.specialattack.modjam.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.PacketHandler;
import net.specialattack.modjam.tileentity.TileEntityLight;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiConfigureSmall extends GuiScreen {

    private TileEntityLight light;
    private boolean initialized = false;
    private int guiHeight;

    public GuiConfigureSmall(TileEntityLight light) {
        this.light = light;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.initialized) {
            this.guiHeight = this.light.channels.length * 24 + 20;

            int y = this.height / 2 - 12;
            for (int i = 0; i < this.light.channels.length; i++) {
                this.buttonList.add(new GuiButton(100, this.width / 2 - 80, y, 20, 20, "-"));
                this.buttonList.add(new GuiButton(101, this.width / 2 + 60, y, 20, 20, "+"));
                y += 24;
            }
        }
        else {
            this.guiHeight = 64;

            this.initialized = this.light.channels != null;

            if (this.initialized) {
                this.initGui();
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id >= 100) {
            int added = 0;
            int id = button.id - 100;
            if (button.id % 2 == 0) {
                added = -1;
            }
            else {
                added = 1;
                id--;
            }
            id = id / 2;

            if (isShiftKeyDown()) {
                added *= 10;
            }

            if (this.light.channels.length > id) {
                this.light.channels[id] += added;
                if (this.light.channels[id] < 0) {
                    this.light.channels[id] = 0;
                }
                if (this.light.channels[id] > 255) {
                    this.light.channels[id] = 255;
                }

                FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(4, this.light, id, this.light.channels[id]));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - 192) / 2;
        int y = (this.height - this.guiHeight - 8) / 2;
        this.mc.func_110434_K().func_110577_a(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, 192, this.guiHeight);
        this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, 192, 8);

        String title = I18n.func_135053_a("gui.light.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;

        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        if (this.initialized) {
            y += 14;
            for (int i = 0; i < this.light.channels.length; i++) {
                title = I18n.func_135052_a("gui.light." + i, this.light.channels[i]);
                x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
                this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
                y += 24;
            }
        }
        else {
            title = I18n.func_135052_a("gui.light.loading");
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
            this.initialized = this.light.channels != null;

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
