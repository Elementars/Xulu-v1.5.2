// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.clickgui;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import java.awt.Rectangle;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.nio.FloatBuffer;

public final class RenderMethods
{
    public static FloatBuffer matModelView;
    public static FloatBuffer matProjection;
    
    public static Color rainbow(final long offset, final float fade) {
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    public static Color blend(final Color color1, final Color color2, final float ratio) {
        final float rat = 1.0f - ratio;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        final Color color3 = new Color(rgb1[0] * ratio + rgb2[0] * rat, rgb1[1] * ratio + rgb2[1] * rat, rgb1[2] * ratio + rgb2[2] * rat);
        return color3;
    }
    
    public static double getDiff(final double lastI, final double i, final float ticks, final double ownI) {
        return lastI + (i - lastI) * ticks - ownI;
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void enableGL3D() {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4353);
        GL11.glDisable(2896);
    }
    
    public static void disableGL3D() {
        GL11.glEnable(2896);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawTriangle(final int x, final int y, final int type, final int size, final int color) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GL11.glColor4f(r, g, b, alpha);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.0f);
        GL11.glShadeModel(7425);
        switch (type) {
            case 0: {
                GL11.glBegin(2);
                GL11.glVertex2d((double)x, (double)(y + size));
                GL11.glVertex2d((double)(x + size), (double)(y - size));
                GL11.glVertex2d((double)(x - size), (double)(y - size));
                GL11.glEnd();
                GL11.glBegin(4);
                GL11.glVertex2d((double)x, (double)(y + size));
                GL11.glVertex2d((double)(x + size), (double)(y - size));
                GL11.glVertex2d((double)(x - size), (double)(y - size));
                GL11.glEnd();
                break;
            }
            case 1: {
                GL11.glBegin(2);
                GL11.glVertex2d((double)x, (double)y);
                GL11.glVertex2d((double)x, (double)(y + size / 2));
                GL11.glVertex2d((double)(x + size + size / 2), (double)y);
                GL11.glEnd();
                GL11.glBegin(4);
                GL11.glVertex2d((double)x, (double)y);
                GL11.glVertex2d((double)x, (double)(y + size / 2));
                GL11.glVertex2d((double)(x + size + size / 2), (double)y);
                GL11.glEnd();
            }
            case 3: {
                GL11.glBegin(2);
                GL11.glVertex2d((double)x, (double)y);
                GL11.glVertex2d(x + size * 1.25, (double)(y - size / 2));
                GL11.glVertex2d(x + size * 1.25, (double)(y + size / 2));
                GL11.glEnd();
                GL11.glBegin(4);
                GL11.glVertex2d(x + size * 1.25, (double)(y - size / 2));
                GL11.glVertex2d((double)x, (double)y);
                GL11.glVertex2d(x + size * 1.25, (double)(y + size / 2));
                GL11.glEnd();
                break;
            }
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void enableGL3D(final float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }
    
    public static int applyTexture(final int texId, final int width, final int height, final ByteBuffer pixels, final boolean linear, final boolean repeat) {
        GL11.glBindTexture(3553, texId);
        GL11.glTexParameteri(3553, 10241, linear ? 9729 : 9728);
        GL11.glTexParameteri(3553, 10240, linear ? 9729 : 9728);
        GL11.glTexParameteri(3553, 10242, repeat ? 10497 : 10496);
        GL11.glTexParameteri(3553, 10243, repeat ? 10497 : 10496);
        GL11.glPixelStorei(3317, 1);
        GL11.glTexImage2D(3553, 0, 32856, width, height, 0, 6408, 5121, pixels);
        return texId;
    }
    
    public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public static void drawRect(final Rectangle rectangle, final int color) {
        drawRect((float)rectangle.x, (float)rectangle.y, (float)(rectangle.x + rectangle.width), (float)(rectangle.y + rectangle.height), color);
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1, final int color) {
        enableGL2D();
        glColor(color);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }
    
    public static void drawBorderedRect(final float x, final float y, final float x1, final float y1, final float width, final int internalColor, final int borderColor) {
        enableGL2D();
        glColor(internalColor);
        drawRect(x + width, y + width, x1 - width, y1 - width);
        glColor(borderColor);
        drawRect(x + width, y, x1 - width, y + width);
        drawRect(x, y, x + width, y1);
        drawRect(x1 - width, y, x1, y1);
        drawRect(x + width, y1 - width, x1 - width, y1);
        disableGL2D();
    }
    
    public static void drawBorderedRect(float x, float y, float x1, float y1, final int insideC, final int borderC) {
        enableGL2D();
        x *= 2.0f;
        x1 *= 2.0f;
        y *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y, y1 - 1.0f, borderC);
        drawVLine(x1 - 1.0f, y, y1, borderC);
        drawHLine(x, x1 - 1.0f, y, borderC);
        drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void drawBorderedRectReliant(final float x, final float y, final float x1, final float y1, final float lineWidth, final int inside, final int border) {
        enableGL2D();
        drawRect(x, y, x1, y1, inside);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }
    
    public static void drawGradientBorderedRectReliant(final float x, final float y, final float x1, final float y1, final float lineWidth, final int border, final int bottom, final int top) {
        enableGL2D();
        drawGradientRect(x, y, x1, y1, top, bottom);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }
    
    public static void drawRoundedRect(float x, float y, float x1, float y1, final int borderC, final int insideC) {
        enableGL2D();
        x *= 2.0f;
        y *= 2.0f;
        x1 *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y + 1.0f, y1 - 2.0f, borderC);
        drawVLine(x1 - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void drawBorderedRect(final Rectangle rectangle, final float width, final int internalColor, final int borderColor) {
        final float x = (float)rectangle.x;
        final float y = (float)rectangle.y;
        final float x2 = (float)(rectangle.x + rectangle.width);
        final float y2 = (float)(rectangle.y + rectangle.height);
        enableGL2D();
        glColor(internalColor);
        drawRect(x + width, y + width, x2 - width, y2 - width);
        glColor(borderColor);
        drawRect(x + 1.0f, y, x2 - 1.0f, y + width);
        drawRect(x, y, x + width, y2);
        drawRect(x2 - width, y, x2, y2);
        drawRect(x + 1.0f, y2 - width, x2 - 1.0f, y2);
        disableGL2D();
    }
    
    public static void drawGradientRect(final float x, final float y, final float x1, final float y1, final int topColor, final int bottomColor) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(topColor);
        GL11.glVertex2d((double)x, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        glColor(bottomColor);
        GL11.glVertex2d((double)x1, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawGradientHRect(final float x, final float y, final float x1, final float y1, final int topColor, final int bottomColor) {
        enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        glColor(topColor);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        glColor(bottomColor);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        disableGL2D();
    }
    
    public static void drawGradientRect(final double x, final double y, final double x2, final double y2, final int col1, final int col2) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(col1);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        glColor(col2);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawGradientBorderedRect(final double x, final double y, final double x2, final double y2, final float l1, final int col1, final int col2, final int col3) {
        enableGL2D();
        GL11.glPushMatrix();
        glColor(col1);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        drawGradientRect(x, y, x2, y2, col2, col3);
        disableGL2D();
    }
    
    public static void drawStrip(final int x, final int y, final float width, final double angle, final float points, final float radius, final int color) {
        final float f1 = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glTranslated((double)x, (double)y, 0.0);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glLineWidth(width);
        if (angle > 0.0) {
            GL11.glBegin(3);
            for (int i = 0; i < angle; ++i) {
                final float a = (float)(i * (angle * 3.141592653589793 / points));
                final float xc = (float)(Math.cos(a) * radius);
                final float yc = (float)(Math.sin(a) * radius);
                GL11.glVertex2f(xc, yc);
            }
            GL11.glEnd();
        }
        if (angle < 0.0) {
            GL11.glBegin(3);
            for (int i = 0; i > angle; --i) {
                final float a = (float)(i * (angle * 3.141592653589793 / points));
                final float xc = (float)(Math.cos(a) * -radius);
                final float yc = (float)(Math.sin(a) * -radius);
                GL11.glVertex2f(xc, yc);
            }
            GL11.glEnd();
        }
        disableGL2D();
        GL11.glDisable(3479);
        GL11.glPopMatrix();
    }
    
    public static void drawHLine(float x, float y, final float x1, final int y1) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }
    
    public static void drawVLine(final float x, float y, float x1, final int y1) {
        if (x1 < y) {
            final float var5 = y;
            y = x1;
            x1 = var5;
        }
        drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }
    
    public static void drawHLine(float x, float y, final float x1, final int y1, final int y2) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1, final float r, final float g, final float b, final float a) {
        enableGL2D();
        GL11.glColor4f(r, g, b, a);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }
    
    public static void rectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final double var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final Tessellator var6 = Tessellator.getInstance();
        final BufferBuilder var7 = var6.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        var7.begin(7, DefaultVertexFormats.POSITION);
        var7.pos(left, bottom, 0.0).endVertex();
        var7.pos(right, bottom, 0.0).endVertex();
        var7.pos(right, top, 0.0).endVertex();
        var7.pos(left, top, 0.0).endVertex();
        var6.draw();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }
    
    public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
        r *= 2.0f;
        cx *= 2.0f;
        cy *= 2.0f;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        final float theta = (float)(6.2831852 / num_segments);
        final float p = (float)Math.cos(theta);
        final float s = (float)Math.sin(theta);
        float x = r;
        float y = 0.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            final float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void drawFullCircle(int cx, int cy, double r, final int c) {
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360; ++i) {
            final double x = Math.sin(i * 3.141592653589793 / 180.0) * r;
            final double y = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(cx + x, cy + y);
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
        final float red = 0.003921569f * redRGB;
        final float green = 0.003921569f * greenRGB;
        final float blue = 0.003921569f * blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawOutlinedBox(final AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin(3);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
    }
    
    public static void renderCrosses(final AxisAlignedBB box) {
        GL11.glBegin(1);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glEnd();
    }
    
    public static void drawBox(final AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glEnd();
    }
    
    static {
        RenderMethods.matModelView = GLAllocation.createDirectFloatBuffer(16);
        RenderMethods.matProjection = GLAllocation.createDirectFloatBuffer(16);
    }
}
