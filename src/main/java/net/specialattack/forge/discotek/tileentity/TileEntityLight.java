
package net.specialattack.forge.discotek.tileentity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.specialattack.forge.core.SpACore;
import net.specialattack.forge.core.sync.ISyncable;
import net.specialattack.forge.core.sync.ISyncableObjectOwner;
import net.specialattack.forge.core.sync.SBoolean;
import net.specialattack.forge.core.sync.SDouble;
import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.core.sync.SString;
import net.specialattack.forge.core.sync.packet.Packet1TrackingStatus;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.client.ClientProxy;
import net.specialattack.forge.discotek.lights.ChannelSyncablePair;
import net.specialattack.forge.discotek.lights.Channels;
import net.specialattack.forge.discotek.lights.ILight;
import net.specialattack.forge.discotek.lights.ILightRenderHandler;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Interface(modid = "ComputerCraft", iface = "dan200.computer.api.IPeripheral")
public class TileEntityLight extends TileEntity implements ISyncableObjectOwner { //, IPeripheral {

    //    private SBoolean hasLens;
    //    private SInteger direction;
    //
    //    private SFloat pitch;
    //    private SFloat yaw;
    //    private SFloat brightness;
    //    private SFloat focus;
    //    private SInteger red;
    //    private SInteger green;
    //    private SInteger blue;
    //    private float prevPitch = 0.0F;
    //    private float prevYaw = 0.0F;
    //    private float prevBrightness = 1.0F;
    //    private float prevFocus = 1.0F;
    //    private int prevRed = 0xFF;
    //    private int prevGreen = 0xFF;
    //    private int prevBlue = 0xFF;

    private List<ChannelSyncablePair> syncablePairs;
    private List<ISyncable> syncables;

    public List<ChannelLevel> channels;

    public TileEntityLight() {
        //        this.hasLens = new SBoolean(this, true);
        //        this.pitch = new SFloat(this, 0.0F);
        //        this.yaw = new SFloat(this, 0.0F);
        //        this.brightness = new SFloat(this, 1.0F);
        //        this.focus = new SFloat(this, 1.0F);
        //        this.direction = new SInteger(this, 0);
        //        this.red = new SInteger(this, 0xFF);
        //        this.green = new SInteger(this, 0xFF);
        //        this.blue = new SInteger(this, 0xFF);
        //        this.syncables = Arrays.asList((ISyncable) this.hasLens, this.pitch, this.yaw, this.brightness, this.focus, this.direction, this.red, this.green, this.blue);
    }

    public TileEntityLight(Block blockType, int meta, boolean setupChannels) {
        this();
        this.blockMetadata = meta;
        this.blockType = blockType;
        if (setupChannels) {
            this.setupChannels();
        }
    }

    public void setupChannels() {
        this.syncablePairs = this.createSyncablePairs();
        if (this.syncablePairs == null) {
            return;
        }
        ArrayList<ISyncable> syncables = new ArrayList<ISyncable>();
        this.channels = new ArrayList<TileEntityLight.ChannelLevel>();
        for (ChannelSyncablePair pair : this.syncablePairs) {
            if (pair.channel != null) {
                this.channels.add(new ChannelLevel(pair.channel, pair.syncable));
            }
            syncables.add(pair.syncable);
            if (pair.syncable instanceof SInteger) {
                pair.prevValue = ((SInteger) pair.syncable).getValue();
            }
            else if (pair.syncable instanceof SBoolean) {
                pair.prevValue = ((SBoolean) pair.syncable).getValue();
            }
            else if (pair.syncable instanceof SFloat) {
                pair.prevValue = ((SFloat) pair.syncable).getValue();
            }
            else if (pair.syncable instanceof SDouble) {
                pair.prevValue = ((SDouble) pair.syncable).getValue();
            }
            else if (pair.syncable instanceof SString) {
                pair.prevValue = ((SString) pair.syncable).getValue();
            }
        }
        this.syncables = Collections.unmodifiableList(syncables);
    }

