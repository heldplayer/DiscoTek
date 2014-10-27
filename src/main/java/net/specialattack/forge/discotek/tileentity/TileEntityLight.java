package net.specialattack.forge.discotek.tileentity;

import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.DataOutputStream;
import java.io.IOException;
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
import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.core.sync.packet.Packet1TrackingStatus;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.client.ClientProxy;
import net.specialattack.forge.discotek.client.renderer.light.ILightRenderHandler;
import net.specialattack.forge.discotek.light.Channels;
import net.specialattack.forge.discotek.light.ILight;
import net.specialattack.forge.discotek.light.instance.ILightInstance;

@Interface(modid = "ComputerCraft", iface = "dan200.computer.api.IPeripheral")
public class TileEntityLight extends TileEntity implements ISyncableObjectOwner { //, IPeripheral {

    public ChannelLevel[] channels;

    private ILightInstance light;

    public TileEntityLight() {
    }

    public TileEntityLight(Block blockType, int meta, boolean setupChannels) {
        this();
        this.blockMetadata = meta;
        this.blockType = blockType;
        if (setupChannels) {
            this.setupChannels();
        }
    }

    public void setBlockType(int blockId) {
        this.blockType = (Block) Block.blockRegistry.getObjectById(blockId);
    }

    public void setBlockType(Block block) {
        this.blockType = block;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("blockId")) {
            this.setBlockType(compound.getInteger("blockId"));
        } else {
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

        NBTTagCompound light = compound.getCompoundTag("light");

        if (light != null) {
            ILightInstance instance = this.getLightInstance();
            if (instance != null) {
                instance.readFromNBT(light);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setString("block", Block.blockRegistry.getNameForObject(this.getBlockType()));
        compound.setInteger("blockMetadata", this.getBlockMetadata());

        NBTTagList levels = new NBTTagList();
        for (ChannelLevel channel1 : this.channels) {
            NBTTagCompound level = new NBTTagCompound();
            level.setInteger("id", channel1.channel.id);
            level.setInteger("value", channel1.getValue());
            level.setInteger("port", channel1.port);
            levels.appendTag(level);
        }
        compound.setTag("levels", levels);

        ILightInstance instance = this.getLightInstance();
        if (instance != null) {
            NBTTagCompound light = new NBTTagCompound();
            instance.writeToNBT(light);
            compound.setTag("light", light);
        }
    }

    public ILightInstance getLightInstance() {
        if (this.light == null) {
            ILight light = this.getLight();
            if (light != null) {
                this.light = light.createInstance(this);
                this.markDirty();
            }
        }
        return this.light;
    }

    public ILight getLight() {
        Block block = this.getBlockType();
        if (block != null && block instanceof BlockLight) {
            return ((BlockLight) block).getLight(this.getBlockMetadata());
        }
        return null;
    }

    public void setLightInstance(ILightInstance instance) {
        this.light = instance;
    }

    @Override
    public void updateEntity() {
        if (!this.worldObj.isRemote) {
            if (this.channels == null) {
                this.setupChannels();
            }
        }

        if (this.light != null) {
            this.light.doTick();
        }
    }

    @Override
    public Block getBlockType() {
        if (this.worldObj == null && this.blockType == null) {
            return null;
        }
        return super.getBlockType();
    }

    public void setBlockType(String block) {
        this.blockType = (Block) Block.blockRegistry.getObject(block);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("tracking", true);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, compound);
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if (this.worldObj != null && this.worldObj.isRemote) {
            ClientProxy.addTile(this);
        }
    }

    @Override
    public void validate() {
        super.validate();

        if (this.worldObj != null && this.worldObj.isRemote) {
            ClientProxy.addTile(this);
        }
    }

    @Override
    public void onDataPacket(NetworkManager netManager, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(netManager, packet);

        if (packet.func_148857_g().hasKey("tracking", 1) && packet.func_148857_g().getBoolean("tracking")) {
            SpACore.syncPacketHandler.sendPacketToServer(new Packet1TrackingStatus(this, true));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }

    public void setupChannels() {
        List<Channels> channels = this.getChannels();
        if (channels == null) {
            return;
        }

        this.channels = new ChannelLevel[channels.size()];

        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return;
        }

        for (int i = 0; i < this.channels.length; i++) {
            Channels channel = channels.get(i);
            ChannelLevel level = this.channels[i] = new ChannelLevel(channel, null);
            level.syncable = instance.getSyncable(channel.identifier);
            level.min = channel.min;
            level.max = channel.max;
        }
        this.markDirty();
    }

    public void setValue(String identifier, String value) {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return;
        }

        instance.setValue(identifier, value);
    }

