
package net.specialattack.discotek.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.controllers.ControllerPixel;
import net.specialattack.discotek.packet.Packet3PixelSlider;
import net.specialattack.discotek.packet.PacketHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiControllerPixel extends GuiScreen implements ISliderCompat {

    private ControllerPixel.ControllerInstance controller;
    private boolean initialized = false;
    private int guiHeight;
    private int[] levels;
    private int guiWidth;

    public GuiControllerPixel(ControllerPixel.ControllerInstance controller) {
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
                this.levels = new int[chans * rows];
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < chans; j++) {
                    float val = ((float) this.controller.levels[j + (i * chans)] / 255.0F);

                    int x = this.width / 2 - ((chans / 2) * 18) + (j * 18);
                    int yp = (this.height) / 2 - 100 + (i * 100);

                    this.buttonList.add(new GuiVerticalSlider(j + (i * chans), x, yp, "" + (val == 1 ? "FF" : (int) (val * 100)), val, this));
                }
            }
        }
        else {
            this.guiHeight = 64;
            this.guiWidth = 192;

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
    }

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - this.guiWidth) / 2;
        int y = (this.height - this.guiHeight) / 2;
        this.mc.getTextureManager().bindTexture(Assets.SMALL_GUI);

        if (!this.initialized) {
            this.drawTexturedModalRect(x, y, 0, 0, 192, this.guiHeight);
            this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, 192, 8);

            String title = StatCollector.translateToLocal("gui.controller.title");
            y += 6;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

            title = StatCollector.translateToLocal("gui.controller.loading");
            y += 20;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0x4F4F4F);
        }
        else {
            drawRect(x, y, x + this.guiWidth, y + this.guiHeight, 0xFF8888CC);

            String title = StatCollector.translateToLocal("gui.controller.title");
            y += 6;
            x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;
            this.fontRenderer.drawString(title, x, y, 0x000000);

            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= 12; j++) {
                    int xp = this.width / 2 - (6 * 18) + ((j - 1) * 18);
                    int yp = (this.height) / 2 + (i * 100);
                    String txt = (j + (i * 12)) + "";
                    this.fontRenderer.drawString(txt, xp + (16 - this.fontRenderer.getStringWidth(txt)) / 2, yp - 16, 0x000000);
                }
            }

            this.fontRenderer.drawString("SpA \u00a74\u00a7l\u00a7oPixEl\u00a7r12/24", x - 60, this.guiHeight + y - 20, 0x000000);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (!this.initialized) {
            this.initialized = this.controller.levels != null;

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
    public void slideActionPerformed(GuiSlider slider) {
        //this.controller.tile.transmitLevelChange(slider.id, (short) (slider.sliderValue * 255));
        FMLClientHandler.instance().sendPacket(PacketHandler.instance.createPacket(new Packet3PixelSlider(this.controller, slider.id, (int) (slider.sliderValue * 255))));
        // FIXME: Packet 6
        // FMLClientHandler.instance().sendPacket(PacketHandler.createPacket(6, this.controller));
    }

}
