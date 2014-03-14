
package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightHologramInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightHologram implements ILight {

    private final List<Channels> channels;

    public LightHologram() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.HEAD_ROTATION, Channels.PITCH, Channels.ROTATION, Channels.SIZE, Channels.RED, Channels.GREEN, Channels.BLUE, Channels.NAME);
    }

    @Override
    public ILightInstance createInstance(TileEntityLight tile) {
        return new LightHologramInstance(tile);
    }

    @Override
    public List<Channels> getChannels() {
        return this.channels;
    }

    @Override
    public boolean supportsLens() {
        return false;
    }

    @Override
    public String getIdentifier() {
        return "hologram";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean extra) {
        NBTTagCompound compound = stack.stackTagCompound;

        if (compound != null) {
            if (compound.hasKey("color")) {
                String color = Integer.toHexString(compound.getInteger("color")).toUpperCase();
                while (color.length() < 6) {
                    color = "0" + color;
                }
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.color", "#" + color));
            }
            else {
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.color", "#FFFFFF"));
            }
            if (compound.hasKey("brightness")) {
                int brightness = (int) (compound.getFloat("brightness") * 100.0F);
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.brightness", brightness + "%"));
            }
            else {
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.brightness", "100%"));
            }
            if (compound.hasKey("size")) {
                int size = (int) (compound.getFloat("size") * 5.0F);
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.size", size + "%"));
            }
            else {
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.size", "5%"));
            }
            if (compound.hasKey("playerName")) {
                String playerName = compound.getString("playerName");
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.playerName", playerName));
            }
            else {
                list.add(StatCollector.translateToLocal("gui.tooltip.light.playerName.none"));
            }
        }
        else {
            list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.color", "#FFFFFF"));
            list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.brightness", "100%"));
            list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.size", "5%"));
            list.add(StatCollector.translateToLocal("gui.tooltip.light.playerName.none"));
        }

    }

}
