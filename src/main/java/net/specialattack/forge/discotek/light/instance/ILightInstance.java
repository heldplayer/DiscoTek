package net.specialattack.forge.discotek.light.instance;

import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.discotek.block.BlockLight;

public interface ILightInstance {

    List<ISyncable> getSyncables();

    void doTick();

    void setBlockBounds(BlockLight block);

    ISyncable getSyncable(String identifier);

    void setValue(String identifier, String value);

    void setValue(String identifier, float value);

    void setValue(String identifier, int value);

    void setValue(String identifier, boolean value);

    String getString(String identifier, float partialTicks);

    float getFloat(String identifier, float partialTicks);

    int getInteger(String identifier, float partialTicks);

    boolean getBoolean(String identifier, float partialTicks);

    void readFromNBT(NBTTagCompound compound);

    void writeToNBT(NBTTagCompound compound);

    void readLosely(NBTTagCompound compound);

    void writeLosely(NBTTagCompound compound);

}