    public void setValue(String identifier, float value) {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return;
        }

        instance.setValue(identifier, value);
    }

    public void setValue(String identifier, int value) {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return;
        }

        instance.setValue(identifier, value);
    }

    public void setValue(String identifier, boolean value) {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return;
        }

        instance.setValue(identifier, value);
    }

    public String getString(String identifier, float partialTicks) {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return "";
        }

        return instance.getString(identifier, partialTicks);
    }

    public float getFloat(String identifier, float partialTicks) {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return 0.0F;
        }

        return instance.getFloat(identifier, partialTicks);
    }

    public int getInteger(String identifier, float partialTicks) {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            if (identifier.equals("direction")) {
                return 1;
            }
            return 0;
        }

        return instance.getInteger(identifier, partialTicks);
    }

    public boolean getBoolean(String identifier, float partialTicks) {
        ILightInstance instance = this.getLightInstance();
        return instance != null && instance.getBoolean(identifier, partialTicks);

    }

    public void setLevelUnsafe(int id, int value) {
        this.setLevelUnsafe(Channels.getChannel(id), value);
    }

    public void setLevelUnsafe(Channels channel, int value) {
        if (channel == null || this.channels == null) {
            return;
        }
        for (ChannelLevel level : this.channels) {
            if (level.channel == channel) {
                level.value = value;
            }
        }
        this.markDirty();
    }

    public void setLevel(int id, int value) {
        this.setLevel(Channels.getChannel(id), value);
    }

    public void setLevel(Channels channel, int value) {
        if (channel == null || this.channels == null) {
            return;
        }
        for (ChannelLevel level : this.channels) {
            if (level.channel == channel) {
                level.setValue(value);
            }
        }
        this.markDirty();
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
        if (channel == null || this.channels == null) {
            return;
        }
        for (ChannelLevel level : this.channels) {
            if (level.channel == channel) {
                level.port = port;
            }
        }
        this.markDirty();
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

                    if (level.channel == Channels.REDSTONE) {
                        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
                    }
                }
            }
            this.markDirty();
        }
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
        return this.isInvalid();
    }

    @Override
    public void setNotValid() {
        // Not supported
    }

    @Override
    public List<ISyncable> getSyncables() {
        ILightInstance instance = this.getLightInstance();
        if (instance != null) {
            return instance.getSyncables();
        }
        return null;
    }

    @Override
    public void readSetup(ByteArrayDataInput in) throws IOException {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return;
        }
        List<ISyncable> syncables = instance.getSyncables();
        for (ISyncable syncable : syncables) {
            syncable.setId(in.readInt());
            syncable.read(in);
        }
    }

    @Override
    public void writeSetup(DataOutputStream out) throws IOException {
        ILightInstance instance = this.getLightInstance();
        if (instance == null) {
            return;
        }
        List<ISyncable> syncables = instance.getSyncables();
        for (ISyncable syncable : syncables) {
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
    public void onDataChanged(ISyncable syncable) {
    }

    public static class ChannelLevel {

        public Channels channel;
        public ISyncable syncable;
        public int port;
        public float min = 0.0F;
        public float max = 1.0F;
        protected int value;

        public ChannelLevel(Channels channel, ISyncable syncable) {
            this.channel = channel;
            this.syncable = syncable;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            if (value instanceof Number) {
                this.value = ((Number) value).intValue();
                if (this.syncable instanceof SFloat) {
                    float result = ((Number) value).intValue() * (this.max - this.min) / 255.0F + this.min;
                    ((SFloat) this.syncable).setValue(result);
                    return;
                }
            }
            this.syncable.setValue(value);
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
