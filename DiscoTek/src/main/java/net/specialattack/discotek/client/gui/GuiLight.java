
package net.specialattack.discotek.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.packet.Packet1LightPort;
import net.specialattack.discotek.packet.PacketHandler;
import net.specialattack.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLight extends GuiScreen implements ISliderCompat {

    private TileEntityLight light;
    private int guiHeight;
    private int guiWidth;
    private GuiHorizontalSlider[] sliders;

    public GuiLight(TileEntityLight light) {
        this.light = light;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();

        this.guiHeight = this.light.channels.length * 24 + 30;
        this.guiWidth = 192;

        int y = (this.height - this.guiHeight) / 2 + 29;
        this.sliders = new GuiHorizontalSlider[this.light.channels.length];
        for (int i = 0; i < this.light.channels.length; i++) {
            this.buttonList.add(new GuiButton(100 + i * 2, this.width / 2 - 90, y, 20, 20, "-"));
            this.buttonList.add(new GuiButton(101 + i * 2, this.width / 2 + 70, y, 20, 20, "+"));
            this.buttonList.add(this.sliders[i] = new GuiHorizontalSlider(i, this.width / 2 - 70, y, 140, 20, "gui.light." + this.light.channels[i].channel.identifier, (float) this.light.channels[i].port / 255.0F, this));
            y += 24;
        }

        this.buttonList.add(new GuiButton(-1, (this.width + this.guiWidth) / 2 - 25, (this.height - this.guiHeight) / 2 + 5, 20, 20, "?"));
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
                this.light.channels[id].port += added;
                if (this.light.channels[id].port < 0) {
                    this.light.channels[id].port = 0;
                }
                if (this.light.channels[id].port > 255) {
                    this.light.channels[id].port = 255;
                }

                this.sliders[id].sliderValue = (float) this.light.channels[id].port / 255.0F;
                this.sliders[id].updateText();

                FMLClientHandler.instance().sendPacket(PacketHandler.instance.createPacket(new Packet1LightPort(this.light, id, this.light.channels[id].port)));
            }
        }

        if (button.id == -1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiHelp(this, Assets.HELP_LIGHT));
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

        String title = StatCollector.translateToLocal("gui.light.title");
        y += 6;
        x = (this.width - this.fontRenderer.getStringWidth(title)) / 2;

        this.fontRenderer.drawString(title, x, y, 0x4F4F4F);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void slideActionPerformed(GuiSlider slider) {
        int port = (int) ((float) slider.sliderValue * 255.0F);
        this.light.channels[slider.id].port = port;
        FMLClientHandler.instance().sendPacket(PacketHandler.instance.createPacket(new Packet1LightPort(this.light, slider.id, port)));
    }
}
