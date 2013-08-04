
package net.specialattack.modjam.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.PacketHandler;
import net.specialattack.modjam.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiBasicController extends GuiSliderCompat {

    private TileEntityController controller;
    private boolean initialized = false;
    private int guiHeight = 160;
    private short[] levels;

    public GuiBasicController(TileEntityController controller) {
        this.controller = controller;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.initialized) {
            if (this.levels == null) {
                this.levels = new short[24];
            }

            float b0Val = ((float) this.controller.levels[1] / 255.0f);
            float b1Val = ((float) this.controller.levels[2] / 255.0f);
            float b2Val = ((float) this.controller.levels[3] / 255.0f);
            float b3Val = ((float) this.controller.levels[4] / 255.0f);
            float b4Val = ((float) this.controller.levels[5] / 255.0f);
            float b5Val = ((float) this.controller.levels[6] / 255.0f);
            float b6Val = ((float) this.controller.levels[7] / 255.0f);
            float b7Val = ((float) this.controller.levels[8] / 255.0f);

            this.buttonList.add(new GuiChannelSlider(1, this.width / 2 - 9 - (18 * 3), (this.height) / 2 - 40, "" + (b0Val == 1 ? "FF" : (int) (b0Val * 100)), b0Val, this));
            this.buttonList.add(new GuiChannelSlider(2, this.width / 2 - 9 - (18 * 2), (this.height) / 2 - 40, "" + (b1Val == 1 ? "FF" : (int) (b1Val * 100)), b1Val, this));
            this.buttonList.add(new GuiChannelSlider(3, this.width / 2 - 9 - 18, (this.height) / 2 - 40, "" + (b2Val == 1 ? "FF" : (int) (b2Val * 100)), b2Val, this));
            this.buttonList.add(new GuiChannelSlider(4, this.width / 2 - 9, (this.height) / 2 - 40, "" + (b3Val == 1 ? "FF" : (int) (b3Val * 100)), b3Val, this));
            this.buttonList.add(new GuiChannelSlider(5, this.width / 2 + 9, (this.height) / 2 - 40, "" + (b4Val == 1 ? "FF" : (int) (b4Val * 100)), b4Val, this));
            this.buttonList.add(new GuiChannelSlider(6, this.width / 2 + 9 + (18 * 1), (this.height) / 2 - 40, "" + (b5Val == 1 ? "FF" : (int) (b5Val * 100)), b5Val, this));
            this.buttonList.add(new GuiChannelSlider(7, this.width / 2 + 9 + (18 * 2), (this.height) / 2 - 40, "" + (b6Val == 1 ? "FF" : (int) (b6Val * 100)), b6Val, this));
            this.buttonList.add(new GuiChannelSlider(8, this.width / 2 + 9 + (18 * 3), (this.height) / 2 - 40, "" + (b7Val == 1 ? "FF" : (int) (b7Val * 100)), b7Val, this));

            // this.buttonList.add(new GuiButton(1, this.width / 2 + 30, (this.height + this.guiHeight) / 2 - 20, 50, 20, I18n.func_135053_a("gui.controller.done")));
            // this.buttonList.add(new GuiButton(2, this.width / 2 - 80, (this.height + this.guiHeight) / 2 - 20, 40, 20, I18n.func_135053_a("gui.controller.up")));
            // this.buttonList.add(new GuiButton(3, this.width / 2 - 30, (this.height + this.guiHeight) / 2 - 20, 40, 20, I18n.func_135053_a("gui.controller.down")));
        }
        else {
            this.initialized = this.controller.levels != null;

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
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (this.initialized) {
            //
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

    @Override
    public void slideActionPerformed(ModJamSlider slider) {
        this.controller.setChannelLevel(slider.id, (short) (slider.sliderValue * 255));
        FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(6, this.controller));
    }

}
