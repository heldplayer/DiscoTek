
package net.specialattack.discotek;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInstance;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.specialattack.discotek.tileentity.TileEntityController;
import net.specialattack.discotek.tileentity.TileEntitySpAGuo;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

@Deprecated
public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerObj) {
        ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);

        EntityPlayer player = ((EntityPlayer) playerObj);

        int id = in.readUnsignedByte();

        switch (id) {
        case 5: {
            TileEntityController tile = (TileEntityController) player.worldObj.getBlockTileEntity(in.readInt(), in.readInt(), in.readInt());
            if (tile != null) {
                if (tile.getBlockMetadata() == 0) {
                    int count = in.readInt();
                    tile.levels = new int[count];
                    for (int i = 0; i < count; i++) {
                        tile.levels[i] = in.readInt();
                    }
                }
                else if (tile.getBlockMetadata() == 1) {
                    int errorLength = in.readInt();
                    if (errorLength > 0) {
                        byte[] error = new byte[errorLength];
                        in.readFully(error);
                        tile.error = new String(error);
                        tile.errorIndex = in.readInt();
                    }
                    else {
                        tile.error = null;
                    }

                    int count = in.readInt();
                    tile.instructions = new Instruction[count];
                    for (int i = 0; i < count; i++) {
                        int length = in.readUnsignedByte();
                        if (length == 0) {
                            continue;
                        }

                        tile.instructions[i] = new Instruction();

                        byte[] data = new byte[length];
                        in.readFully(data);
                        tile.instructions[i].identifier = new String(data);
                        tile.instructions[i].argument = in.readUnsignedByte();
                    }
                }
                tile.onInventoryChanged();
            }
        }
        break;
        case 6: {
            TileEntityController tile = (TileEntityController) player.worldObj.getBlockTileEntity(in.readInt(), in.readInt(), in.readInt());
            if (tile != null) {
                int count = in.readInt();
                for (int i = 0; i < count; i++) {
                    tile.levels[i] = in.readUnsignedByte();
                }
                tile.onInventoryChanged();
                tile.updateDmxNetwork();
            }
        }
        break;
        }
    }

    @Deprecated
    public static Packet250CustomPayload createPacket(int id, Object... data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(32767);
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeByte(id);

            switch (id) {
            case 5: { // Controller instructions
                TileEntityController tile = (TileEntityController) data[0];
                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);
                if (tile.getBlockMetadata() == 0) {
                    dos.writeInt(tile.levels.length);
                    for (int i = 0; i < tile.levels.length; i++) {
                        dos.writeInt(tile.levels[i]);
                    }
                }
                else if (tile.getBlockMetadata() == 1) {
                    if (tile.error == null) {
                        dos.writeInt(0);
                    }
                    else {
                        dos.writeInt(tile.error.length());
                        dos.writeBytes(tile.error);
                        dos.writeInt(tile.errorIndex);
                    }
                    dos.writeInt(tile.instructions.length);
                    for (int i = 0; i < tile.instructions.length; i++) {
                        Instruction instruction = tile.instructions[i];
                        if (instruction == null) {
                            dos.writeByte(0);
                        }
                        else {
                            dos.writeByte(instruction.identifier.length());
                            dos.writeBytes(instruction.identifier);
                            dos.writeByte(instruction.argument);
                        }
                    }
                }
                else if (tile.getBlockMetadata() == 2) {
                    TileEntitySpAGuo guo = (TileEntitySpAGuo) data[0];
                    for (int i = 0; i < guo.messages.length; i++) {
                        System.out.println(guo.messages[i]);
                    }
                }
            }
            break;
            case 6: { // Controller levels
                TileEntityController tile = (TileEntityController) data[0];
                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);
                dos.writeInt(tile.levels.length);
                for (int i = 0; i < tile.levels.length; i++) {
                    dos.writeByte(tile.levels[i]);
                }
            }
            break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = Objects.MOD_CHANNEL;
        packet.data = bos.toByteArray();
        packet.length = packet.data.length;
        return packet;
    }

    public static void sendPacketToPlayersWatchingBlock(Packet packet, World world, int x, int z) {
        if (packet == null) {
            return;
        }

        Chunk chunk = world.getChunkFromBlockCoords(x, z);

        sendPacketToPlayersWatchingChunk(packet, world.provider.dimensionId, chunk.xPosition, chunk.zPosition);
    }

    public static void sendPacketToPlayersWatchingChunk(Packet packet, int dimensionId, int chunkX, int chunkZ) {
        if (packet == null) {
            return;
        }

        MinecraftServer server = MinecraftServer.getServer();

        if (server != null) {
            for (WorldServer world : server.worldServers) {
                if (world.provider.dimensionId == dimensionId) {
                    PlayerManager manager = world.getPlayerManager();
                    PlayerInstance instance = manager.getOrCreateChunkWatcher(chunkX, chunkZ, false);

                    if (instance != null) {
                        instance.sendToAllPlayersWatchingChunk(packet);
                    }
                }
            }
        }
    }

}
