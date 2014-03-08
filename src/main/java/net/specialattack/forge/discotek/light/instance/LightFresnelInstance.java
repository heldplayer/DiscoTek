
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

public class LightFresnelInstance implements ILightInstance {

    private TileEntityLight tile;

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

    public LightFresnelInstance(TileEntityLight tile) {
        this.tile = tile;
        this.hasLens = new SBoolean(tile, true);
        this.color = new SInteger(tile, 0xFFFFFF);
        this.brightness = new SFloat(tile, 1.0F);
        this.pitch = new SFloat(tile, 0.0F);
        this.yaw = new SFloat(tile, 0.0F);
        this.focus = new SFloat(tile, 1.0F);
        this.syncables = Arrays.asList((ISyncable) this.hasLens, this.color, this.brightness, this.pitch, this.yaw, this.focus);
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
        block.setBlockBounds(0.0625F, 0.125F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

    @Override
    public ISyncable getSyncable(String identifier) {
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
        if (identifier.equals("color"))
            this.color.setValue(value);
    }

    @Override
    public void setValue(String identifier, boolean value) {
        if (identifier.equals("hasLens"))
            this.hasLens.setValue(value);
    }

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
        if (identifier.equals("color"))
            return this.color.getValue();

        return 0;
    }

    @Override
    public boolean getBoolean(String identifier, float partialTicks) {
        if (identifier.equals("hasLens"))
            return this.hasLens.getValue();

        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
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
    public void readFromNBT(NBTTagCompound compound) {
        compound.setBoolean("hasLens", this.hasLens.getValue());
        compound.setInteger("color", this.color.getValue());
        compound.setFloat("brightness", this.brightness.getValue());
        compound.setFloat("pitch", this.pitch.getValue());
        compound.setFloat("yaw", this.yaw.getValue());
        compound.setFloat("focus", this.focus.getValue());
    }

}
