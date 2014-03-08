
package net.specialattack.forge.discotek.light.instance;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.core.MathHelper;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.core.sync.SString;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LightHologramInstance implements ILightInstance {

    private TileEntityLight tile;

    private SInteger direction;
    private SInteger red;
    private SInteger green;
    private SInteger blue;
    private SFloat brightness;
    private SFloat length;
    private SFloat yaw;
    private SFloat focus;
    private SString playerName;
    private int prevRed = 0xFF;
    private int prevGreen = 0xFF;
    private int prevBlue = 0xFF;
    private float prevBrightness = 1.0F;
    private float prevLength = 0.0F;
    private float prevYaw = 0.0F;
    private float prevFocus = 1.0F;

    @SideOnly(Side.CLIENT)
    public EntityOtherPlayerMP player;

    public List<ISyncable> syncables;

    public LightHologramInstance(TileEntityLight tile) {
        this.tile = tile;
        this.direction = new SInteger(tile, 1);
        this.red = new SInteger(tile, 0xFF);
        this.green = new SInteger(tile, 0xFF);
        this.blue = new SInteger(tile, 0xFF);
        this.brightness = new SFloat(tile, 1.0F);
        this.length = new SFloat(tile, 0.0F);
        this.yaw = new SFloat(tile, 0.0F);
        this.focus = new SFloat(tile, 1.0F);
        this.playerName = new SString(tile, "");
        this.syncables = Arrays.asList((ISyncable) this.direction, this.red, this.green, this.blue, this.brightness, this.length, this.yaw, this.focus);
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
            this.prevYaw = this.yaw.getValue();
            this.prevFocus = this.focus.getValue();

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

            String playerName = this.playerName.getValue();
            if (this.player != null) {
                if (playerName.isEmpty()) {
                    this.player = null;
                }
                if (!playerName.equals(this.player.getCommandSenderName())) {
                    this.player = new EntityOtherPlayerMP(this.tile.getWorld(), new GameProfile("", playerName));
                }
                this.player.onUpdate();
            }
            else {
                if (!playerName.isEmpty()) {
                    this.player = new EntityOtherPlayerMP(this.tile.getWorld(), new GameProfile("", playerName));
                }
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
        if (identifier.equals("direction"))
            return this.direction;
        if (identifier.equals("red"))
            return this.red;
        if (identifier.equals("green"))
            return this.green;
        if (identifier.equals("blue"))
            return this.blue;
        if (identifier.equals("brightness"))
            return this.brightness;
        if (identifier.equals("length"))
            return this.length;
        if (identifier.equals("yaw"))
            return this.yaw;
        if (identifier.equals("focus"))
            return this.focus;

        return null;
    }

    @Override
    public void setValue(String identifier, String value) {
        if (identifier.equals("playerName"))
            this.playerName.setValue(value);
    }

    @Override
    public void setValue(String identifier, float value) {
        if (identifier.equals("brightness"))
            this.brightness.setValue(value);
        if (identifier.equals("length"))
            this.length.setValue(value);
        if (identifier.equals("yaw"))
            this.yaw.setValue(value);
        if (identifier.equals("focus"))
            this.focus.setValue(value);
    }

    @Override
    public void setValue(String identifier, int value) {
        if (identifier.equals("red"))
            this.red.setValue(value);
        if (identifier.equals("green"))
            this.green.setValue(value);
        if (identifier.equals("blue"))
            this.blue.setValue(value);
    }

    @Override
    public void setValue(String identifier, boolean value) {}

    @Override
    public String getString(String identifier, float partialTicks) {
        if (identifier.equals("playerName"))
            return this.playerName.getValue();

        return "";
    }

    @Override
    public float getFloat(String identifier, float partialTicks) {
        if (identifier.equals("brightness"))
            return MathHelper.partial(this.brightness.getValue(), this.prevBrightness, partialTicks);
        if (identifier.equals("length"))
            return MathHelper.partial(this.length.getValue(), this.prevLength, partialTicks);
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
        if (identifier.equals("red"))
            return MathHelper.partial(this.red.getValue(), this.prevRed, partialTicks);
        if (identifier.equals("green"))
            return MathHelper.partial(this.green.getValue(), this.prevGreen, partialTicks);
        if (identifier.equals("blue"))
            return MathHelper.partial(this.blue.getValue(), this.prevBlue, partialTicks);

        return 0;
    }

    @Override
    public boolean getBoolean(String identifier, float partialTicks) {
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
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
        this.yaw.setValue(compound.getFloat("yaw"));
        this.prevYaw = this.yaw.getValue();
        this.focus.setValue(compound.getFloat("focus"));
        this.prevFocus = this.focus.getValue();
        this.playerName.setValue(compound.getString("playerName"));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        compound.setInteger("red", this.red.getValue());
        compound.setInteger("green", this.green.getValue());
        compound.setInteger("blue", this.blue.getValue());
        compound.setFloat("brightness", this.brightness.getValue());
        compound.setFloat("length", this.length.getValue());
        compound.setFloat("yaw", this.yaw.getValue());
        compound.setFloat("focus", this.focus.getValue());
        compound.setString("playerName", this.playerName.getValue());
    }

}
