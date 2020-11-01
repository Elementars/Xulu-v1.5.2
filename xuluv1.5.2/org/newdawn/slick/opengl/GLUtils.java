// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import org.newdawn.slick.opengl.renderer.Renderer;

public final class GLUtils
{
    public static void checkGLContext() {
        try {
            Renderer.get().glGetError();
        }
        catch (NullPointerException e) {
            throw new RuntimeException("OpenGL based resources (images, fonts, sprites etc) must be loaded as part of init() or the game loop. They cannot be loaded before initialisation.");
        }
    }
}
