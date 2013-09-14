
package net.specialattack.discotek.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.PacketHandler;
import net.specialattack.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLight extends GuiSliderCompat {

    private TileEntityLight light;
    private boolean initialized = false;
    private int guiHeight;
    private GuiHorizontalSlider[] sliders;

    public GuiLight(TileEntityLight light) {
        this.light = light;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.initialized) {
            this.guiHeight = this.light.channels.length * 24 + 20;

            int y = (this.height - this.guiHeight) / 2 + 14;
            this.sliders = new GuiHorizontalSlider[this.light.channels.length];
            for (int i = 0; i < this.light.channels.length; i++) {
                this.buttonList.add(new GuiButton(100 + i * 2, this.width / 2 - 90, y, 20, 20, "-"));
                this.buttonList.add(new GuiButton(101 + i * 2, this.width / 2 + 70, y, 20, 20, "+"));
                this.buttonList.add(this.sliders[i] = new GuiHorizontalSlider(i, this.width / 2 - 70, y, 140, 20, "gui.light." + i, (float) this.light.channels[i] / 255.0F, this));
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

                this.sliders[id].sliderValue = (float) this.light.channels[id] / 255.0F;

                FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(4, this.light, id, this.light.channels[id]));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - 192) / 2;
        int y = (this.height - this.guiHeight - 8) / 2;
        this.mc.getTextureManager().bindTexture(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, 192, this.guiHeight);
        this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, 192, 8);

        String title = I18n.getString("gui.light.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;

        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        if (this.initialized) {
            y += 14;
            for (int i = 0; i < this.light.channels.length; i++) {
                //title = I18n.func_135052_a("gui.light." + i, this.light.channels[i]);
                //x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
                //this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
                y += 24;
            }
        }
        else {
            title = I18n.getString("gui.light.loading");
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

    @Override
    public void slideActionPerformed(ModJamSlider slider) {
        if (this.initialized) {
            this.light.channels[slider.id] = (int) ((float) slider.sliderValue * 255.0F);
            FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(4, this.light, slider.id, this.light.channels[slider.id]));
        }
    }
}
