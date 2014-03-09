
package net.specialattack.forge.discotek.light.instance;

import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.core.sync.SBoolean;
import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightMapInstance implements ILightInstance {

    private TileEntityLight tile;

    private SInteger direction;
    private SBoolean hasLens;
    private SInteger color;
    private SFloat brightness;
    private SFloat pitch;
    private SFloat yaw;
    private SFloat focus;
    private float prevBrightness = 1.0F;
    private float prevPitch = 0.0F;
    private float prevYaw = 0.0F;
    private float prevFocus = 1.0F;

    private List<ISyncable> syncables;

    public LightMapInstance(TileEntityLight tile) {
        this.tile = tile;
        this.direction = new SInteger(tile, 1);
        this.hasLens = new SBoolean(tile, true);
        this.color = new SInteger(tile, 0xFFFFFF);
        this.brightness = new SFloat(tile, 1.0F);
        this.pitch = new SFloat(tile, 0.0F);
        this.yaw = new SFloat(tile, 0.0F);
        this.focus = new SFloat(tile, 1.0F);
        this.syncables = Arrays.asList((ISyncable) this.direction, this.hasLens, this.color, this.brightness, this.pitch, this.yaw, this.focus);
    }

    @Override
    public List<ISyncable> getSyncables() {
        return this.syncables;
    }

    @Override
    public void doTick() {
        if (this.tile.getWorld().isRemote) {
            this.prevBrightness = this.brightness.getValue();
            this.prevPitch = this.pitch.getValue();
            this.prevYaw = this.yaw.getValue();
            this.prevFocus = this.focus.getValue();

            if (this.pitch.getValue() > 0.8F) {
                this.prevPitch = 0.8F;
                this.pitch.setValue(0.8F);
            }
            else if (this.pitch.getValue() < -0.8F) {
                this.prevPitch = -0.8F;
                this.pitch.setValue(-0.8F);
            }

            if (this.brightness.getValue() > 1.0F) {
                this.prevBrightness = 1.0F;
                this.brightness.setValue(1.0F);
            }
            else if (this.brightness.getValue() < 0.0F) {
                this.prevBrightness = 0.0F;
                this.brightness.setValue(0.0F);
            }

            if (this.focus.getValue() > 20.0F) {
                this.prevFocus = 20.0F;
                this.focus.setValue(20.0F);
            }
            else if (this.focus.getValue() < 0.0F) {
                this.prevFocus = 0.0F;
                this.focus.setValue(0.0F);
            }
        }
    }

    @Override
    public void setBlockBounds(BlockLight block) {
        switch (this.direction.getValue()) {
        case 0:
            block.setBlockBounds(0.0625F, 0.0625F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
        break;
        case 1:
            block.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.9375F, 0.9375F);
        break;
        case 2:
            block.setBlockBounds(0.0625F, 0.0625F, 0.0625F, 0.9375F, 0.9375F, 1.0F);
        break;
        case 3:
            block.setBlockBounds(0.0625F, 0.0625F, 0.0F, 0.9375F, 0.9375F, 0.9375F);
        break;
        case 4:
            block.setBlockBounds(0.0625F, 0.0625F, 0.0625F, 1.0F, 0.9375F, 0.9375F);
        break;
        case 5:
            block.setBlockBounds(0.0F, 0.0625F, 0.0625F, 0.9375F, 0.9375F, 0.9375F);
        break;
        }
    }

    @Override
    public ISyncable getSyncable(String identifier) {
        if (identifier.equals("direction"))
            return this.direction;
        if (identifier.equals("color"))
            return this.color;
        if (identifier.equals("brightness"))
            return this.brightness;
        if (identifier.equals("pitch"))
            return this.pitch;
        if (identifier.equals("yaw"))
            return this.yaw;
        if (identifier.equals("focus"))
            return this.focus;

        return null;
    }

    @Override
    public void setValue(String identifier, String value) {}

    @Override
    public void setValue(String identifier, float value) {
        if (identifier.equals("brightness"))
            this.brightness.setValue(value);
        if (identifier.equals("pitch"))
            this.pitch.setValue(value);
        if (identifier.equals("yaw"))
            this.yaw.setValue(value);
        if (identifier.equals("focus"))
            this.focus.setValue(value);
    }

    @Override
    public void setValue(String identifier, int value) {
        if (identifier.equals("direction"))
            this.direction.setValue(value);
        if (identifier.equals("color"))
            this.color.setValue(value);

        if (identifier.equals("red")) {
            int color = this.color.getValue();
            color = color & 0x00FFFF;
            color = color | ((value & 0xFF) << 16);
            this.color.setValue(color);
        }
        if (identifier.equals("green")) {
            int color = this.color.getValue();
            color = color & 0xFF00FF;
            color = color | ((value & 0xFF) << 8);
            this.color.setValue(color);
        }
        if (identifier.equals("blue")) {
            int color = this.color.getValue();
            color = color & 0xFFFF00;
            color = color | (value & 0xFF);
            this.color.setValue(color);
        }
    }

    @Override
    public void setValue(String identifier, boolean value) {}

    @Override
    public String getString(String identifier, float partialTicks) {
        return "";
    }

    @Override
    public float getFloat(String identifier, float partialTicks) {
        if (identifier.equals("brightness"))
            return MathHelper.partial(this.brightness.getValue(), this.prevBrightness, partialTicks);
        if (identifier.equals("pitch"))
            return MathHelper.partial(this.pitch.getValue(), this.prevPitch, partialTicks);
        if (identifier.equals("yaw"))
            return MathHelper.partial(this.yaw.getValue(), this.prevYaw, partialTicks);
        if (identifier.equals("focus"))
            return MathHelper.partial(this.focus.getValue(), this.prevFocus, partialTicks);

        return 0.0F;
    }

    @Override
    public int getInteger(String identifier, float partialTicks) {
        if (identifier.equals("direction"))
            return this.direction.getValue();
        if (identifier.equals("color"))
            return this.color.getValue();

        if (identifier.equals("red")) {
            int color = this.color.getValue();
            return (color & 0xFF0000) >> 16;
        }
        if (identifier.equals("green")) {
            int color = this.color.getValue();
            return (color & 0xFF00) >> 8;
        }
        if (identifier.equals("blue")) {
            int color = this.color.getValue();
            return color & 0xFF;
        }

        return 0;
    }

    @Override
    public boolean getBoolean(String identifier, float partialTicks) {
        if (identifier.equals("hasLens"))
            return this.hasLens.getValue();

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.hasLens.setValue(compound.getBoolean("hasLens"));
        this.color.setValue(compound.getInteger("color"));
        this.brightness.setValue(compound.getFloat("brightness"));
        this.prevBrightness = this.brightness.getValue();
        this.pitch.setValue(compound.getFloat("pitch"));
        this.prevPitch = this.pitch.getValue();
        this.yaw.setValue(compound.getFloat("yaw"));
        this.prevYaw = this.yaw.getValue();
        this.focus.setValue(compound.getFloat("focus"));
        this.prevFocus = this.focus.getValue();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("hasLens", this.hasLens.getValue());
        compound.setInteger("color", this.color.getValue());
        compound.setFloat("brightness", this.brightness.getValue());
        compound.setFloat("pitch", this.pitch.getValue());
        compound.setFloat("yaw", this.yaw.getValue());
        compound.setFloat("focus", this.focus.getValue());
    }

}
