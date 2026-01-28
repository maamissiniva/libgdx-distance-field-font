# LibGDX distance field font rendering

## Dependency

[maamissiniva-libgdx-distance-field-font](https://central.sonatype.com/artifact/io.github.maamissiniva/maamissiniva-libgdx-distance-field-font)

The font format is defined [here](https://github.com/maamissiniva/bitmap-font-descriptor). 
A bitmap font can be generated
a plugin defined [here](https://github.com/maamissiniva/gradle-plugin-distance-field-font).

## Loading a bitmap font

The loading mechanism does not support anything else than texture file loading,
no support for atlases.

Use an asset manager and either use the specific loader:

```
MBitmapFont.setLoader(manager); // Needed once per manager, sets the loader for MBitmapFont
manager.load(name, MBitmapFont.class);
manager.finishLoadingAsset(name);
MBitmapFont fnt = manager.get(name);
```

or use the synchronous load method:

```
MBitmapFont fnt = MBitmapFont.loadSync(assetManager, name);
```

## Drawing text

One line text "layout" at the moment. Drawing is done using a triangle batch, if needed
a sprite batch support with custom shaders can be built.

The scale parameter is the target font size divided by the generated font size. If you want
to draw with a size 12 font and generated a size 32 font, the scale would be `12f/32f`. The
font size is given in the font descriptor so a size 12 font drawing would be
`12f / fnt.bfd.size`.

With a triangle batch within a `begin()` and `end()`:

```
triangleBatch.begin();
...
// draw 
fnt.draw(
    triangleBatch,   
    Color.WHITE,                 // color
    scale,                       // scale, multiplier of bitmap size
    x, y,                        // text coordinates
    "The text",                  // text
    HorizontalAlign.LEFT_ORIGIN, // draw text left of coordinates
    VerticalAlign.FONT_BOTTOM    // draw text above the coordinates
    );
...
// draw outline
fnt.drawOutline(
    triangleBatch,   
    0.3f,                        // outline "size"
    Color.WHITE,                 // color
    scale,                       // scale, multiplier of bitmap size
    x, y,                        // text coordinates
    "The text",                  // text
    HorizontalAlign.LEFT_ORIGIN, // draw text left of coordinates
    VerticalAlign.FONT_BOTTOM    // draw text above the coordinates
    );
...
triangleBatch.end();
```

There are methods that use a referential to place the text.


