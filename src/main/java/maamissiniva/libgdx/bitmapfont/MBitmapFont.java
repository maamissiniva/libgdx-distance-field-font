package maamissiniva.libgdx.bitmapfont;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import maamissiniva.bitmapfontdescriptor.BitmapFontDescriptor;
import maamissiniva.bitmapfontdescriptor.CharGlyph;
import maamissiniva.bitmapfontdescriptor.Glyph;
import maamissiniva.libgdx.trianglebatch.TriangleBatch;

/**
 * Runtime distance field font built from a {@link BitmapFontDescriptor}.  
 */
public class MBitmapFont {
    
    public BitmapFontDescriptor bfd;
    public Array<TextureRegion> textures;
    public Glyph[] glyphs;
    
    public MBitmapFont(BitmapFontDescriptor bfd, Array<TextureRegion> textures) {
        this.bfd      = bfd;
        this.textures = textures;
        glyphs = new Glyph[256];
        for (CharGlyph c : bfd.chars)
            if (c.character < 256)
                glyphs[c.character] = bfd.glyphs.get(c.glyph);
    }
    
    public static void setLoader(AssetManager manager) {
        manager.setLoader(MBitmapFont.class, new MBitmapFontLoader(manager.getFileHandleResolver()));        
    }
    
    /**
     * Most likely use case.
     * @param manager asset manager
     * @param name    font file name
     * @return        bitmap font
     */
    public static MBitmapFont loadSync(AssetManager manager, String name) {
        setLoader(manager);
        manager.load(name, MBitmapFont.class);
        manager.finishLoadingAsset(name);
        return manager.get(name);
    }

    public Glyph getGlyph(char c) {
        if (c >= glyphs.length) 
            return null;
        return glyphs[c];        
    }
    
    public float getAdvance(char c) {
        Glyph g = getGlyph(c);
        if (g == null)
            return 0f;
        return g.advance;
    }
    
    public float getWidth(char c) {
        Glyph g = getGlyph(c);
        if (g == null)
            return 0f;
        return g.lwidth;
    }
    
    // Multiply by some scale if needed.
    public float getTextWidth(String text) {
        float width = 0f;
        for (int i=0; i<text.length()-1; i++) 
            width += getAdvance(text.charAt(i));
        if (text.length() > 0) 
            width += getWidth(text.charAt(text.length() - 1));
        return width;
    }
    
    public void draw(
            TriangleBatch triangleBatch, Color color,
            float scale, float x, float y, 
            String text, 
            HorizontalAlign hAlign,
            VerticalAlign vAlign) {
        float width = getTextWidth(text);
        // Compute text origin
        float ox = x;
        float oy = y;
        switch (hAlign) {
        case LEFT_ORIGIN  :                              break;
        case CENTER       : ox -= width * scale * 0.5f; break;
        case RIGHT_ORIGIN : ox -= width * scale;        break;
        }
        switch (vAlign) {
        case FONT_BOTTOM :                                 break;
        case FONT_CENTER : oy -= bfd.size * scale * 0.5f; break;
        case FONT_TOP    : oy -= bfd.size * scale;        break;
        default          :                                 break;
        }
        float fColor = color.toFloatBits();
        for (int i = 0; i < text.length(); i++) {
            Glyph g = getGlyph(text.charAt(i));
            if (g != null) {
                TextureRegion t = textures.items[g.texture];
                float x0 = ox + g.drawX * scale;
                float x1 = x0 + g.drawWidth * scale;
                float y0 = oy + g.drawY * scale;
                float y1 = y0 + g.drawHeight * scale;
                triangleBatch.drawDistanceFieldQuad(
                        t.getTexture(), 
                        scale, 
                        x0, y0, fColor, g.u0, g.v0, 
                        x1, y0, fColor, g.u1, g.v0,
                        x1, y1, fColor, g.u1, g.v1,
                        x0, y1, fColor, g.u0, g.v1
                        );
                ox += g.advance * scale;
            }
        }
    }

    public void drawOutline(
            TriangleBatch triangleBatch, float outline, Color color,
            float scale,  
            float x, float y, 
            String text, 
            HorizontalAlign hAlign,
            VerticalAlign vAlign) {
        float width = getTextWidth(text);
        // Compute text origin
        float ox = x;
        float oy = y;
        switch (hAlign) {
        case LEFT_ORIGIN  :                              break;
        case CENTER       : ox -= width * scale * 0.5f; break;
        case RIGHT_ORIGIN : ox -= width * scale;        break;
        }
        switch (vAlign) {
        case FONT_BOTTOM :                                 break;
        case FONT_CENTER : oy -= bfd.size * scale * 0.5f; break;
        case FONT_TOP    : oy -= bfd.size * scale;        break;
        default          :                                 break;
        }
        float fColor = color.toFloatBits();
        for (int i = 0; i < text.length(); i++) {
            Glyph g = getGlyph(text.charAt(i));
            if (g != null) {
                TextureRegion t = textures.items[g.texture];
                float x0 = ox + g.drawX * scale;
                float x1 = x0 + g.drawWidth * scale;
                float y0 = oy + g.drawY * scale;
                float y1 = y0 + g.drawHeight * scale;
                triangleBatch.drawDistanceFieldOutlineQuad(
                        t.getTexture(), 
                        scale, outline,
                        x0, y0, fColor, g.u0, g.v0, 
                        x1, y0, fColor, g.u1, g.v0,
                        x1, y1, fColor, g.u1, g.v1,
                        x0, y1, fColor, g.u0, g.v1
                        );
                ox += g.advance * scale;
            }
        }
    }

