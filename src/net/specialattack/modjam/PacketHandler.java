
package net.specialattack.modjam;

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
import net.specialattack.modjam.tileentity.TileEntityLight;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);

        int id = in.readUnsignedByte();

        switch (id) {
        case 1: {
            TileEntityLight tile = (TileEntityLight) ((EntityPlayer) player).worldObj.getBlockTileEntity(in.readInt(), in.readInt(), in.readInt());
            tile.setColor(in.readInt());
            tile.setHasLens(in.readBoolean());
            tile.setPitch(in.readFloat());
            tile.setYaw(in.readFloat());
            tile.setBrightness(in.readFloat());
            tile.setFocus(in.readFloat());
        }
        break;
        case 2: {
            TileEntityLight tile = (TileEntityLight) ((EntityPlayer) player).worldObj.getBlockTileEntity(in.readInt(), in.readInt(), in.readInt());
            int count = in.readUnsignedByte();
            for (int i = 0; i < count; i++) {
                int type = in.readUnsignedByte();
                if (type == 0) {
                    tile.setColor(in.readInt());
                }
                else if (type == 1) {
                    tile.setHasLens(in.readBoolean());
                }
                else {
                    tile.setValue(type, in.readFloat());
                }
            }
        }
        break;
        }
    }

    public static Packet250CustomPayload createPacket(int id, Object... data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(32767);
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeByte(id);

            switch (id) {
            case 1: { // Send light info
                TileEntityLight tile = (TileEntityLight) data[0];
                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);
                dos.writeInt(tile.getColor());
                dos.writeBoolean(tile.hasLens());
                dos.writeFloat(tile.getPitch(0.0F));
                dos.writeFloat(tile.getYaw(0.0F));
                dos.writeFloat(tile.getBrightness(0.0F));
                dos.writeFloat(tile.getFocus(0.0F));
            }
            break;
            case 2: { // Sync value
                TileEntityLight tile = (TileEntityLight) data[0];
                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);

                int[] types = (int[]) data[1];
                dos.writeByte(types.length);
                for (int i = 0; i < types.length; i++) {
                    int type = types[i];
                    dos.writeByte(type);
                    if (type == 0) {
                        dos.writeInt(tile.getColor());
                    }
                    else if (type == 1) {
                        dos.writeBoolean(tile.hasLens());
                    }
                    else {
                        dos.writeFloat(tile.getValue(type));
                    }
                }
            }
            break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = Constants.MOD_CHANNEL;
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
