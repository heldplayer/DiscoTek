package net.specialattack.forge.discotek.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.specialattack.forge.core.client.MC;
import net.specialattack.forge.discotek.Assets;
import org.apache.commons.io.Charsets;

@SideOnly(Side.CLIENT)
public class GuiHelp extends GuiScreen {

    private GuiScreen parent;
    private int guiHeight = 164;
    private List<String> lines;
    private int page;
    private GuiButton prev;
    private GuiButton next;

    @SuppressWarnings("unchecked")
    public GuiHelp(GuiScreen parent, ResourceLocation helpFile) {
        this.parent = parent;
        this.lines = new ArrayList<String>();

        try {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(MC.getResourceManager().getResource(helpFile).getInputStream(), Charsets.UTF_8));

            String line;
            while ((line = bufferedreader.readLine()) != null) {
                line = line.replaceAll("%", "\u00a7");
                this.lines.addAll(MC.getFontRenderer().listFormattedStringToWidth(line, 180));
            }
        } catch (IOException e) {
            e.printStackTrace();
            MC.getMc().displayGuiScreen(this.parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - 192) / 2;
        int y = (this.height - this.guiHeight) / 2;
        this.mc.getTextureManager().bindTexture(Assets.SMALL_GUI);
        this.drawTexturedModalRect(x, y, 0, 0, 192, this.guiHeight);
        this.drawTexturedModalRect(x, y + this.guiHeight, 0, 248, 192, 8);

        String title = StatCollector.translateToLocal("gui.help.title");
        y += 6;
        x = (this.width - this.fontRendererObj.getStringWidth(title)) / 2;
        this.fontRendererObj.drawString(title, x, y, 0x4F4F4F);

        y += 12;
        x = (this.width - 192) / 2 + 6;

        for (int i = this.page * 12; i < (this.page + 1) * 12 && i < this.lines.size(); i++) {
            this.fontRendererObj.drawString(this.lines.get(i), x, y, 0x4F4F4F);
            y += 10;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            if (this.page > 0) {
                this.page--;
                this.next.enabled = true;
            }
            if (this.page <= 0) {
                this.prev.enabled = false;
            }
        } else if (button.id == 1) {
            if (this.page * 12 <= this.lines.size()) {
                this.page++;
                this.prev.enabled = true;
            }
            if ((this.page + 1) * 12 >= this.lines.size()) {
                this.next.enabled = false;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        this.buttonList.clear();

        this.buttonList.add(this.prev = new GuiButton(0, this.width / 2 - 80, (this.height + this.guiHeight) / 2 - 20, 50, 20, StatCollector.translateToLocal("gui.help.previous")));
        this.buttonList.add(this.next = new GuiButton(1, this.width / 2 + 30, (this.height + this.guiHeight) / 2 - 20, 50, 20, StatCollector.translateToLocal("gui.help.next")));

        this.prev.enabled = false;
        if (this.lines.size() < 13) {
            this.next.enabled = false;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
