
package net.specialattack.modjam.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.tileentity.TileEntityLight;

public class GuiConfigureSmall extends GuiScreen {

    private TileEntityLight light;

    private boolean initialized = false;

    public GuiConfigureSmall(TileEntityLight light) {
        this.light = light;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.initialized) {
            int y = this.height / 2 - 12;
            for (int i = 0; i < this.light.channels.length; i++) {
                this.buttonList.add(new GuiButton(100, this.width / 2 - 80, y, 20, 20, "-"));
                this.buttonList.add(new GuiButton(101, this.width / 2 + 60, y, 20, 20, "+"));
                y += 24;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - 192) / 2;
        int y = (this.height - 64) / 2;
        this.mc.func_110434_K().func_110577_a(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, 192, 64);

        String title = I18n.func_135053_a("gui.light.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;

        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        if (this.initialized) {
            y += 20;
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
