
package net.specialattack.forge.discotek.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.controller.instance.ControllerPixelInstance;
import net.specialattack.forge.discotek.packet.Packet3PixelSlider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiControllerPixel extends GuiScreen implements ISliderCompat {

    private ControllerPixelInstance controller;
    private int guiHeight;
    private int guiWidth;
    private int[] levels;

    public GuiControllerPixel(ControllerPixelInstance controller) {
        this.controller = controller;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();

        this.guiHeight = 200 + 40;
        this.guiWidth = 14 * 18;

        int chans = 12;
        int rows = 2;
        if (this.levels == null) {
            this.levels = new int[chans * rows];
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < chans; j++) {
                float val = (this.controller.levels[j + (i * chans)] / 255.0F);

                int x = this.width / 2 - ((chans / 2) * 18) + (j * 18);
                int yp = (this.height) / 2 - 95 + (i * 100);

                this.buttonList.add(new GuiVerticalSlider(j + (i * chans), x, yp, "" + (val == 1 ? "FF" : (int) (val * 100)), val, this));
            }
        }

        this.buttonList.add(new GuiButton(-1, (this.width + this.guiWidth) / 2 - 22, (this.height - this.guiHeight) / 2 + 2, 20, 20, "?"));
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == -1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiHelp(this, Assets.HELP_PIXEL));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - this.guiWidth) / 2;
        int y = (this.height - this.guiHeight) / 2;
        this.mc.getTextureManager().bindTexture(Assets.SMALL_GUI);

        Gui.drawRect(x, y, x + this.guiWidth, y + this.guiHeight, 0xFF8888CC);

        String title = StatCollector.translateToLocal("gui.controller.title");
        y += 6;
        x = (this.width - this.fontRendererObj.getStringWidth(title)) / 2;
        this.fontRendererObj.drawString(title, x, y, 0x000000);

        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= 12; j++) {
                int xp = this.width / 2 - (6 * 18) + ((j - 1) * 18);
                int yp = (this.height) / 2 + (i * 100) + 5;
                String txt = (j + (i * 12)) + "";
                this.fontRendererObj.drawString(txt, xp + (16 - this.fontRendererObj.getStringWidth(txt)) / 2, yp - 16, 0x000000);
            }
        }

        this.fontRendererObj.drawString("SpA \u00a74\u00a7l\u00a7oPixEl\u00a7r12/24", x - 60, this.guiHeight + y - 20, 0x000000);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void slideActionPerformed(GuiSlider slider) {
        ModDiscoTek.packetHandler.sendPacketToServer(new Packet3PixelSlider(this.controller, slider.id, (int) (slider.sliderValue * 255)));
    }

}
