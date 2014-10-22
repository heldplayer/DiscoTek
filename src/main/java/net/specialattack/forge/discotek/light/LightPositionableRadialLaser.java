package net.specialattack.forge.discotek.light;

import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.light.instance.LightPositionableRadialLaserInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightPositionableRadialLaser implements ILight {

    private final List<Channels> channels;

    public LightPositionableRadialLaser() {
        this.channels = Arrays.asList(Channels.BRIGHTNESS, Channels.LENGTH, Channels.PITCH, Channels.ROTATION, Channels.FOCUS, Channels.RED, Channels.GREEN, Channels.BLUE, Channels.BEAT);
    }

    @Override
    public ILightInstance createInstance(TileEntityLight tile) {
        return new LightPositionableRadialLaserInstance(tile);
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
        return "positionableradiallaser";
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
            } else {
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.color", "#FFFFFF"));
            }
            if (compound.hasKey("brightness")) {
                int brightness = (int) (compound.getFloat("brightness") * 100.0F);
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.brightness", brightness + "%"));
            } else {
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.brightness", "100%"));
            }
            if (compound.hasKey("length")) {
                int length = (int) (compound.getFloat("length") * 5.0F);
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.length", length + "%"));
            } else {
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.length", "0%"));
            }
            if (compound.hasKey("focus")) {
                int focus = (int) (compound.getFloat("focus") * 5.0F);
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.focus", focus + "%"));
            } else {
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.focus", "5%"));
            }
        } else {
            list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.color", "#FFFFFF"));
            list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.brightness", "100%"));
            list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.length", "0%"));
            list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.focus", "5%"));
        }
    }

}
