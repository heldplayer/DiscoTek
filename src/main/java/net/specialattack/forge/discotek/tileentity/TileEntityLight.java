
package net.specialattack.forge.discotek.tileentity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.core.sync.SInteger;
import net.specialattack.forge.core.sync.packet.Packet1TrackingStatus;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.client.ClientProxy;
import net.specialattack.forge.discotek.lights.Channels;
import net.specialattack.forge.discotek.lights.ILight;
import net.specialattack.forge.discotek.lights.ILightRenderHandler;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Interface(modid = "ComputerCraft", iface = "dan200.computer.api.IPeripheral")
public class TileEntityLight extends TileEntity implements ISyncableObjectOwner { //, IPeripheral {

    private SBoolean hasLens;
    private SInteger direction;

    private SFloat pitch;
    private SFloat yaw;
    private SFloat brightness;
    private SFloat focus;
    private SInteger red;
    private SInteger green;
    private SInteger blue;
    private float prevPitch = 0.0F;
    private float prevYaw = 0.0F;
    private float prevBrightness = 1.0F;
    private float prevFocus = 1.0F;
    private int prevRed = 0xFF;
    private int prevGreen = 0xFF;
    private int prevBlue = 0xFF;

    private List<ISyncable> syncables;

    public ChannelLevel[] channels;

    public TileEntityLight() {
        this.hasLens = new SBoolean(this, true);
        this.pitch = new SFloat(this, 0.0F);
        this.yaw = new SFloat(this, 0.0F);
        this.brightness = new SFloat(this, 1.0F);
        this.focus = new SFloat(this, 1.0F);
        this.direction = new SInteger(this, 0);
        this.red = new SInteger(this, 0xFF);
        this.green = new SInteger(this, 0xFF);
        this.blue = new SInteger(this, 0xFF);
        this.syncables = Arrays.asList((ISyncable) this.hasLens, this.pitch, this.yaw, this.brightness, this.focus, this.direction, this.red, this.green, this.blue);
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
        List<Channels> channels = this.getChannels();
        if (channels == null) {
            return;
        }
        this.channels = new ChannelLevel[channels.size()];
        for (int i = 0; i < this.channels.length; i++) {
            Channels channel = channels.get(i);
            this.channels[i] = new ChannelLevel(channel, null);
            switch (channel.id) {
            case 0:
            case 7:
                this.channels[i].syncable = this.brightness;
            break;
            case 1:
                this.channels[i].syncable = this.pitch;
                this.channels[i].min = -0.8F;
                this.channels[i].max = 0.8F;
            break;
            case 2:
                this.channels[i].syncable = this.yaw;
                this.channels[i].max = (float) Math.PI * 4.0F;
            break;
            case 3:
                this.channels[i].syncable = this.focus;
                this.channels[i].max = 20.0F;
            break;
            case 4:
                this.channels[i].syncable = this.red;
            break;
            case 5:
                this.channels[i].syncable = this.green;
            break;
            case 6:
                this.channels[i].syncable = this.blue;
            break;
            }
        }
    }

