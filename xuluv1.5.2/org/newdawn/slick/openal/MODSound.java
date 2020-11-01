// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.openal;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import java.io.IOException;
import java.io.InputStream;
import ibxm.Module;

public class MODSound extends AudioImpl
{
    private Module module;
    private SoundStore store;
    
    public MODSound(final SoundStore store, final InputStream in) throws IOException {
        this.store = store;
    }
    
    @Override
    public int playAsMusic(final float pitch, final float gain, final boolean loop) {
        this.cleanUpSource();
        this.store.setCurrentMusicVolume(gain);
        this.store.setMOD(this);
        return this.store.getSource(0);
    }
    
    private void cleanUpSource() {
        AL10.alSourceStop(this.store.getSource(0));
        final IntBuffer buffer = BufferUtils.createIntBuffer(1);
        for (int queued = AL10.alGetSourcei(this.store.getSource(0), 4117); queued > 0; --queued) {
            AL10.alSourceUnqueueBuffers(this.store.getSource(0), buffer);
        }
        AL10.alSourcei(this.store.getSource(0), 4105, 0);
    }
    
    public void poll() {
    }
    
    @Override
    public int playAsSoundEffect(final float pitch, final float gain, final boolean loop) {
        return -1;
    }
    
    @Override
    public void stop() {
        this.store.setMOD(null);
    }
    
    @Override
    public float getPosition() {
        throw new RuntimeException("Positioning on modules is not currently supported");
    }
    
    @Override
    public boolean setPosition(final float position) {
        throw new RuntimeException("Positioning on modules is not currently supported");
    }
}
