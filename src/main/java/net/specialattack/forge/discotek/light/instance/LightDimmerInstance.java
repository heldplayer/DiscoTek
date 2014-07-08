package net.specialattack.forge.discotek.light.instance;

import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import java.util.Arrays;
import java.util.List;

public class LightDimmerInstance implements ILightInstance {

    private SInteger redstone;

    private List<ISyncable> syncables;

    public LightDimmerInstance(TileEntityLight tile) {
        this.redstone = new SInteger(tile, 0);
        this.syncables = Arrays.asList((ISyncable) this.redstone);
    }

    @Override
    public List<ISyncable> getSyncables() {
        return this.syncables;
    }

    @Override
    public void doTick() {
    }

    @Override
    public void setBlockBounds(BlockLight block) {
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
    }

    @Override
    public ISyncable getSyncable(String identifier) {
        if (identifier.equals("redstone")) {
            return this.redstone;
        }

        return null;
    }

    @Override
    public void setValue(String identifier, String value) {
    }

    @Override
    public void setValue(String identifier, float value) {
    }

    @Override
    public void setValue(String identifier, int value) {
        if (identifier.equals("redstone")) {
            this.redstone.setValue(value);
        }
    }

    @Override
    public void setValue(String identifier, boolean value) {
    }

    @Override
    public String getString(String identifier, float partialTicks) {
        return "";
    }

    @Override
    public float getFloat(String identifier, float partialTicks) {
        return 0.0F;
    }

    @Override
    public int getInteger(String identifier, float partialTicks) {
        if (identifier.equals("redstone")) {
            return this.redstone.getValue() / 16;
        }

        return 0;
    }

    @Override
    public boolean getBoolean(String identifier, float partialTicks) {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.redstone.setValue(compound.getInteger("redstone"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setInteger("redstone", this.redstone.getValue());
    }

    @Override
    public void readLosely(NBTTagCompound compound) {
    }

    @Override
    public void writeLosely(NBTTagCompound compound) {
    }

}
