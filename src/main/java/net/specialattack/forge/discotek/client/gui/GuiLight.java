package net.specialattack.forge.discotek.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StatCollector;
import net.specialattack.forge.core.sync.SBoolean;
import net.specialattack.forge.core.sync.SString;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.packet.Packet1LightPort;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

@SideOnly(Side.CLIENT)
public class GuiLight extends GuiScreen implements ISliderCompat {

    private TileEntityLight light;
    private int guiHeight;
    private int guiWidth;
    private GuiHorizontalSlider[] sliders;
    private GuiTextField[] textFields;

    public GuiLight(TileEntityLight light) {
        this.light = light;
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
        x = (this.width - this.fontRendererObj.getStringWidth(title)) / 2;

        this.fontRendererObj.drawString(title, x, y, 0x4F4F4F);

        for (GuiTextField field : this.textFields) {
            if (field != null) {
                field.drawTextBox();
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char character, int key) {
        for (int i = 0; i < this.textFields.length; i++) {
            GuiTextField field = this.textFields[i];
            if (field != null) {
                if (field.isFocused()) {
                    if (key == 1) {
                        field.setFocused(false);
                    } else {
                        field.textboxKeyTyped(character, key);
                        ModDiscoTek.packetHandler.sendPacketToServer(new Packet1LightPort(this.light, i, field.getText()));
                    }
                    return;
                }
            }
        }

        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);

        for (GuiTextField field : this.textFields) {
            if (field != null) {
                field.mouseClicked(par1, par2, par3);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id >= 100) {
            int added = 0;
            int id = button.id - 100;
            if (button.id % 2 == 0) {
                added = -1;
            } else {
                added = 1;
                id--;
            }
            id = id / 2;

            if (GuiScreen.isShiftKeyDown()) {
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

                this.sliders[id].sliderValue = this.light.channels[id].port / 255.0F;
                this.sliders[id].updateText();

                ModDiscoTek.packetHandler.sendPacketToServer(new Packet1LightPort(this.light, id, this.light.channels[id].port));
            }

            return;
        }

        if (button.id == -1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiHelp(this, Assets.HELP_LIGHT));
            return;
        }

        if (button.id < this.light.channels.length) {
            ModDiscoTek.packetHandler.sendPacketToServer(new Packet1LightPort(this.light, button.id, !((SBoolean) this.light.channels[button.id].syncable).getValue()));
            button.displayString = I18n.format("gui.light." + this.light.channels[button.id].channel.identifier + "." + !((SBoolean) this.light.channels[button.id].syncable).getValue());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.clear();

        this.guiHeight = this.light.channels.length * 24 + 30;
        this.guiWidth = 192;

        int y = (this.height - this.guiHeight) / 2 + 29;
        this.sliders = new GuiHorizontalSlider[this.light.channels.length];
        this.textFields = new GuiTextField[this.light.channels.length];
        for (int i = 0; i < this.light.channels.length; i++) {
            switch (this.light.channels[i].channel.type) {
                case 0:
                    this.buttonList.add(new GuiButton(100 + i * 2, this.width / 2 - 90, y, 20, 20, "-"));
                    this.buttonList.add(new GuiButton(101 + i * 2, this.width / 2 + 70, y, 20, 20, "+"));
                    this.buttonList.add(this.sliders[i] = new GuiHorizontalSlider(i, this.width / 2 - 70, y, 140, 20, "gui.light." + this.light.channels[i].channel.identifier, this.light.channels[i].port / 255.0F, this));
                    break;
                case 1:
                    this.textFields[i] = new GuiTextField(this.fontRendererObj, this.width / 2 - 90, y, 180, 20);
                    this.textFields[i].setText(((SString) this.light.channels[i].syncable).getValue());
                    break;
                case 2:
                    this.buttonList.add(new GuiButton(i, this.width / 2 - 90, y, 180, 20, I18n.format("gui.light." + this.light.channels[i].channel.identifier + "." + ((SBoolean) this.light.channels[i].syncable).getValue())));
                    break;
            }
            y += 24;
        }

        this.buttonList.add(new GuiButton(-1, (this.width + this.guiWidth) / 2 - 25, (this.height - this.guiHeight) / 2 + 5, 20, 20, "?"));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        for (GuiTextField field : this.textFields) {
            if (field != null) {
                field.updateCursorCounter();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void slideActionPerformed(GuiSlider slider) {
        int port = (int) (slider.sliderValue * 255.0F);
        this.light.channels[slider.id].port = port;
        ModDiscoTek.packetHandler.sendPacketToServer(new Packet1LightPort(this.light, slider.id, port));
    }

}
