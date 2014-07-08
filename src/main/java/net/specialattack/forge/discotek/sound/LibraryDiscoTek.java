package net.specialattack.forge.discotek.sound;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import paulscode.sound.Channel;
import paulscode.sound.SoundSystemException;
import paulscode.sound.libraries.ChannelLWJGLOpenAL;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

import java.nio.IntBuffer;

public class LibraryDiscoTek extends LibraryLWJGLOpenAL {

    public LibraryDiscoTek() throws SoundSystemException {
        super();
    }

    @Override
    protected Channel createChannel(int type) {
        ChannelLWJGLOpenAL channel;
        IntBuffer ALSource;

        ALSource = BufferUtils.createIntBuffer(1);
        try {
            AL10.alGenSources(ALSource);
        } catch (java.lang.Exception e) {
            AL10.alGetError();
            return null;  // no more voices left
        }

        if (AL10.alGetError() != AL10.AL_NO_ERROR) {
            return null;
        }

        channel = new ChannelDiscoTek(type, ALSource); // XXX: Changed
        return channel;
    }

    @Override
    public String getClassName() {
        return "LibraryDiscoTek";
    }

}
