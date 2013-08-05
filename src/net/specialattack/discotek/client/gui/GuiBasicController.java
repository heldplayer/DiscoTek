
package net.specialattack.discotek.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.PacketHandler;
import net.specialattack.discotek.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBasicController extends GuiSliderCompat {

    private TileEntityController controller;
    private boolean initialized = false;
    private int guiHeight;
    private short[] levels;
    private int guiWidth;

    public GuiBasicController(TileEntityController controller) {
        this.controller = controller;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.initialized) {
            this.guiHeight = 200 + 40;
            this.guiWidth = 14 * 18;

            int chans = 12;
            int rows = 2;
            if (this.levels == null) {
                this.levels = new short[chans * rows];
            }

            for (int y = 0; y < rows; y++) {
                for (int i = 1; i <= chans; i++) {
                    float val = ((float) this.controller.levels[i + (y * chans)] / 255.0F);

                    int x = this.width / 2 - ((chans / 2) * 18) + ((i - 1) * 18);
                    int yp = (this.height) / 2 - 100 + (y * 100);

                    this.buttonList.add(new GuiChannelSlider(i + (y * chans), x, yp, "" + (val == 1 ? "FF" : (int) (val * 100)), val, this));
                }
            }

            // this.buttonList.add(new GuiButton(1, this.width / 2 + 30, (this.height + this.guiHeight) / 2 - 20, 50, 20, I18n.func_135053_a("gui.controller.done")));
            // this.buttonList.add(new GuiButton(2, this.width / 2 - 80, (this.height + this.guiHeight) / 2 - 20, 40, 20, I18n.func_135053_a("gui.controller.up")));
            // this.buttonList.add(new GuiButton(3, this.width / 2 - 30, (this.height + this.guiHeight) / 2 - 20, 40, 20, I18n.func_135053_a("gui.controller.down")));
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
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - this.guiWidth) / 2;
        int y = (this.height - this.guiHeight) / 2;
        this.mc.func_110434_K().func_110577_a(Assets.SMALL_GUI);
        //this.drawTexturedModalRect(x, y, 0, 0, this.guiWidth, this.guiHeight);
        //this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, this.guiWidth, 8);

        drawRect(x, y, x + this.guiWidth, y + this.guiHeight, 0xFF8888CC);

        String title = I18n.func_135053_a("gui.controller.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;

        if (!this.initialized) {
            title = I18n.func_135052_a("gui.controller.loading");
            y += 20;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
        }

        for (int yi = 0; yi < 2; yi++) {
            for (int i = 1; i <= 12; i++) {
                int xp = this.width / 2 - (6 * 18) + ((i - 1) * 18);
                int yp = (this.height) / 2 + (yi * 100);
                String txt = (i + (yi * 12)) + "";
                this.fontRenderer.drawString(txt, xp + (16 - this.fontRenderer.getStringWidth(txt)) / 2, yp - 16, 0x3F3F3F);
            }
        }

        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
        this.fontRenderer.drawString("SpA", x - 60, this.guiHeight + y - 20, 0x000000);
        this.fontRenderer.drawString("\u00a7l\u00a7oPixEl", x - 39, this.guiHeight + y - 20, 0xAA0000);
        this.fontRenderer.drawString("12/24", x - 10, this.guiHeight + y - 20, 0x000000);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (!this.initialized) {
            this.initialized = this.controller.levels[0] != -1;

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
