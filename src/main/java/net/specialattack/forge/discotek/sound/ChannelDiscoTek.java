package net.specialattack.forge.discotek.sound;

import net.minecraft.client.gui.FontRenderer;
import net.specialattack.forge.core.client.MC;
import net.specialattack.forge.discotek.Objects;
import net.specialattack.forge.discotek.client.ClientProxy;
import net.specialattack.util.MathHelper;
import net.specialattack.util.RAMBuffer;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.GL11;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.libraries.ChannelLWJGLOpenAL;

import javax.sound.sampled.AudioFormat;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ChannelDiscoTek extends ChannelLWJGLOpenAL {

    public double speed = 0.0F;
    public RAMBuffer buffer;
    public EnergySections energy;
    private long prevTime;

    //public PipedInputStream pis;
    //public PipedOutputStream pos;
    public boolean beat;
    public long timeout;
    private byte[][] data = new byte[2][0];
    private int currArray = 0;
    private boolean playing = false;

    // Instantiated when starting the soundsystem
    public ChannelDiscoTek(int type, IntBuffer src) {
        super(type, src);
        this.libraryType = LibraryDiscoTek.class;
    }

    // Called when stopping the soundsystem
    @Override
    public void cleanup() {
        // System.err.println("cleanup()");
        super.cleanup();

        this.remove();
    }

    // Normal sounds (buttons, etc...)
    @Override
    public boolean attachBuffer(IntBuffer buf) {
        // System.err.println("attachBuffer(" + buf + ")");
        return super.attachBuffer(buf);
    }

    @Override
    public void setAudioFormat(AudioFormat audioFormat) {
        // System.err.println("setAudioFormat(" + audioFormat + ")");
        super.setAudioFormat(audioFormat);

        this.sendSpeed();
    }

    @Override
    public void setFormat(int format, int rate) {
        // System.err.println("setFormat(" + format + ", " + rate + ")");
        super.setFormat(format, rate);

        this.sendSpeed();
    }

    // Starting a streaming sound (Music)
    @Override
    public boolean preLoadBuffers(LinkedList<byte[]> bufferList) {
        System.err.println("preLoadBuffers(LinkedList<byte[]>)");
        if (bufferList.isEmpty()) {
            System.err.println(" -> NO DATA D:");
        }
        for (byte[] arr : bufferList) {
            this.sendBytes(arr);
            System.err.println(" -> byte[" + arr.length + "]");
        }
        return super.preLoadBuffers(bufferList);
    }

    // Streaming sound (Music)
    @Override
    public boolean queueBuffer(byte[] buffer) {
        //        int k = 512;
        //        for (int i = 0; i + k - 1 < buffer.length; i += k) {
        //            int j = i / k;
        //            for (int l = 0; l < k; l++) {
        //                if (j % 64 < 32) {
        //                    buffer[i + l] = (byte) ((j % 2) * 16 - 8);
        //                }
        //                else {
        //                    buffer[i + l] = (byte) 0;
        //                }
        //                //buffer[i + l] += (byte) ((j % 3) * 16 - 8);
        //                //buffer[i + l] += (byte) ((j % 8) * 2 - 1);
        //                //buffer[i + l] += (byte) (Math.sin((double) l * Math.PI * 2.0D / (double) k) * 8.0D + Math.sin((double) l * Math.PI * 4.0D / (double) k + 1) * 4.0D);
        //            }
        //        }
        this.sendBytes(buffer);
        // System.err.println("queueBuffer(byte[" + buffer.length + "])");
        return super.queueBuffer(buffer);
    }

    @Override
    public int feedRawAudioData(byte[] buffer) {
        System.err.println("feedRawAudioData(byte[" + buffer.length + "])");
        return super.feedRawAudioData(buffer);
    }

    @Override
    public void close() {
        super.close();

        this.remove();
    }

    @Override
    public void play() {
        // System.err.println("play()");
        super.play();
        this.sendSpeed();
        this.playing = true;
        this.prevTime = System.nanoTime();
        this.add();
    }

    @Override
    public void pause() {
        // System.err.println("pause()");
        super.pause();
        this.playing = false;
    }

    @Override
    public void stop() {
        // System.err.println("stop()");
        super.stop();
        this.playing = false;

        if (this.buffer != null) {
            this.buffer.reset();
        }

        this.remove();
    }

    public void remove() {
        if (ClientProxy.channels.contains(this)) {
            // System.err.println("Removing from watch list");
            ClientProxy.channels.remove(this);
        }
    }

    public void add() {
        if (true) {
            return;
        }
        if (this.channelType == SoundSystemConfig.TYPE_STREAMING) {
            if (this.buffer == null) {
                this.buffer = new RAMBuffer(0x800000);
                this.buffer.addSection(this.energy = new EnergySections(43));
            } else {
                this.buffer.reset();
            }
            // System.err.println("Adding to watch list");
            ClientProxy.channels.add(this);
        }
    }

    public void sendBytes(byte[] data) {
        if (this.buffer != null) {
            this.buffer.write(data);
        }
    }

    public void sendSpeed() {
        if (true) {
            return;
        }
        double bytesPerFrame = 1D;
        switch (this.ALformat) {
            case AL10.AL_FORMAT_MONO8:
                bytesPerFrame = 1D;
                break;
            case AL10.AL_FORMAT_MONO16:
                bytesPerFrame = 2D;
                break;
            case AL10.AL_FORMAT_STEREO8:
                bytesPerFrame = 2D;
                break;
            case AL10.AL_FORMAT_STEREO16:
                bytesPerFrame = 4D;
                break;
            default:
                break;
        }

        this.speed = (1000.0D / bytesPerFrame) / this.sampleRate;
    }

    public void render(double posX, double posY, double width, double height) {
        if (true) {
            return;
        }
        if (this.playing) {
            long currTime = System.nanoTime();

            int length = 8096;
            if (this.data[0].length != length) {
                for (int i = 0; i < this.data.length; i++) {
                    this.data[i] = new byte[length];
                }
            }

            long timeOffset = currTime - this.prevTime;
            double milis = (timeOffset) / 1000000.0D;
            int bytes = (int) (milis / this.speed);

            int prevArray = this.currArray;
            this.currArray = (this.currArray + 1) % this.data.length;

            if (this.buffer.getAvailable() > 0) {
                if (bytes > length) {
                    this.buffer.skip(bytes - length);
                    this.buffer.read(this.data[this.currArray], 0, MathHelper.min(length, this.buffer.getAvailable()));
                } else {
                    try {
                        System.arraycopy(this.data[prevArray], bytes, this.data[this.currArray], 0, length - bytes);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Objects.log.info("srcpos+length: " + (2 * length - 2 * bytes) + "; dstpos+length: " + (length - bytes) + "; length: " + this.data[prevArray].length);
                        this.prevTime = currTime;
                        return;
                    }
                    this.buffer.read(this.data[this.currArray], length - bytes, MathHelper.min(bytes, this.buffer.getAvailable()));
                }
            }

            FontRenderer font = MC.getFontRenderer();
            ArrayList<String> text = new ArrayList<String>();
            text.add("Milis: " + milis);
            text.add("Bytes: " + bytes);
            text.add("CurrArray: " + this.currArray);
            text.add("Available: " + this.buffer.getAvailable());
            text.add("RAMBuffer: " + this.buffer);
            for (int i = 0; i < this.data.length; i++) {
                int total = 0;
                for (byte b : this.data[i]) {
                    total += b + 128;
                }
                text.add("Average[" + i + "] = " + ((float) total / (float) this.data[i].length));
            }

            //text.add("" + this.energy);

            double intervalX = width / (length - 1);
            double intervalY = height / 255D;

            boolean temp = this.beat;
            boolean beat = this.energy.hasBeat();
            //            if (System.currentTimeMillis() > this.timeout) {
            this.beat = beat;
            //            }
            //            if (beat && temp != this.beat) {
            //                this.beat = beat;
            //                this.timeout = System.currentTimeMillis() + 100L;
            //                MC.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("note.hat"), 1.0F));
            //            }
            //            if (!prevBeat && this.energy.fourierEnergyBuffers[0].beat) {
            //            if (!this.prevBeat && this.energy.hasBeat()) {
            //                GL11.glColor4f(1.0F, 0.2F, 0.0F, 0.5F);
            //                MC.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("note.hat"), 1.0F));
            //            }
            //            else {
            //                GL11.glColor4f(0.5F, 0.0F, 0.0F, 0.4F);
            //            }
            //            this.prevBeat = this.energy.hasBeat();
            //            this.prevBeat = this.energy.fourierEnergyBuffers[0].beat;

            ClientProxy.beat = this.beat;
            if (this.beat) {
                String str = "Beat!";
                int x = (int) ((width - font.getStringWidth(str)) / 2 + posX);
                int y = (int) ((height - font.FONT_HEIGHT) / 2 + posY + font.FONT_HEIGHT);
                font.drawString(str, x, y, 0xFF0000, true);
                GL11.glColor4f(1.0F, 0.2F, 0.0F, 0.5F);
            } else {
                GL11.glColor4f(0.5F, 0.0F, 0.0F, 0.4F);
            }
            GL11.glLineWidth(2.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (int i = 0; i < length; i++) {
                double x = intervalX * i;
                double y = intervalY * this.data[this.currArray][i];
                // GL11.glVertex2d(posX + x, posY + y + 128.0F * intervalY);
            }
            GL11.glEnd();

            //GL11.glLineWidth(3.0F);
            double bandWidth = width / (double) this.energy.fourierBuffer.length;
            for (int i = 0; i < this.energy.fourierBuffer.length; i++) {
                if (this.energy.fourierEnergyBuffers[i].beat) {
                    GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.6F);
                } else {
                    GL11.glColor4f(0.0F, 0.0F, 1.0F, 0.6F);
                }
                double startX = posX + bandWidth * i;
                double startY = height - posY - intervalY * this.energy.fourierBuffer[i];// * 50.0D;
                double endX = posX + bandWidth * (i + 1);
                double endY = height - posY;
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2d(startX, startY);
                GL11.glVertex2d(startX, endY);
                GL11.glVertex2d(endX, endY);
                GL11.glVertex2d(endX, startY);
                GL11.glEnd();

                GL11.glEnable(GL11.GL_TEXTURE_2D);
                //font.drawString("" + i, (int) startX, (int) endY - 20, 0xFFFFFF);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);

            int offset = 0;
            for (String str : text) {
                @SuppressWarnings("unchecked") List<String> split = font.listFormattedStringToWidth(str, (int) width - 20);
                for (String string : split) {
                    font.drawString(string, (int) posX + 10, (int) posY + offset, 0xFF0000, true);
                    offset += 8;
                }
                offset++;
            }

            this.prevTime = currTime;
        }
    }

    private static class EnergySections extends RAMBuffer.Sections {

        public EnergyBuffer mainBuffer;
        public float[] fourierBuffer;
        public EnergyBuffer[] fourierEnergyBuffers;

        public EnergySections(int sectionCount) {
            super(1024, sectionCount);

            this.mainBuffer = new EnergyBuffer(sectionCount);
            int bufferSize = 64;
            this.fourierBuffer = new float[bufferSize];
            this.fourierEnergyBuffers = new EnergyBuffer[bufferSize];
            for (int i = 0; i < this.fourierEnergyBuffers.length; i++) {
                this.fourierEnergyBuffers[i] = new EnergyBuffer(sectionCount);
            }
        }

        @Override
        public void pushData(byte[] section) {
            super.pushData(section);

            this.mainBuffer.pushData(section);

            float[] fourierR = new float[section.length];
            float[] fourierI = new float[section.length];

            for (int i = 0; i < section.length; i++) {
                byte b = section[i];
                float f = (float) b / (float) this.fourierBuffer.length;
                fourierR[i] = (float) f;
                fourierI[i] = (float) f;
                //fourierI[fourierI.length - i - 1] = (float) d;
            }

            MathHelper.fastFourierTransform(-1, section.length, fourierR, fourierI);
            for (int i = 0; i < this.fourierBuffer.length; i++) {
                this.fourierBuffer[i] = (float) this.fourierBuffer.length / (float) section.length;
                float sum = 0.0F;
                for (int j = i * section.length / this.fourierBuffer.length; j < (i + 1) * section.length / this.fourierBuffer.length; j++) {
                    sum += Math.sqrt(fourierR[j] * fourierR[j] + fourierI[j] * fourierI[j]);
                }
                this.fourierBuffer[i] *= sum;
                this.fourierEnergyBuffers[i].pushData(this.fourierBuffer[i]);
            }
        }

        @Override
        public String toString() {
            return "Beat: " + this.hasBeat() + ";\nFourierEnergy: " + Arrays.toString(this.fourierEnergyBuffers);
        }

        public boolean hasBeat() {
            int length = this.fourierEnergyBuffers.length;
            if (this.fourierEnergyBuffers[0].beat || this.fourierEnergyBuffers[length - 1].beat) {
                return true;
            }
            if (this.fourierEnergyBuffers[1].beat || this.fourierEnergyBuffers[length - 2].beat) {
                return true;
            }
            //            if (this.fourierEnergyBuffers[length / 2 - 1].beat || this.fourierEnergyBuffers[length / 2].beat) {
            //                return true;
            //            }
            //            if (this.fourierEnergyBuffers[length / 2 + 1].beat || this.fourierEnergyBuffers[length / 2 + 2].beat) {
            //                return true;
            //            }
            //            for (EnergyBuffer buffer : this.fourierEnergyBuffers) {
            //                if (buffer.beat) {
            //                    return true;
            //                }
            //            }
            return false;
        }

    }

    private static class EnergyBuffer {

        public double[] energyLevels;
        public double averageEnergy;
        public double energyVariance;
        public double C;
        public boolean beat;
        public long timeout;

        public EnergyBuffer(int sectionCount) {
            this.energyLevels = new double[sectionCount];
        }

        public void pushData(byte[] section) {
            double energy = 0.0D;
            for (int i = 0; i < section.length; i++) {
                byte b = section[i];
                double d = (double) b / 128.0D;
                energy += d * d;
            }

            this.pushData(energy);
        }

        public void pushData(double energy) {
            this.averageEnergy = 0.0D;
            for (double d : this.energyLevels) {
                this.averageEnergy += d * d;
            }
            this.averageEnergy /= (double) this.energyLevels.length;
            this.averageEnergy = Math.sqrt(this.averageEnergy);

            this.energyVariance = 0.0D;
            for (double d : this.energyLevels) {
                double v = d - this.averageEnergy;
                this.energyVariance += v * v;
            }
            this.energyVariance /= (double) this.energyLevels.length;

            this.C = (-0.0025714D * this.energyVariance) + 1.5142857D;
            //this.C = (-0.0025714D * this.energyVariance) + 1.55D;

            boolean temp = this.beat;
            boolean beat = energy > this.C * this.averageEnergy;
            if (System.currentTimeMillis() > this.timeout) {
                this.beat = beat;
            }
            if (beat && temp != this.beat) {
                this.beat = beat;
                this.timeout = System.currentTimeMillis() + 100L;
                // MC.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("note.hat"), 1.0F));
            }
            //this.beat = energy > this.C * this.averageEnergy;

            System.arraycopy(this.energyLevels, 0, this.energyLevels, 1, this.energyLevels.length - 1);
            this.energyLevels[0] = energy;
        }

        @Override
        public String toString() {
            return "\nCompare: " + this.averageEnergy * this.C + ";\n  Energy: " + this.energyLevels[0] + ";\n Beat: " + this.beat;
        }

    }

}
