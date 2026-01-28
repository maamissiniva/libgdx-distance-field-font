package maamissiniva.libgdx.bitmapfont;

import static maamissiniva.libgdx.json.Deserializer.arrayField;
import static maamissiniva.libgdx.json.Deserializer.fieldFloat;
import static maamissiniva.libgdx.json.Deserializer.fieldChar;
import static maamissiniva.libgdx.json.Deserializer.fieldInt;
import static maamissiniva.libgdx.json.Deserializer.fieldString;
import static maamissiniva.libgdx.json.Deserializer.fieldStringArray;

import java.util.ArrayList;

import maamissiniva.bitmapfontdescriptor.BitmapFontDescriptor;
import maamissiniva.bitmapfontdescriptor.CharGlyph;
import maamissiniva.bitmapfontdescriptor.Glyph;
import maamissiniva.libgdx.json.Deserializer;

public class BitmapFontDescriptorDeserializer {

    public static Deserializer<Glyph> glyphDeserializer = new Deserializer<Glyph>(
            () -> new Glyph(),
            fieldFloat("advance",    (o,v) -> o.advance    = v),
            fieldInt  ("texture",    (o,v) -> o.texture    = v),
            fieldFloat("drawX",      (o,v) -> o.drawX      = v),
            fieldFloat("drawY",      (o,v) -> o.drawY      = v),
            fieldFloat("drawWidth",  (o,v) -> o.drawWidth  = v),
            fieldFloat("drawHeight", (o,v) -> o.drawHeight = v),
            fieldFloat("u0",         (o,v) -> o.u0         = v),
            fieldFloat("v0",         (o,v) -> o.v0         = v),
            fieldFloat("u1",         (o,v) -> o.u1         = v),
            fieldFloat("v1",         (o,v) -> o.v1         = v),
            fieldFloat("lx",         (o,v) -> o.lx         = v),
            fieldFloat("ly",         (o,v) -> o.ly         = v),
            fieldFloat("lwidth",     (o,v) -> o.lwidth     = v),
            fieldFloat("lheight",    (o,v) -> o.lheight    = v)
            );
    
    public static Deserializer<CharGlyph> charGlyphDeserializer = new Deserializer<>(
            () -> new CharGlyph(),
            fieldChar("character", (o,v) -> o.character = v),
            fieldInt ("glyph",     (o,v) -> o.glyph     = v)
            );
    
    public static Deserializer<BitmapFontDescriptor> deserializer = new Deserializer<BitmapFontDescriptor>(
            () -> new BitmapFontDescriptor(),
            fieldString     ("name",     (o,v) -> o.name     = v),
            fieldFloat      ("size",     (o,v) -> o.size     = v),
            fieldFloat      ("top",      (o,v) -> o.top      = v),
            fieldFloat      ("ascent"  , (o,v) -> o.ascent   = v),
            fieldFloat      ("descent" , (o,v) -> o.descent  = v),
            fieldFloat      ("baseline", (o,v) -> o.baseline = v),
            fieldStringArray("textures", (o,v) -> o.textures = v),
            arrayField("chars",
                    o -> o.chars = new ArrayList<>(),
                    charGlyphDeserializer),
            arrayField("glyphs",
                    o -> o.glyphs = new ArrayList<>(),
                    glyphDeserializer)
            );

}
