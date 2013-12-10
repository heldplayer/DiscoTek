
package net.specialattack.discotek.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.specialattack.discotek.block.BlockController;
import net.specialattack.discotek.controllers.IController;
import net.specialattack.discotek.controllers.IControllerInstance;

public class TileEntityController extends TileEntity {

    private List<TileEntityLight> lightsLinked = new ArrayList<TileEntityLight>();
    private NBTTagList lightsTag;

    private IControllerInstance controller;

    public TileEntityController() {}

    public TileEntityController(Block blockType, int meta, boolean setupServer) {
        this();
        this.blockMetadata = meta;
        this.blockType = blockType;
        this.getControllerInstance();
        if (setupServer) {
            this.controller.prepareServer();
        }
    }

    public IController getController() {
        Block block = this.getBlockType();
        if (block != null && block instanceof BlockController) {
            return ((BlockController) block).getController(this.getBlockMetadata());
        }
        return null;
    }

    public IControllerInstance getControllerInstance() {
        if (this.controller == null) {
            this.controller = this.getController().createInstance(this);
        }
        return this.controller;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.lightsTag = compound.getTagList("Lights");

        this.setBlockType(compound.getInteger("blockId"));
        this.blockMetadata = compound.getInteger("blockMetadata");

        if (this.controller == null) {
            this.getControllerInstance();
        }

        NBTTagCompound controller = compound.getCompoundTag("controller");
        if (controller != null && this.controller != null) {
            this.controller.readFromNBT(controller);
            this.controller.prepareServer();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList lightsLinked = new NBTTagList();
        for (TileEntityLight light : this.lightsLinked) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("x", light.xCoord);
            tag.setInteger("y", light.yCoord);
            tag.setInteger("z", light.zCoord);
            lightsLinked.appendTag(tag);
        }
        compound.setTag("Lights", lightsLinked);

        compound.setInteger("blockId", this.getBlockType().blockID);
        compound.setInteger("blockMetadata", this.getBlockMetadata());

        if (this.controller == null) {
            this.getControllerInstance();
        }

        if (this.controller != null) {
            NBTTagCompound controller = new NBTTagCompound("controller");
            this.controller.writeToNBT(controller);
            compound.setCompoundTag("controller", controller);
        }
    }

    @Override
    public void updateEntity() {
        if (this.lightsTag != null) {
            for (int i = 0; i < this.lightsTag.tagCount(); i++) {
                NBTTagCompound tag = (NBTTagCompound) this.lightsTag.tagAt(i);
                int x = tag.getInteger("x");
                int y = tag.getInteger("y");
                int z = tag.getInteger("z");
                TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);
                if (tile != null && tile instanceof TileEntityLight) {
                    this.link((TileEntityLight) this.worldObj.getBlockTileEntity(x, y, z));
                }
            }

            this.lightsTag = null;
        }

        if (this.controller == null) {
            this.getControllerInstance();
        }
        if (!this.worldObj.isRemote) {
            if (this.controller != null && this.controller.doesTick()) {
                this.controller.doTick();
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

    public void setBlockType(int blockId) {
        this.blockType = Block.blocksList[blockId];
    }

    public boolean link(TileEntityLight light) {
        if (this.lightsLinked.contains(light)) {
            return false;
        }

        double reach = 32.0D * 32.0D;
        double distance = (light.xCoord - this.xCoord) * (light.xCoord - this.xCoord);
        distance += (light.yCoord - this.yCoord) * (light.yCoord - this.yCoord);
        distance += (light.zCoord - this.zCoord) * (light.zCoord - this.zCoord);

        if (distance < reach) {
            this.lightsLinked.add(light);

            return true;
        }

        return false;
    }

    public void transmitLevelChange(int channel, int value) {
        for (TileEntityLight light : this.lightsLinked) {
            light.setChannel(channel, value);
        }
    }

}