    public void draw(
            TriangleBatch triangleBatch, Color color,
            Vector2 dx, Vector2 dy, 
            float scale, float x, float y,
            String text,
            HorizontalAlign hAlign, VerticalAlign vAlign) {
        
        float width = getTextWidth(text);
        // Compute text origin
        float ox = x;
        float oy = y;
        switch (hAlign) {
        case LEFT_ORIGIN  :                                                      break;
        case CENTER       : ox -= width * dx.x * scale * 0.5f; oy -= width * dx.y *scale * 0.5f; break;
        case RIGHT_ORIGIN : ox -= width * dx.x * scale;        oy -= width * dx.y *scale;        break;
        }
        switch (vAlign) {
        case FONT_BOTTOM :                                 break;
        case FONT_CENTER : ox-= dy.x * bfd.size * scale * 0.5f; oy -= dy.y * bfd.size * scale * 0.5f; break;
        case FONT_TOP    : ox-= dy.x * bfd.size * scale;        oy -= dy.y * bfd.size * scale;        break;
        default          :                                 break;
        }
        float fColor = color.toFloatBits();
        // Use dx,dy as the direction vectors.
        for (int i = 0; i < text.length(); i++) {
            Glyph g = getGlyph(text.charAt(i));
            if (g != null) {
                TextureRegion t = textures.items[g.texture];
                float x0 = ox + (dx.x * g.drawX     + dy.x * g.drawY     ) * scale;
                float y0 = oy + (dx.y * g.drawX     + dy.y * g.drawY     ) * scale;
                float dwx = dx.x * g.drawWidth * scale;
                float dwy = dx.y * g.drawWidth * scale;
                float dhx = dy.x * g.drawHeight * scale;
                float dhy = dy.y * g.drawHeight * scale;
                triangleBatch.drawDistanceFieldQuad(
                        t.getTexture(), 
                        scale, 
                        x0,             y0,             fColor, g.u0, g.v0, 
                        x0 + dwx,       y0 + dwy,       fColor, g.u1, g.v0,
                        x0 + dwx + dhx, y0 + dwy + dhy, fColor, g.u1, g.v1,
                        x0 + dhx,       y0 + dhy,       fColor, g.u0, g.v1
                        );
                ox += g.advance * scale * dx.x;
                oy += g.advance * scale * dx.y;
            }
        }

    }
    
    public void drawOutline(
            TriangleBatch triangleBatch, float outline, Color color,
            Vector2 dx, Vector2 dy, 
            float scale, float x, float y,
            String text,
            HorizontalAlign hAlign, VerticalAlign vAlign) {
        
        float width = getTextWidth(text);
        // Compute text origin
        float ox = x;
        float oy = y;
        switch (hAlign) {
        case LEFT_ORIGIN  :                                                      break;
        case CENTER       : ox -= width * dx.x * scale * 0.5f; oy -= width * dx.y *scale * 0.5f; break;
        case RIGHT_ORIGIN : ox -= width * dx.x * scale;        oy -= width * dx.y *scale;        break;
        }
        switch (vAlign) {
        case FONT_BOTTOM :                                 break;
        case FONT_CENTER : ox-= dy.x * bfd.size * scale * 0.5f; oy -= dy.y * bfd.size * scale * 0.5f; break;
        case FONT_TOP    : ox-= dy.x * bfd.size * scale;        oy -= dy.y * bfd.size * scale;        break;
        default          :                                 break;
        }
        float fColor = color.toFloatBits();
        // Use dx,dy as the direction vectors.
        for (int i = 0; i < text.length(); i++) {
            Glyph g = getGlyph(text.charAt(i));
            if (g != null) {
                TextureRegion t = textures.items[g.texture];
                float x0 = ox + (dx.x * g.drawX     + dy.x * g.drawY     ) * scale;
                float y0 = oy + (dx.y * g.drawX     + dy.y * g.drawY     ) * scale;
                float dwx = dx.x * g.drawWidth * scale;
                float dwy = dx.y * g.drawWidth * scale;
                float dhx = dy.x * g.drawHeight * scale;
                float dhy = dy.y * g.drawHeight * scale;
                triangleBatch.drawDistanceFieldOutlineQuad(
                        t.getTexture(),
                        scale, 
                        outline, 
                        x0,             y0,             fColor, g.u0, g.v0, 
                        x0 + dwx,       y0 + dwy,       fColor, g.u1, g.v0,
                        x0 + dwx + dhx, y0 + dwy + dhy, fColor, g.u1, g.v1,
                        x0 + dhx,       y0 + dhy,       fColor, g.u0, g.v1
                        );
                ox += g.advance * scale * dx.x;
                oy += g.advance * scale * dx.y;
            }
        }

    }

}
