
package net.specialattack.forge.discotek.light.instance;

import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class LightPositionableRadialLaserInstance implements ILightInstance {

    private TileEntityLight tile;

    private SInteger direction;
    private SInteger red;
    private SInteger green;
    private SInteger blue;
    private SFloat brightness;
    private SFloat length;
    private SFloat pitch;
    private SFloat rotation;
    private SFloat focus;
    private int prevRed = 0xFF;
    private int prevGreen = 0xFF;
    private int prevBlue = 0xFF;
    private float prevBrightness = 1.0F;
    private float prevLength = 0.0F;
    private float prevPitch = 0.0F;
    private float prevRotation = 0.0F;
    private float prevFocus = 1.0F;

    private List<ISyncable> syncables;

    public LightPositionableRadialLaserInstance(TileEntityLight tile) {
        this.tile = tile;
        this.direction = new SInteger(tile, 1);
        this.red = new SInteger(tile, 0xFF);
        this.green = new SInteger(tile, 0xFF);
        this.blue = new SInteger(tile, 0xFF);
        this.brightness = new SFloat(tile, 1.0F);
        this.length = new SFloat(tile, 0.0F);
        this.pitch = new SFloat(tile, 0.0F);
        this.rotation = new SFloat(tile, 0.0F);
        this.focus = new SFloat(tile, 1.0F);
        this.syncables = Arrays.asList((ISyncable) this.direction, this.red, this.green, this.blue, this.brightness, this.length, this.pitch, this.rotation, this.focus);
    }

    @Override
    public List<ISyncable> getSyncables() {
        return this.syncables;
    }

    @Override
    public void doTick() {
        if (this.tile.getWorld().isRemote) {
            this.prevRed = this.red.getValue();
            this.prevGreen = this.green.getValue();
            this.prevBlue = this.blue.getValue();
            this.prevBrightness = this.brightness.getValue();
            this.prevLength = this.length.getValue();
            this.prevPitch = this.pitch.getValue();
            this.prevRotation = this.rotation.getValue();
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

            if (this.length.getValue() > 20.0F) {
                this.prevLength = 20.0F;
                this.length.setValue(20.0F);
            }
            else if (this.length.getValue() < 0.0F) {
                this.prevLength = 0.0F;
                this.length.setValue(0.0F);
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
        if (identifier.equals("direction")) {
            return this.direction;
        }
        if (identifier.equals("red")) {
            return this.red;
        }
        if (identifier.equals("green")) {
            return this.green;
        }
        if (identifier.equals("blue")) {
            return this.blue;
        }
        if (identifier.equals("brightness")) {
            return this.brightness;
        }
        if (identifier.equals("length")) {
            return this.length;
        }
        if (identifier.equals("pitch")) {
            return this.pitch;
        }
        if (identifier.equals("rotation")) {
            return this.rotation;
        }
        if (identifier.equals("focus")) {
            return this.focus;
        }

        return null;
    }

    @Override
    public void setValue(String identifier, String value) {}

    @Override
    public void setValue(String identifier, float value) {
        if (identifier.equals("brightness")) {
            this.brightness.setValue(value);
        }
        if (identifier.equals("length")) {
            this.length.setValue(value);
        }
        if (identifier.equals("pitch")) {
            this.pitch.setValue(value);
        }
        if (identifier.equals("rotation")) {
            this.rotation.setValue(value);
        }
        if (identifier.equals("focus")) {
            this.focus.setValue(value);
        }
    }

    @Override
    public void setValue(String identifier, int value) {
        if (identifier.equals("direction")) {
            this.direction.setValue(value);
        }
        if (identifier.equals("red")) {
            this.red.setValue(value);
        }
        if (identifier.equals("green")) {
            this.green.setValue(value);
        }
        if (identifier.equals("blue")) {
            this.blue.setValue(value);
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
        if (identifier.equals("brightness")) {
            return MathHelper.partial(this.prevBrightness, this.brightness.getValue(), partialTicks);
        }
        if (identifier.equals("length")) {
            return MathHelper.partial(this.prevLength, this.length.getValue(), partialTicks);
        }
        if (identifier.equals("pitch")) {
            return MathHelper.partial(this.prevPitch, this.pitch.getValue(), partialTicks);
        }
        if (identifier.equals("rotation")) {
            return MathHelper.partial(this.prevRotation, this.rotation.getValue(), partialTicks);
        }
        if (identifier.equals("focus")) {
            return MathHelper.partial(this.prevFocus, this.focus.getValue(), partialTicks);
        }

        return 0.0F;
    }

    @Override
    public int getInteger(String identifier, float partialTicks) {
        if (identifier.equals("direction")) {
            return this.direction.getValue();
        }
        if (identifier.equals("red")) {
            return MathHelper.partial(this.prevRed, this.red.getValue(), partialTicks);
        }
        if (identifier.equals("green")) {
            return MathHelper.partial(this.prevGreen, this.green.getValue(), partialTicks);
        }
        if (identifier.equals("blue")) {
            return MathHelper.partial(this.prevBlue, this.blue.getValue(), partialTicks);
        }

        return 0;
    }

    @Override
    public boolean getBoolean(String identifier, float partialTicks) {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.red.setValue(compound.getInteger("red"));
        this.prevRed = this.red.getValue();
        this.green.setValue(compound.getInteger("green"));
        this.prevGreen = this.green.getValue();
        this.blue.setValue(compound.getInteger("blue"));
        this.prevBlue = this.blue.getValue();
        this.brightness.setValue(compound.getFloat("brightness"));
        this.prevBrightness = this.brightness.getValue();
        this.length.setValue(compound.getFloat("length"));
        this.prevLength = this.length.getValue();
        this.pitch.setValue(compound.getFloat("pitch"));
        this.prevPitch = this.pitch.getValue();
        this.rotation.setValue(compound.getFloat("rotation"));
        this.prevRotation = this.rotation.getValue();
        this.focus.setValue(compound.getFloat("focus"));
        this.prevFocus = this.focus.getValue();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setInteger("red", this.red.getValue());
        compound.setInteger("green", this.green.getValue());
        compound.setInteger("blue", this.blue.getValue());
        compound.setFloat("brightness", this.brightness.getValue());
        compound.setFloat("length", this.length.getValue());
        compound.setFloat("pitch", this.pitch.getValue());
        compound.setFloat("rotation", this.rotation.getValue());
        compound.setFloat("focus", this.focus.getValue());
    }

}
