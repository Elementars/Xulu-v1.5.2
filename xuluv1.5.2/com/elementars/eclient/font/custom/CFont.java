// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.font.custom;

import org.lwjgl.opengl.GL11;
import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.Font;

public class CFont
{
    private float imgSize;
    protected CharData[] charData;
    protected Font font;
    protected boolean antiAlias;
    protected boolean fractionalMetrics;
    protected int fontHeight;
    protected int charOffset;
    protected DynamicTexture tex;
    
    public CFont(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        this.imgSize = 512.0f;
        this.charData = new CharData[256];
        this.fontHeight = -1;
        this.charOffset = 0;
        this.font = font;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.tex = this.setupTexture(font, antiAlias, fractionalMetrics, this.charData);
    }
    
    protected DynamicTexture setupTexture(final Font font, final boolean antiAlias, final boolean fractionalMetrics, final CharData[] chars) {
        final BufferedImage img = this.generateFontImage(font, antiAlias, fractionalMetrics, chars);
        try {
            return new DynamicTexture(img);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected BufferedImage generateFontImage(final Font font, final boolean antiAlias, final boolean fractionalMetrics, final CharData[] chars) {
        final int imgSize = (int)this.imgSize;
        final BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
        final Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
        g.setFont(font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        final FontMetrics fontMetrics = g.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        for (int i = 0; i < chars.length; ++i) {
            final char ch = (char)i;
            final CharData charData = new CharData();
            final Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
            charData.width = dimensions.getBounds().width + 8;
            charData.height = dimensions.getBounds().height;
            if (positionX + charData.width >= imgSize) {
                positionX = 0;
                positionY += charHeight;
                charHeight = 0;
            }
            if (charData.height > charHeight) {
                charHeight = charData.height;
            }
            charData.storedX = positionX;
            charData.storedY = positionY;
            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height;
            }
            chars[i] = charData;
            g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width;
        }
        return bufferedImage;
    }
    
    public void drawChar(final CharData[] chars, final char c, final float x, final float y) throws ArrayIndexOutOfBoundsException {
        try {
            this.drawQuad(x, y, (float)chars[c].width, (float)chars[c].height, (float)chars[c].storedX, (float)chars[c].storedY, (float)chars[c].width, (float)chars[c].height);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void drawQuad(final float x, final float y, final float width, final float height, final float srcX, final float srcY, final float srcWidth, final float srcHeight) {
        final float renderSRCX = srcX / this.imgSize;
        final float renderSRCY = srcY / this.imgSize;
        final float renderSRCWidth = srcWidth / this.imgSize;
        final float renderSRCHeight = srcHeight / this.imgSize;
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
    }
    
    public int getStringHeight(final String text) {
        return this.getHeight();
    }
    
    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }
    
    public int getStringWidth(final String text) {
        int width = 0;
        for (final char c : text.toCharArray()) {
            if (c < this.charData.length && c >= '\0') {
                width += this.charData[c].width - 8 + this.charOffset;
            }
        }
        return width / 2;
    }
    
    public boolean isAntiAlias() {
        return this.antiAlias;
    }
    
    public void setAntiAlias(final boolean antiAlias) {
        if (this.antiAlias != antiAlias) {
            this.antiAlias = antiAlias;
            this.tex = this.setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
        }
    }
    
    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }
    
    public void setFractionalMetrics(final boolean fractionalMetrics) {
        if (this.fractionalMetrics != fractionalMetrics) {
            this.fractionalMetrics = fractionalMetrics;
            this.tex = this.setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
        }
    }
    
    public Font getFont() {
        return this.font;
    }
    
    public void setFont(final Font font) {
        this.font = font;
        this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }
    
    protected static class CharData
    {
        public int width;
        public int height;
        public int storedX;
        public int storedY;
    }
}