    public void setDirection(int side) {
        this.direction.setValue(side);
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

    public int getDirection() {
        return this.direction.getValue();
    }

    public int getColor(float partialTicks) {
        int red = (int) (this.prevRed + (this.red.getValue() - this.prevRed) * partialTicks);
        int green = (int) (this.prevGreen + (this.green.getValue() - this.prevGreen) * partialTicks);
        int blue = (int) (this.prevBlue + (this.blue.getValue() - this.prevBlue) * partialTicks);
        return red << 16 | green << 8 | blue;
    }

    public void setColor(int color) {
        int red = (color & 0xFF0000) >> 16;
        int green = (color & 0xFF00) >> 8;
        int blue = (color & 0xFF);
        this.prevRed = red;
        this.prevGreen = green;
        this.prevBlue = blue;
        this.red.setValue(red);
        this.green.setValue(green);
        this.blue.setValue(blue);
    }

    public boolean hasLens() {
        return this.hasLens.getValue();
    }

    public void setHasLens(boolean hasLens) {
        this.hasLens.setValue(hasLens);
    }

    public float getPitch(float partialTicks) {
        return this.prevPitch + (this.pitch.getValue() - this.prevPitch) * partialTicks;
    }

    public void setPitch(float pitch) {
        if (pitch > 0.8F) {
            this.prevPitch = 0.8F;
            this.pitch.setValue(0.8F);
        }
        else if (pitch < -0.8F) {
            this.prevPitch = -0.8F;
            this.pitch.setValue(-0.8F);
        }
        else {
            this.prevPitch = pitch;
            this.pitch.setValue(pitch);
        }
    }

    public float getYaw(float partialTicks) {
        return this.prevYaw + (this.yaw.getValue() - this.prevYaw) * partialTicks;
    }

    public void setYaw(float yaw) {
        this.prevYaw = yaw;
        this.yaw.setValue(yaw);
    }

    public float getBrightness(float partialTicks) {
        return this.prevBrightness + (this.brightness.getValue() - this.prevBrightness) * partialTicks;
    }

    public void setBrightness(float brightness) {
        if (brightness > 1.0F) {
            this.prevBrightness = 1.0F;
            this.brightness.setValue(1.0F);
        }
        else if (brightness < 0.0F) {
            this.prevBrightness = 0.0F;
            this.brightness.setValue(0.0F);
        }
        else {
            this.prevBrightness = brightness;
            this.brightness.setValue(brightness);
        }
    }

    public float getFocus(float partialTicks) {
        return this.prevFocus + (this.focus.getValue() - this.prevFocus) * partialTicks;
    }

    public void setFocus(float focus) {
        if (focus > 20.0F) {
            this.prevFocus = 20.0F;
            this.focus.setValue(20.0F);
        }
        else if (focus < 0.0F) {
            this.prevFocus = 0.0F;
            this.focus.setValue(0.0F);
        }
        else {
            this.prevFocus = focus;
            this.focus.setValue(focus);
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
        this.red.setValue(compound.getInteger("red"));
        this.green.setValue(compound.getInteger("green"));
        this.blue.setValue(compound.getInteger("blue"));
        this.hasLens.setValue(compound.getBoolean("hasLens"));
        this.pitch.setValue(compound.getFloat("pitch"));
        this.prevPitch = this.pitch.getValue();
        this.yaw.setValue(compound.getFloat("yaw"));
        this.prevYaw = this.yaw.getValue();
        this.brightness.setValue(compound.getFloat("brightness"));
        this.prevBrightness = this.brightness.getValue();
        this.focus.setValue(compound.getFloat("focus"));
        this.prevFocus = this.focus.getValue();
        this.direction.setValue(compound.getInteger("direction"));

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
        compound.setInteger("red", this.red.getValue());
        compound.setInteger("green", this.green.getValue());
        compound.setInteger("blue", this.blue.getValue());
        compound.setBoolean("hasLens", this.hasLens.getValue());
        compound.setFloat("pitch", this.pitch.getValue());
        compound.setFloat("yaw", this.yaw.getValue());
        compound.setFloat("brightness", this.brightness.getValue());
        compound.setFloat("focus", this.focus.getValue());
        compound.setInteger("direction", this.direction.getValue());

        compound.setString("block", Block.blockRegistry.getNameForObject(this.getBlockType()));
        compound.setInteger("blockMetadata", this.getBlockMetadata());

        NBTTagList levels = new NBTTagList();
        for (int i = 0; i < this.channels.length; i++) {
            NBTTagCompound level = new NBTTagCompound();
            ChannelLevel channel = this.channels[i];
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
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, compound);
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
        this.prevPitch = this.pitch.getValue();
        this.prevYaw = this.yaw.getValue();
        this.prevBrightness = this.brightness.getValue();
        this.prevFocus = this.focus.getValue();
        this.prevRed = this.red.getValue();
        this.prevGreen = this.green.getValue();
        this.prevBlue = this.blue.getValue();

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
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
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
                float result = (float) value * (this.max - this.min) / 255.0F + this.min;
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
