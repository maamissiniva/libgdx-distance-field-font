package maamissiniva.libgdx.bitmapfont;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import maamissiniva.bitmapfontdescriptor.BitmapFontDescriptor;

public class MBitmapFontLoader extends AsynchronousAssetLoader<MBitmapFont, MBitmapFontLoader.MBitmapFontParameter> {
    
    public MBitmapFontLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    static String getTexturePath(String dffName, String textureName) {
        int i = dffName.lastIndexOf('/');
        if (i < 0)
            return textureName;
        return dffName.substring(0, i+1) + textureName;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, MBitmapFontParameter parameter) {
        Array<AssetDescriptor> deps = new Array<>();
        BitmapFontDescriptor descriptor = BitmapFontDescriptorDeserializer.deserializer.deserialize(file);
        TextureLoader.TextureParameter textureParams = new TextureLoader.TextureParameter();
        textureParams.genMipMaps = false; 
        textureParams.magFilter  = TextureFilter.Linear;
        textureParams.minFilter  = TextureFilter.Linear;
        for (String textureName : descriptor.textures) {
            String resolved = getTexturePath(fileName, textureName);
            AssetDescriptor ad = new AssetDescriptor(resolved, Texture.class, textureParams);
            deps.add(ad);
        }
        return deps;
    }

    @Override
    public void loadAsync (AssetManager manager, String fileName, FileHandle file, MBitmapFontParameter parameter) {
    }

    @Override
    public MBitmapFont loadSync (AssetManager manager, String fileName, FileHandle file, MBitmapFontParameter parameter) {
        BitmapFontDescriptor descriptor = BitmapFontDescriptorDeserializer.deserializer.deserialize(file);
        Array<TextureRegion> textures = new Array<>(false, descriptor.textures.size(), n -> new TextureRegion[n]);
        for (String textureName : descriptor.textures) {
            String resolved = getTexturePath(fileName, textureName);
            Texture texture = manager.get(resolved, Texture.class);
            textures.add(new TextureRegion(texture));
        }
        return new MBitmapFont(descriptor, textures);
    }

    static public class MBitmapFontParameter extends AssetLoaderParameters<MBitmapFont> {
    }
    
}
