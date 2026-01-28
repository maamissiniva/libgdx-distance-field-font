package maamissiniva.libgdx.bitmapfont;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import maamissiniva.bitmapfontdescriptor.BitmapFontDescriptor;
import maamissiniva.bitmapfontdescriptor.CharGlyph;
import maamissiniva.bitmapfontdescriptor.Glyph;

public class BitmapFontDescriptorTest {
    
    static String toJSONString(Object o) throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        return m.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }
    
    static BitmapFontDescriptor deserializeBFD(String json) {
        System.out.println("deserializing '" + json + "'");
        return BitmapFontDescriptorDeserializer.deserializer.deserialize(json);
    }
        
    static Glyph glyph(int texture, float x, float y, float width, float height, float u0, float v0, float u1, float v1, float xoffset, float yoffset, float advance) {
        Glyph glyph = new Glyph();
        glyph.texture    = texture;
        glyph.drawX      = x;
        glyph.drawY      = y;
        glyph.drawWidth  = width;
        glyph.drawHeight = height;
        glyph.u0         = u0;
        glyph.v0         = v0;
        glyph.u1         = u1;
        glyph.v1         = v1;
        glyph.lx         = xoffset;
        glyph.ly         = yoffset;
        glyph.lwidth     = xoffset;
        glyph.lheight    = yoffset;
        glyph.advance    = advance;
        return glyph;
    }
    
    @Test
    public void test01() throws JsonProcessingException {
        BitmapFontDescriptor d = new BitmapFontDescriptor();
        d.name     = "FontName";
        d.size     = 32;
        d.ascent   = 30;
        d.baseline = 20;
        d.descent  = 10;
        d.textures = Arrays.asList("FontName.png");
        d.glyphs   = Arrays.asList();
        String js = toJSONString(d);
        BitmapFontDescriptor e = deserializeBFD(js);
        assertEquals(d.name, e.name);
        assertEquals(d.textures, e.textures);
    }
        
    @Test
    public void test02() throws JsonProcessingException {
        BitmapFontDescriptor d = new BitmapFontDescriptor();
        d.name     = "FontName";
        d.size     = 32;
        d.ascent   = 30;
        d.baseline = 20;
        d.descent  = 10;
        d.textures = Arrays.asList("FontName.png");
        d.chars = Arrays.asList(new CharGlyph('a', 1));
        d.glyphs   = Arrays.asList(glyph(
                0, 
                10.1f, 10.2f, 30.1f, 30.2f,
                .2f, .3f, .4f, .5f,
                12.1f, 12.2f, 
                13f));
        String js = toJSONString(d);
        BitmapFontDescriptor e = deserializeBFD(js);
        assertEquals(d.name, e.name);
        assertEquals(d.textures, e.textures);
    }
    
}
