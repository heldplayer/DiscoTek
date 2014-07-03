
package net.specialattack.forge.discotek.light.instance;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.core.sync.SBoolean;
import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.core.sync.SString;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.sync.SVariableFloat;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;
import net.specialattack.util.MathHelper;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LightHologramInstance implements ILightInstance {

    private TileEntityLight tile;

    private SInteger direction;
    private SInteger red;
    private SInteger green;
    private SInteger blue;
    private SVariableFloat brightness;
    private SFloat size;
    private SFloat pitch;
    private SFloat rotation;
    private SFloat headRotation;
    private SString playerName;
    private SBoolean beat;
    private int prevRed = 0xFF;
    private int prevGreen = 0xFF;
    private int prevBlue = 0xFF;
    private float prevBrightness = 1.0F;
    private float prevSize = 1.0F;
    private float prevPitch = 0.0F;
    private float prevRotation = 0.0F;
    private float prevHeadRotation = 0.0F;

    @SideOnly(Side.CLIENT)
    public EntityOtherPlayerMP player;

    public List<ISyncable> syncables;

    public LightHologramInstance(TileEntityLight tile) {
        this.tile = tile;
        this.direction = new SInteger(tile, 1);
        this.red = new SInteger(tile, 0xFF);
        this.green = new SInteger(tile, 0xFF);
        this.blue = new SInteger(tile, 0xFF);
        this.brightness = new SVariableFloat(tile, 1.0F);
        this.size = new SFloat(tile, 1.0F);
        this.pitch = new SFloat(tile, 0.0F);
        this.rotation = new SFloat(tile, 0.0F);
        this.headRotation = new SFloat(tile, 0.0F);
        this.playerName = new SString(tile, "");
        this.beat = new SBoolean(tile, false);
        this.syncables = Arrays.asList((ISyncable) this.direction, this.red, this.green, this.blue, this.brightness, this.size, this.pitch, this.rotation, this.headRotation, this.playerName, this.beat);
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
            this.prevSize = this.size.getValue();
            this.prevPitch = this.pitch.getValue();
            this.prevRotation = this.rotation.getValue();
            this.prevHeadRotation = this.headRotation.getValue();

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

            if (this.size.getValue() > 20.0F) {
                this.prevSize = 20.0F;
                this.size.setValue(20.0F);
            }
            else if (this.size.getValue() < 0.0F) {
                this.prevSize = 0.0F;
                this.size.setValue(0.0F);
            }

            String playerName = this.playerName.getValue();
            if (this.player != null && playerName != null) {
                if (playerName.isEmpty()) {
                    this.player = null;
                }
                else if (!playerName.equals(this.player.getCommandSenderName())) {
                    this.player = new EntityOtherPlayerMP(this.tile.getWorld(), new GameProfile(null, playerName));
                }
                if (this.player != null) {
                    this.player.onUpdate();
                }
            }
            else if (playerName == null) {
                this.player = null;
            }
            else if (!playerName.isEmpty()) {
                this.player = new EntityOtherPlayerMP(this.tile.getWorld(), new GameProfile(null, playerName));
            }
        }
    }

    @Override
    public void setBlockBounds(BlockLight block) {
        switch (this.direction.getValue()) {
        case 0:
        //block.setBlockBounds(0.125F, 0.5625F, 0.125F, 0.875F, 1.0F, 0.875F);
        break;
        case 1:
        //block.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.4375F, 0.875F);
        break;
        case 2:
        //block.setBlockBounds(0.125F, 0.125F, 0.5625F, 0.875F, 0.875F, 1.0F);
        break;
        case 3:
        //block.setBlockBounds(0.125F, 0.125F, 0.0F, 0.875F, 0.875F, 0.4375F);
        break;
        case 4:
        //block.setBlockBounds(0.5625F, 0.125F, 0.125F, 1.0F, 0.875F, 0.875F);
        break;
        case 5:
        //block.setBlockBounds(0.0F, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F);
        break;
        }
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
        if (identifier.equals("size")) {
            return this.size;
        }
        if (identifier.equals("pitch")) {
            return this.pitch;
        }
        if (identifier.equals("rotation")) {
            return this.rotation;
        }
        if (identifier.equals("headRotation")) {
            return this.headRotation;
        }
        if (identifier.equals("name")) {
            return this.playerName;
        }
        if (identifier.equals("beat")) {
            return this.beat;
        }

        return null;
    }

    @Override
    public void setValue(String identifier, String value) {
        if (identifier.equals("playerName")) {
            this.playerName.setValue(value);
        }
    }

    @Override
    public void setValue(String identifier, float value) {
        if (identifier.equals("brightness")) {
            this.brightness.setValue(value);
        }
        if (identifier.equals("size")) {
            this.size.setValue(value);
        }
        if (identifier.equals("pitch")) {
            this.pitch.setValue(value);
        }
        if (identifier.equals("rotation")) {
            this.rotation.setValue(value);
        }
        if (identifier.equals("headRotation")) {
            this.headRotation.setValue(value);
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
        if (identifier.equals("playerName")) {
            return this.playerName.getValue();
        }

        return "";
    }

    @Override
    public float getFloat(String identifier, float partialTicks) {
        if (identifier.equals("brightness")) {
            return MathHelper.partial(this.prevBrightness, this.brightness.getValue(), partialTicks);
        }
        if (identifier.equals("size")) {
            return MathHelper.partial(this.prevSize, this.size.getValue(), partialTicks);
        }
        if (identifier.equals("pitch")) {
            return MathHelper.partial(this.prevPitch, this.pitch.getValue(), partialTicks);
        }
        if (identifier.equals("rotation")) {
            return MathHelper.partial(this.prevRotation, this.rotation.getValue(), partialTicks);
        }
        if (identifier.equals("headRotation")) {
            return MathHelper.partial(this.prevHeadRotation, this.headRotation.getValue(), partialTicks);
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
        if (identifier.equals("beat")) {
            return this.beat.getValue();
        }

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.direction.setValue(compound.getInteger("direction"));
        this.red.setValue(compound.getInteger("red"));
        this.prevRed = this.red.getValue();
        this.green.setValue(compound.getInteger("green"));
        this.prevGreen = this.green.getValue();
        this.blue.setValue(compound.getInteger("blue"));
        this.prevBlue = this.blue.getValue();
        this.brightness.setValue(compound.getFloat("brightness"));
        this.prevBrightness = this.brightness.getValue();
        this.size.setValue(compound.getFloat("size"));
        this.prevSize = this.size.getValue();
        this.rotation.setValue(compound.getFloat("rotation"));
        this.prevRotation = this.rotation.getValue();
        this.headRotation.setValue(compound.getFloat("headRotation"));
        this.prevHeadRotation = this.headRotation.getValue();
        this.playerName.setValue(compound.getString("playerName"));
        this.beat.setValue(compound.getBoolean("beat"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setInteger("direction", this.direction.getValue());
        compound.setInteger("red", this.red.getValue());
        compound.setInteger("green", this.green.getValue());
        compound.setInteger("blue", this.blue.getValue());
        compound.setFloat("brightness", this.brightness.getValueDirect());
        compound.setFloat("size", this.size.getValue());
        compound.setFloat("rotation", this.rotation.getValue());
        compound.setFloat("headRotation", this.headRotation.getValue());
        compound.setString("playerName", this.playerName.getValue());
        compound.setBoolean("beat", this.beat.getValue());
    }

    @Override
    public void readLosely(NBTTagCompound compound) {
        int color = compound.getInteger("color");
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        this.red.setValue(red);
        this.prevRed = red;
        this.green.setValue(green);
        this.prevGreen = green;
        this.blue.setValue(blue);
        this.prevBlue = blue;
        this.brightness.setValue(compound.getFloat("brightness"));
        this.prevBrightness = this.brightness.getValue();
        this.size.setValue(compound.getFloat("size"));
        this.prevSize = this.size.getValue();
        this.playerName.setValue(compound.getString("playerName"));
        this.beat.setValue(compound.getBoolean("beat"));
    }

    @Override
    public void writeLosely(NBTTagCompound compound) {
        int red = this.red.getValue();
        int green = this.green.getValue();
        int blue = this.blue.getValue();
        int color = (red << 16) | (green << 8) | blue;
        compound.setInteger("color", color);
        compound.setFloat("brightness", this.brightness.getValueDirect());
        compound.setFloat("size", this.size.getValue());
        compound.setString("playerName", this.playerName.getValue());
        compound.setBoolean("beat", this.beat.getValue());
    }

}