    @Override
    public void validate() {
        super.validate();

        if (this.worldObj != null && this.worldObj.isRemote) {
            ClientProxy.addTile(this);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if (this.worldObj != null && this.worldObj.isRemote) {
            ClientProxy.addTile(this);
        }
    }

    public ChannelSyncablePair getSyncablePair(String identifier) {
        if (this.syncablePairs == null) {
            return null;
        }
        for (ChannelSyncablePair pair : this.syncablePairs) {
            if (pair.name.equals(identifier)) {
                return pair;
            }
        }
        return null;
    }

    public int getDirection() {
        ChannelSyncablePair pair = this.getSyncablePair("direction");
        return pair != null ? ((SInteger) pair.syncable).getValue() : 0;
    }

    public void setDirection(int side) {
        ChannelSyncablePair pair = this.getSyncablePair("direction");
        if (pair != null)
            pair.syncable.setValue(side);
    }

    public int getColor(float partialTicks) {
        ChannelSyncablePair red = this.getSyncablePair("red");
        ChannelSyncablePair green = this.getSyncablePair("green");
        ChannelSyncablePair blue = this.getSyncablePair("blue");
        if (red == null || green == null || blue == null)
            return 0;

        int iRed = (int) (((Integer) red.prevValue) + (((SInteger) red.syncable).getValue() - ((Integer) red.prevValue)) * partialTicks);
        int iGreen = (int) (((Integer) green.prevValue) + (((SInteger) green.syncable).getValue() - ((Integer) green.prevValue)) * partialTicks);
        int iBlue = (int) (((Integer) blue.prevValue) + (((SInteger) blue.syncable).getValue() - ((Integer) blue.prevValue)) * partialTicks);

        return iRed << 16 | iGreen << 8 | iBlue;
    }

    public void setColor(int color) {
        ChannelSyncablePair red = this.getSyncablePair("red");
        ChannelSyncablePair green = this.getSyncablePair("green");
        ChannelSyncablePair blue = this.getSyncablePair("blue");

        if (red == null || green == null || blue == null)
            return;

        int iRed = (color & 0xFF0000) >> 16;
        int iGreen = (color & 0xFF00) >> 8;
        int iBlue = (color & 0xFF);

        red.prevValue = iRed;
        red.syncable.setValue(iRed);

        green.prevValue = iGreen;
        green.syncable.setValue(iGreen);

        blue.prevValue = iBlue;
        blue.syncable.setValue(iBlue);
    }

    public boolean hasLens() {
        ChannelSyncablePair pair = this.getSyncablePair("hasLens");
        return pair != null && ((SBoolean) pair.syncable).getValue();
    }

    public void setHasLens(boolean hasLens) {
        ChannelSyncablePair pair = this.getSyncablePair("direction");
        if (pair != null)
            pair.syncable.setValue(hasLens);
    }

    public float getPitch(float partialTicks) {
        ChannelSyncablePair pair = this.getSyncablePair("tilt");
        return pair != null ? (((Float) pair.prevValue) + (((SFloat) pair.syncable).getValue() - ((Float) pair.prevValue)) * partialTicks) : 0.0F;
    }

    public void setPitch(float pitch) {
        ChannelSyncablePair pair = this.getSyncablePair("tilt");
        if (pair == null)
            return;

        if (pitch > 0.8F) {
            pair.prevValue = 0.8F;
            pair.syncable.setValue(0.8F);
        }
        else if (pitch < -0.8F) {
            pair.prevValue = -0.8F;
            pair.syncable.setValue(-0.8F);
        }
        else {
            pair.prevValue = pitch;
            pair.syncable.setValue(pitch);
        }
    }

    public float getYaw(float partialTicks) {
        ChannelSyncablePair pair = this.getSyncablePair("pan");
        return pair != null ? (((Float) pair.prevValue) + (((SFloat) pair.syncable).getValue() - ((Float) pair.prevValue)) * partialTicks) : 0.0F;
    }

    public void setYaw(float yaw) {
        ChannelSyncablePair pair = this.getSyncablePair("pan");
        if (pair == null)
            return;

        pair.prevValue = yaw;
        pair.syncable.setValue(yaw);
    }

    public float getBrightness(float partialTicks) {
        ChannelSyncablePair pair = this.getSyncablePair("brightness");
        return pair != null ? (((Float) pair.prevValue) + (((SFloat) pair.syncable).getValue() - ((Float) pair.prevValue)) * partialTicks) : 0.0F;
    }

    public void setBrightness(float brightness) {
        ChannelSyncablePair pair = this.getSyncablePair("brightness");
        if (pair == null)
            return;
        if (brightness > 1.0F) {
            pair.prevValue = 1.0F;
            pair.syncable.setValue(1.0F);
        }
        else if (brightness < 0.0F) {
            pair.prevValue = 0.0F;
            pair.syncable.setValue(0.0F);
        }
        else {
            pair.prevValue = brightness;
            pair.syncable.setValue(brightness);
        }
    }

    public float getFocus(float partialTicks) {
        ChannelSyncablePair pair = this.getSyncablePair("focus");
        return pair != null ? (((Float) pair.prevValue) + (((SFloat) pair.syncable).getValue() - ((Float) pair.prevValue)) * partialTicks) : 0.0F;
    }

    public void setFocus(float focus) {
        ChannelSyncablePair pair = this.getSyncablePair("focus");
        if (pair == null)
            return;
        if (focus > 20.0F) {
            pair.prevValue = 20.0F;
            pair.syncable.setValue(20.0F);
        }
        else if (focus < 0.0F) {
            pair.prevValue = 0.0F;
            pair.syncable.setValue(0.0F);
        }
        else {
            pair.prevValue = focus;
            pair.syncable.setValue(focus);
        }
    }

    public void setLevelUnsafe(int id, int value) {
        this.setLevelUnsafe(Channels.getChannel(id), value);
    }

    public void setLevelUnsafe(Channels channel, int value) {
        if (channel == null && this.channels != null) {
            return;
        }
        for (ChannelLevel level : this.channels) {
            if (level.channel == channel) {
                level.value = value;
            }
        }
    }

    public void setLevel(int id, int value) {
        this.setLevel(Channels.getChannel(id), value);
    }

    public void setLevel(Channels channel, int value) {
        if (channel == null && this.channels != null) {
            return;
        }
        for (ChannelLevel level : this.channels) {
            if (level.channel == channel) {
                level.setValue(value);
            }
        }
    }

    public int getLevel(int id) {
        return this.getLevel(Channels.getChannel(id));
    }

    public int getLevel(Channels channel) {
        if (channel != null && this.channels != null) {
            for (ChannelLevel level : this.channels) {
                if (level.channel == channel) {
                    return level.getValue();
                }
            }
        }
        return 0;
    }

    public void setPort(int id, int port) {
        this.setPort(Channels.getChannel(id), port);
    }

    public void setPort(Channels channel, int port) {
        if (channel == null && this.channels != null) {
            return;
        }
        for (ChannelLevel level : this.channels) {
            if (level.channel == channel) {
                level.port = port;
            }
        }
    }

    public int getPort(int id) {
        return this.getPort(Channels.getChannel(id));
    }

    public int getPort(Channels channel) {
        if (channel != null && this.channels != null) {
            for (ChannelLevel level : this.channels) {
                if (level.channel == channel) {
                    return level.port;
                }
            }
        }
        return 0;
    }

    public void setChannel(int port, int value) {
        if (this.channels != null) {
            for (ChannelLevel level : this.channels) {
                if (level.port == port) {
                    level.setValue(value);

                    if (level.channel == Channels.STRENGTH) {
                        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList syncables = compound.getTagList("syncables", 10);
        for (int i = 0; i < syncables.tagCount(); i++) {
            NBTTagCompound pairCompound = syncables.getCompoundTagAt(i);
            ChannelSyncablePair pair = this.getSyncablePair(pairCompound.getString("name"));
            if (pair.syncable instanceof SInteger) {
                pair.syncable.setValue(pairCompound.getInteger("value"));
                pair.prevValue = pairCompound.getInteger("value");
            }
            else if (pair.syncable instanceof SBoolean) {
                pair.syncable.setValue(pairCompound.getBoolean("value"));
                pair.prevValue = pairCompound.getBoolean("value");
            }
            else if (pair.syncable instanceof SFloat) {
                pair.syncable.setValue(pairCompound.getFloat("value"));
                pair.prevValue = pairCompound.getFloat("value");
            }
            else if (pair.syncable instanceof SDouble) {
                pair.syncable.setValue(pairCompound.getDouble("value"));
                pair.prevValue = pairCompound.getDouble("value");
            }
            else if (pair.syncable instanceof SString) {
                pair.syncable.setValue(pairCompound.getString("value"));
                pair.prevValue = pairCompound.getString("value");
            }
        }

        if (compound.hasKey("blockId")) {
            this.setBlockType(compound.getInteger("blockId"));
        }
        else {
            this.setBlockType(compound.getString("block"));
        }
        this.blockMetadata = compound.getInteger("blockMetadata");

        if (this.channels == null) {
            this.setupChannels();
        }

        NBTTagList levels = compound.getTagList("levels", 10);
        for (int i = 0; i < levels.tagCount(); i++) {
            NBTTagCompound level = levels.getCompoundTagAt(i);
            int id = level.getInteger("id");
            int value = level.getInteger("value");
            int port = level.getInteger("port");
            this.setLevelUnsafe(id, value);
            this.setPort(id, port);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList syncables = new NBTTagList();
        for (ChannelSyncablePair pair : this.syncablePairs) {
            NBTTagCompound pairCompound = new NBTTagCompound();
            pairCompound.setString("name", pair.name);
            if (pair.syncable instanceof SInteger) {
                pairCompound.setInteger("value", ((SInteger) pair.syncable).getValue());
            }
            else if (pair.syncable instanceof SBoolean) {
                pairCompound.setBoolean("value", ((SBoolean) pair.syncable).getValue());
            }
            else if (pair.syncable instanceof SFloat) {
                pairCompound.setFloat("value", ((SFloat) pair.syncable).getValue());
            }
            else if (pair.syncable instanceof SDouble) {
                pairCompound.setDouble("value", ((SDouble) pair.syncable).getValue());
            }
            else if (pair.syncable instanceof SString) {
                pairCompound.setString("value", ((SString) pair.syncable).getValue());
            }
        }
        compound.setTag("syncables", syncables);

        compound.setString("block", Block.blockRegistry.getNameForObject(this.getBlockType()));
        compound.setInteger("blockMetadata", this.getBlockMetadata());

        NBTTagList levels = new NBTTagList();
        for (int i = 0; i < this.channels.size(); i++) {
            NBTTagCompound level = new NBTTagCompound();
            ChannelLevel channel = this.channels.get(i);
            level.setInteger("id", channel.channel.id);
            level.setInteger("value", channel.getValue());
            level.setInteger("port", channel.port);
            levels.appendTag(level);
        }
        compound.setTag("levels", levels);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("tracking", true);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager netManager, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(netManager, packet);

        if (packet.func_148857_g().hasKey("tracking", 1) && packet.func_148857_g().getBoolean("tracking")) {
            SpACore.packetHandler.sendPacketToServer(new Packet1TrackingStatus(this, true));
        }
    }

    @Override
    public void updateEntity() {
        ChannelSyncablePair pair = this.getSyncablePair("tilt");
        if (pair != null) {
            pair.prevValue = ((SFloat) pair.syncable).getValue();
        }

        pair = this.getSyncablePair("pan");
        if (pair != null) {
            pair.prevValue = ((SFloat) pair.syncable).getValue();
            if (((SFloat) pair.syncable).getValue() > 0.8F) {
                pair.prevValue = 0.8F;
                ((SFloat) pair.syncable).setValue(0.8F);
            }
            else if (((SFloat) pair.syncable).getValue() < -0.8F) {
                pair.prevValue = -0.8F;
                ((SFloat) pair.syncable).setValue(-0.8F);
            }
        }

        pair = this.getSyncablePair("brightness");
        if (pair != null) {
            pair.prevValue = ((SFloat) pair.syncable).getValue();
            if (((SFloat) pair.syncable).getValue() > 1.0F) {
                pair.prevValue = 1.0F;
                ((SFloat) pair.syncable).setValue(1.0F);
            }
            else if (((SFloat) pair.syncable).getValue() < 0.0F) {
                pair.prevValue = 0.0F;
                ((SFloat) pair.syncable).setValue(0.0F);
            }
        }

        pair = this.getSyncablePair("focus");
        if (pair != null) {
            pair.prevValue = ((SFloat) pair.syncable).getValue();
            if (((SFloat) pair.syncable).getValue() > 20.0F) {
                pair.prevValue = 20.0F;
                ((SFloat) pair.syncable).setValue(20.0F);
            }
            else if (((SFloat) pair.syncable).getValue() < 0.0F) {
                pair.prevValue = 0.0F;
                ((SFloat) pair.syncable).setValue(0.0F);
            }
        }

        pair = this.getSyncablePair("red");
        if (pair != null) {
            pair.prevValue = ((SInteger) pair.syncable).getValue();
        }

        pair = this.getSyncablePair("green");
        if (pair != null) {
            pair.prevValue = ((SInteger) pair.syncable).getValue();
        }

        pair = this.getSyncablePair("blue");
        if (pair != null) {
            pair.prevValue = ((SInteger) pair.syncable).getValue();
        }

        if (!this.worldObj.isRemote) {
            if (this.channels == null) {
                this.setupChannels();
            }
        }
    }

    @Override
    public Block getBlockType() {
        if (this.worldObj == null && this.blockType == null) {
            return null;
        }
        return super.getBlockType();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }

    public void setBlockType(int blockId) {
        this.blockType = (Block) Block.blockRegistry.getObjectById(blockId);
    }

    public void setBlockType(String block) {
        this.blockType = (Block) Block.blockRegistry.getObject(block);
    }

    public ILight getLight() {
        Block block = this.getBlockType();
        if (block != null && block instanceof BlockLight) {
            return ((BlockLight) block).getLight(this.getBlockMetadata());
        }
        return null;
    }

    public ILightRenderHandler getRenderHandler() {
        Block block = this.getBlockType();
        if (block != null && block instanceof BlockLight) {
            return ((BlockLight) block).getLightRenderer(this.getBlockMetadata());
        }
        return null;
    }

    public List<Channels> getChannels() {
        ILight light = this.getLight();
        return light == null ? null : light.getChannels();
    }

    public List<ChannelSyncablePair> createSyncablePairs() {
        ILight light = this.getLight();
        return light == null ? null : light.createSyncables(this);
    }

    // ISyncableObjectOwner

    @Override
    public boolean isNotValid() {
        return super.isInvalid();
    }

    @Override
    public List<ISyncable> getSyncables() {
        return this.syncables;
    }

    @Override
    public void readSetup(ByteArrayDataInput in) throws IOException {
        if (this.syncables == null) {
            this.setupChannels();
        }
        for (int i = 0; i < this.syncables.size(); i++) {
            ISyncable syncable = this.syncables.get(i);
            syncable.setId(in.readInt());
            syncable.read(in);
        }
    }

    @Override
    public void writeSetup(DataOutputStream out) throws IOException {
        for (int i = 0; i < this.syncables.size(); i++) {
            ISyncable syncable = this.syncables.get(i);
            out.writeInt(syncable.getId());
            syncable.write(out);
        }
    }

    @Override
    public String getIdentifier() {
        return "TileEntityLight_" + this.xCoord + ";" + this.yCoord + ";" + this.zCoord;
    }

    @Override
    public boolean isWorldBound() {
        return true;
    }

    @Override
    public World getWorld() {
        return this.getWorldObj();
    }

    @Override
    public int getPosX() {
        return this.xCoord;
    }

    @Override
    public int getPosY() {
        return this.yCoord;
    }

    @Override
    public int getPosZ() {
        return this.zCoord;
    }

    @Override
    public void onDataChanged(ISyncable syncable) {}

    public static class ChannelLevel {

        public Channels channel;
        public ISyncable syncable;
        protected int value;
        public int port;

        public float min = 0.0F;
        public float max = 1.0F;

        public ChannelLevel(Channels channel, ISyncable syncable) {
            this.channel = channel;
            this.syncable = syncable;
        }

        public void setValue(int value) {
            this.value = value;
            if (this.syncable instanceof SFloat) {
                float result = value * (this.max - this.min) / 255.0F + this.min;
                ((SFloat) this.syncable).setValue(result);
                return;
            }
            this.syncable.setValue(value);
        }

        public int getValue() {
            return this.value;
        }

    }

    // IPeripheral

    // FIXME
    // @formatter:off
    /*
    @Override
    public String getType() {
        return "DiscoTek.Light";
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "getChannel", "setChannel", "getSupportedChannels", "getChannelPort", "setChannelPort" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
        switch (method) {
        case 0: { // getChannel
            if (arguments.length != 1) {
                throw new Exception("Expected 1 parameter");
            }

            if (arguments[0] == null || !(arguments[0] instanceof Double || arguments[0] instanceof String)) {
                throw new Exception("Expected a Number or String as the first parameter");
            }

            Channels channel = null;
            if (arguments[0] instanceof Double) {
                channel = Channels.getChannel((int) ((Double) arguments[0]).doubleValue());
            }
            else {
                channel = Channels.getChannel((String) arguments[0]);
            }

            return new Object[] { this.getLevel(channel) };
        }
        case 1: { // setChannel
            if (arguments.length != 2) {
                throw new Exception("Expected 2 parameters");
            }

            if (arguments[0] == null || !(arguments[0] instanceof Double || arguments[0] instanceof String)) {
                throw new Exception("Expected a Number or String as the first parameter");
            }
            if (arguments[1] == null || !(arguments[1] instanceof Double)) {
                throw new Exception("Expected a Number as the second parameter");
            }

            Channels channel = null;
            if (arguments[0] instanceof Double) {
                channel = Channels.getChannel((int) ((Double) arguments[0]).doubleValue());
            }
            else {
                channel = Channels.getChannel((String) arguments[0]);
            }

            int level = (int) ((Double) arguments[1]).doubleValue();

            if (level < 0 || level > 255) {
                throw new Exception("Second parameter must be between 0 and 255");
            }

            this.setLevel(channel, level);

            return null;
        }
        case 2: { // getSupportedChannels
            if (arguments.length != 0) {
                throw new Exception("Expected 0 parameters");
            }

            Object[] result = new Object[this.channels.length * 2];
            for (int i = 0; i < this.channels.length; i++) {
                result[i * 2] = Double.valueOf(this.channels[i].channel.id);
                result[i * 2 + 1] = this.channels[i].channel.identifier;
            }

            return result;
        }
        case 3: { // getChannelPort
            if (arguments.length != 1) {
                throw new Exception("Expected 1 parameter");
            }

            if (arguments[0] == null || !(arguments[0] instanceof Double || arguments[0] instanceof String)) {
                throw new Exception("Expected a Number or String as the first parameter");
            }

            Channels channel = null;
            if (arguments[0] instanceof Double) {
                channel = Channels.getChannel((int) ((Double) arguments[0]).doubleValue());
            }
            else {
                channel = Channels.getChannel((String) arguments[0]);
            }

            return new Object[] { this.getPort(channel) };
        }
        case 4: { // setChannelPort
            if (arguments.length != 2) {
                throw new Exception("Expected 2 parameters");
            }

            if (arguments[0] == null || !(arguments[0] instanceof Double || arguments[0] instanceof String)) {
                throw new Exception("Expected a Number or String as the first parameter");
            }
            if (arguments[1] == null || !(arguments[1] instanceof Double)) {
                throw new Exception("Expected a Number as the second parameter");
            }

            Channels channel = null;
            if (arguments[0] instanceof Double) {
                channel = Channels.getChannel((int) ((Double) arguments[0]).doubleValue());
            }
            else {
                channel = Channels.getChannel((String) arguments[0]);
            }

            int port = (int) ((Double) arguments[1]).doubleValue();

            if (port < 0 || port > 255) {
                throw new Exception("Second parameter must be between 0 and 255");
            }

            this.setPort(channel, port);

            return null;
        }
        }
        return null;
    }

    @Override
    public boolean canAttachToSide(int side) {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer) {}

    @Override
    public void detach(IComputerAccess computer) {}
    */
    // @formatter:on

}
