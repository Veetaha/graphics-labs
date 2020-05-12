package v.e.e.t.a.h.a;

import javax.imageio.ImageIO;
import javax.vecmath.*;

import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class Resources {
    static class Paths {
        static class ladybug {
            static String obj = "models/ladybug.obj";
            static String texture = "models/ladybug.png";
        }
        static String background = "models/background.jpg";
    }

    static String resourcePath(String relative) {
        return Thread.currentThread().getContextClassLoader().getResource(relative).getPath();
    }

    static Image loadImage(String resource) {
        try {
            return ImageIO.read(new File(resourcePath(resource)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(228);
            return null;
        }
    }

    static TextureLoader textureLoader(String resource, Canvas3D canvas) {
        return new TextureLoader(resourcePath(resource), canvas);
    }

    static Appearance loadTexture(String fileName, boolean emit) {
        var loader = new TextureLoader(loadImage(fileName), null);
        var texture = (Texture2D)loader.getTexture();
        texture.setMinFilter(texture.BASE_LEVEL_LINEAR);
        texture.setMagFilter(texture.BASE_LEVEL_LINEAR);

        var appearance = new Appearance();
        {
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.MODULATE);
            appearance.setTextureAttributes(texAttr);
        }

        appearance.setTexture(texture);

        var white = new Color3f(1.0f, 1.0f, 1.0f);
        var black = new Color3f(0f, 0f, 0f);
        if (emit) {
            appearance.setMaterial(new Material(black, white, black, black, 4.0f));
        } else {
            appearance.setMaterial(new Material(white, black, white, white, 4.0f));
        }

        return appearance;
    }

    static Scene loadModel(String path) {
        var file = new ObjectFile(ObjectFile.RESIZE);
        file.setFlags(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
        try {
            return file.load(new BufferedReader(new InputStreamReader(Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path)))
            );
        } catch (Exception err) {
            err.printStackTrace();
            System.exit(228);
            return null;
        }
    }
}
