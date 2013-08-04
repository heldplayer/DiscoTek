
package net.specialattack.modjam.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.PacketHandler;
import net.specialattack.modjam.tileentity.TileEntityLight;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiLight extends GuiSliderCompat {
    //Moved sot hat you can watch the light & edit it at the same time
    private TileEntityLight light;
    private boolean initialized = false;
    private int guiHeight;

    public GuiLight(TileEntityLight light) {
        this.light = light;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.initialized) {
            this.guiHeight = 24 * 4 + 20;
            int y = 16;
            float pan = this.light.getYaw(0) / 8.0f;
            float tilt = (this.light.getPitch(0) + this.light.pitchRange[light.getBlockMetadata() & 0xFF]) / (this.light.pitchRange[light.getBlockMetadata() & 0xFF] * 2);
            float focus = (this.light.getFocus(0) / 20.0f);
            this.buttonList.add(new GuiButton(100, 192 / 2 - 80, y, 20, 20, "-"));
            this.buttonList.add(new GuiButton(101, 192 / 2 + 60, y, 20, 20, "+"));
            y += 24;
            this.buttonList.add(new GuiHorizontalSlider(1, (192 - 150) / 2, y, "Pan: ", pan, this));
            y += 24;
            this.buttonList.add(new GuiHorizontalSlider(2, (192 - 150) / 2, y, "Tilt: ", tilt, this));
            y += 24;
            this.buttonList.add(new GuiHorizontalSlider(3, (192 - 150) / 2, y, "Focus: ", focus, this));

        }
        else {
            this.guiHeight = 64;

            this.initialized = true;

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
            if (button.id % 2 == 0) {
                added = -1;
            }
            else {
                added = 1;
            }

            if (isShiftKeyDown()) {
                added *= 10;
            }

            this.light.channel += added;
            if (this.light.channel < 0) {
                this.light.channel = 0;
            }
            if (this.light.channel > 255) {
                this.light.channel = 255;
            }

            FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(4, this.light, this.light.channel));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = 0;//(this.width - 192) / 2;
        int y = 0;//(this.height - this.guiHeight - 8) / 2;
        this.mc.func_110434_K().func_110577_a(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, 192, this.guiHeight);
        this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, 192, 8);

        String title = I18n.func_135053_a("gui.light.title");
        y += 6;
        x = (192 - this.fontRenderer.getStringWidth(title)) / 2;

        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        if (this.initialized) {
            y += 14;
            title = I18n.func_135052_a("gui.light.channel", this.light.channel);
            x = (192 - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
            y += 24;
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
            this.initialized = true;

            if (this.initialized) {
                this.initGui();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void slideActionPerformed(ModJamSlider slider) {
        if (this.initialized) {
            if (slider.id == 1) {
                float value = slider.sliderValue * 8f;
                FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(7, this.light, 1, value));
                light.setYaw(value);
            }
            else if (slider.id == 2) {
                float value = (slider.sliderValue * this.light.pitchRange[light.getBlockMetadata()] * 2) - this.light.pitchRange[light.getBlockMetadata()];
                FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(7, this.light, 2, value));
                light.setPitch(value);
            }
            else if (slider.id == 3) {
                float value = (slider.sliderValue) * 20f;
                FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(7, this.light, 3, value));
                light.setFocus(value);
            }
        }
    }

}
