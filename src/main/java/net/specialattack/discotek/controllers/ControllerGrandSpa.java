
package net.specialattack.discotek.controllers;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.Instruction;
import net.specialattack.discotek.client.gui.GuiControllerGrandSpA;
import net.specialattack.discotek.packet.Packet6GrandSpAGui;
import net.specialattack.discotek.packet.PacketHandler;
import net.specialattack.discotek.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ControllerGrandSpa implements IController {

    private Icon bottom;
    private Icon top;
    private Icon side;

    @Override
    public IControllerInstance createInstance(TileEntityController tile) {
        return new ControllerInstance(tile);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.top = register.registerIcon(Assets.DOMAIN + "controller-top1");
        this.bottom = register.registerIcon(Assets.DOMAIN + "controller-bottom1");
        this.side = register.registerIcon(Assets.DOMAIN + "controller-side1");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side) {
        if (side == 0) {
            return this.bottom;
        }
        else if (side == 1) {
            return this.top;
        }
        return this.side;
    }

    @Override
    public String getIdentifier() {
        return "grandspa";
    }

    public static class ControllerInstance implements IControllerInstance {

        public TileEntityController tile;

        public int[] stack = new int[16];
        public int stackPointer;
        public boolean interpretFirst;
        public Instruction[] instructions;
        public int instructionPointer;
        private boolean running;
        public String error;
        public int errorIndex;

        public ControllerInstance(TileEntityController tile) {
            this.tile = tile;
        }

        @Override
        public void doTick() {
            if (this.instructions == null) {
                this.instructions = new Instruction[50];
            }
            else if (this.instructions.length != 50) {
                Instruction[] temp = this.instructions;
                this.instructions = new Instruction[50];
                System.arraycopy(temp, 0, this.instructions, 0, temp.length < this.instructions.length ? temp.length : this.instructions.length);
            }

            try {
                if (this.running && this.instructions[this.instructionPointer] != null) {
                    Instruction instruction = this.instructions[this.instructionPointer];

                    if (instruction.identifier.equals("SLEEP")) { // Sleep for N ticks
                        if (this.interpretFirst) {
                            this.interpretFirst = false;
                            this.pushStack(instruction.argument);
                        }
                        int value = this.popStack();
                        value--;
                        if (value <= 0) {
                            this.next();
                        }
                        else {
                            this.pushStack(value);
                        }
                    }
                    else if (instruction.identifier.equals("PUSH")) { // Push N to the stack
                        this.pushStack(instruction.argument);
                        this.next();
                    }
                    else if (instruction.identifier.equals("POP")) { // Pop the stack
                        this.popStack();
                        this.next();
                    }
                    else if (instruction.identifier.equals("LEV")) { // Set channel to N
                        int channel = this.popStack();
                        int value = instruction.argument;
                        this.tile.transmitLevelChange(channel, value);
                        this.next();
                    }
                    else if (instruction.identifier.equals("LEV2")) { // Set 2 channels
                        int value2 = instruction.argument;
                        int channel2 = this.popStack();
                        int value1 = this.popStack();
                        int channel1 = this.popStack();
                        this.tile.transmitLevelChange(channel1, value1);
                        this.tile.transmitLevelChange(channel2, value2);
                        this.next();
                    }
                    else if (instruction.identifier.equals("LEV3")) { // Set 3 channels
                        int value3 = instruction.argument;
                        int channel3 = this.popStack();
                        int value2 = this.popStack();
                        int channel2 = this.popStack();
                        int value1 = this.popStack();
                        int channel1 = this.popStack();
                        this.tile.transmitLevelChange(channel1, value1);
                        this.tile.transmitLevelChange(channel2, value2);
                        this.tile.transmitLevelChange(channel3, value3);
                        this.next();
                    }
                    else if (instruction.identifier.equals("MOT")) { // Motion 1 channel
                        if (this.interpretFirst) {
                            this.interpretFirst = false;

                            int ticks = instruction.argument;
                            int value = this.popStack();
                            int channel = this.popStack();
                            this.pushStack(channel);
                            this.pushStack(value);
                            this.pushStack(0);//(tile.levels[channel]);
                            this.pushStack(ticks);
                        }
                        int ticks = this.popStack();
                        ticks--;

                        int start = this.popStack();
                        int value = this.popStack();
                        int channel = this.popStack();

                        int newValue = (value * (instruction.argument - ticks) + start * ticks) / instruction.argument;

                        this.tile.transmitLevelChange(channel, newValue);

                        if (ticks < 0) {
                            this.next();
                        }
                        else {
                            this.pushStack(channel);
                            this.pushStack(value);
                            this.pushStack(start);
                            this.pushStack(ticks);
                        }
                    }
                    else if (instruction.identifier.equals("MOT2")) { // Motion 2 channels
                        if (this.interpretFirst) {
                            this.interpretFirst = false;

                            int ticks = instruction.argument;
                            int value2 = this.popStack();
                            int channel2 = this.popStack();
                            int value1 = this.popStack();
                            int channel1 = this.popStack();
                            this.pushStack(channel1);
                            this.pushStack(value1);
                            this.pushStack(0);//(tile.levels[channel1]);
                            this.pushStack(channel2);
                            this.pushStack(value2);
                            this.pushStack(0);//(tile.levels[channel2]);
                            this.pushStack(ticks);
                        }
                        int ticks = this.popStack();
                        ticks--;

                        int start2 = this.popStack();
                        int value2 = this.popStack();
                        int channel2 = this.popStack();
                        int start1 = this.popStack();
                        int value1 = this.popStack();
                        int channel1 = this.popStack();

                        int newValue2 = (value2 * (instruction.argument - ticks) + start2 * ticks) / instruction.argument;
                        int newValue1 = (value1 * (instruction.argument - ticks) + start1 * ticks) / instruction.argument;

                        this.tile.transmitLevelChange(channel2, newValue2);
                        this.tile.transmitLevelChange(channel1, newValue1);

                        if (ticks <= 0) {
                            this.next();
                        }
                        else {
                            this.pushStack(channel1);
                            this.pushStack(value1);
                            this.pushStack(start1);
                            this.pushStack(channel2);
                            this.pushStack(value2);
                            this.pushStack(start2);
                            this.pushStack(ticks);
                        }
                    }
                    else if (instruction.identifier.equals("MOT3")) { // Motion 3 channels
                        if (this.interpretFirst) {
                            this.interpretFirst = false;

                            int ticks = instruction.argument;
                            int value3 = this.popStack();
                            int channel3 = this.popStack();
                            int value2 = this.popStack();
                            int channel2 = this.popStack();
                            int value1 = this.popStack();
                            int channel1 = this.popStack();
                            this.pushStack(channel1);
                            this.pushStack(value1);
                            this.pushStack(0);//(tile.levels[channel1]);
                            this.pushStack(channel2);
                            this.pushStack(value2);
                            this.pushStack(0);//(tile.levels[channel2]);
                            this.pushStack(channel3);
                            this.pushStack(value3);
                            this.pushStack(0);//(tile.levels[channel3]);
                            this.pushStack(ticks);
                        }
                        int ticks = this.popStack();
                        ticks--;

                        int start3 = this.popStack();
                        int value3 = this.popStack();
                        int channel3 = this.popStack();
                        int start2 = this.popStack();
                        int value2 = this.popStack();
                        int channel2 = this.popStack();
                        int start1 = this.popStack();
                        int value1 = this.popStack();
                        int channel1 = this.popStack();

                        int newValue3 = (value3 * (instruction.argument - ticks) + start3 * ticks) / instruction.argument;
                        int newValue2 = (value2 * (instruction.argument - ticks) + start2 * ticks) / instruction.argument;
                        int newValue1 = (value1 * (instruction.argument - ticks) + start1 * ticks) / instruction.argument;

                        this.tile.transmitLevelChange(channel3, newValue3);
                        this.tile.transmitLevelChange(channel2, newValue2);
                        this.tile.transmitLevelChange(channel1, newValue1);

                        if (ticks <= 0) {
                            this.next();
                        }
                        else {
                            this.pushStack(channel1);
                            this.pushStack(value1);
                            this.pushStack(start1);
                            this.pushStack(channel2);
                            this.pushStack(value2);
                            this.pushStack(start2);
                            this.pushStack(channel3);
                            this.pushStack(value3);
                            this.pushStack(start3);
                            this.pushStack(ticks);
                        }
                    }
                    else if (instruction.identifier.equals("GOTO")) { // Go to instruction at index N
                        this.changeTo(instruction.argument - 1);
                    }
                    else if (instruction.identifier.equals("CLEAR")) { // Clear the stack
                        this.stackPointer = 0;
                        this.next();
                    }
                    else {
                        throw new ControllerException("gui.controller.unknowninstruction", this.instructionPointer + 1);
                    }
                }
                else {
                    this.next();
                }
            }
            catch (ControllerException e) {
                this.running = false;
                this.error = e.getMessage();
                this.errorIndex = e.index;
                System.err.println(StatCollector.translateToLocalFormatted(e.getMessage(), e.index));
            }
            catch (Throwable e) {
                this.running = false;
                this.error = e.getMessage();
                System.err.println(StatCollector.translateToLocal(e.getMessage()));
            }

        }

        @Override
        public void writeToNBT(NBTTagCompound compound) {
            compound.setInteger("Pointer", this.instructionPointer);
            NBTTagList instructions = new NBTTagList();
            for (Instruction instruction : this.instructions) {
                if (instruction == null) {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setString("Identifier", "NOOP");
                    tag.setInteger("Argument", 0);
                    instructions.appendTag(tag);
                    continue;
                }
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("Identifier", instruction.identifier);
                tag.setInteger("Argument", instruction.argument);
                instructions.appendTag(tag);
            }
            compound.setTag("Instructions", instructions);
            compound.setIntArray("Stack", this.stack);
            compound.setInteger("StackPointer", this.stackPointer);
            compound.setBoolean("InterpretFirst", this.interpretFirst);
            compound.setBoolean("Running", this.running);
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            this.instructionPointer = compound.getInteger("Pointer");
            NBTTagList instructions = compound.getTagList("Instructions");
            this.instructions = new Instruction[instructions.tagCount()];
            for (int i = 0; i < instructions.tagCount(); i++) {
                NBTTagCompound tag = (NBTTagCompound) instructions.tagAt(i);
                Instruction instruction = new Instruction();
                instruction.identifier = tag.getString("Identifier");
                instruction.argument = tag.getInteger("Argument");
                if (!instruction.identifier.equals("NOOP")) {
                    this.instructions[i] = instruction;
                }
            }
            this.stack = compound.getIntArray("Stack");
            this.stackPointer = compound.getInteger("StackPointer");
            this.interpretFirst = compound.getBoolean("Interpret");
            this.running = compound.getBoolean("Running");
        }

        @Override
        public boolean doesTick() {
            return true;
        }

        @Override
        public void openGui(EntityPlayer player, Side side) {
            if (side == Side.CLIENT) {
                FMLClientHandler.instance().displayGuiScreen(player, new GuiControllerGrandSpA(this));
            }
            else {
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(PacketHandler.instance.createPacket(new Packet6GrandSpAGui(this)));
            }
        }

        @Override
        public void prepareServer() {}

        public void changeTo(int index) {
            this.instructionPointer = index;
            if (this.instructionPointer < 0 || this.instructionPointer >= this.instructions.length) {
                this.instructionPointer = 0;
            }
            this.interpretFirst = true;
        }

        public void next() {
            this.instructionPointer++;
            if (this.instructionPointer >= this.instructions.length) {
                this.instructionPointer = 0;
            }
            this.interpretFirst = true;
        }

        public void pushStack(int value) throws ControllerException {
            this.stackPointer++;
            if (this.stackPointer >= this.stack.length) {
                throw new ControllerException("gui.controller.stackoverflow", this.instructionPointer);
            }
            this.stack[this.stackPointer] = value;
        }

        public int popStack() throws ControllerException {
            this.stackPointer--;
            if (this.stackPointer < 0) {
                throw new ControllerException("gui.controller.stackunderflow", this.instructionPointer);
            }
            return this.stack[this.stackPointer + 1];
        }

        public void startStop() {
            this.running = !this.running;
            if (this.running) {
                this.interpretFirst = true;
                this.stackPointer = 0;
                this.stack = new int[16];
                this.instructionPointer = 0;
                this.error = null;
                this.errorIndex = 0;
            }
        }

    }

    public static class ControllerException extends Exception {

        private static final long serialVersionUID = 7486507998690757872L;
        public final int index;

        public ControllerException(String message, int index) {
            super(message);
            this.index = index;
        }

    }

}
