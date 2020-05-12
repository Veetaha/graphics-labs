package v.e.e.t.a.h.a;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Main extends JFrame {
    private static class Resources {
                private static class Paths {
                    private static String wolfObj = "wolf/wolf.obj";
                    private static String wolfTexture = "wolf/texture.png";
                    private static String background = "wolf/forest.jpg";
        }

        private static TextureLoader textureLoader(String path, Canvas3D canvas) throws IOException {
            var url = Thread.currentThread().getContextClassLoader().getResource(path);
            return new TextureLoader(url.getPath(), canvas);
        }

        private static Texture loadTexture(String path, Canvas3D canvas) throws IOException {
            var texture = textureLoader(path, canvas).getTexture();
            texture.setBoundaryModeT(Texture.WRAP);
            texture.setBoundaryModeS(Texture.WRAP);
            texture.setBoundaryColor(new Color4f(1.0f, 1.0f, 1.0f, 1.0f));
            return texture;
        }

        private static Material createMaterial() {
            var material = new Material();
            material.setAmbientColor(new Color3f(new Color(243, 115, 124)));
            material.setDiffuseColor(new Color3f(new Color(145, 242, 207)));
            material.setSpecularColor(new Color3f(new Color(195, 141, 155)));
            material.setLightingEnable(true);
            return material;
        }

        private static Scene loadModel(String path) throws IOException {
            ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
            file.setFlags(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
            return file.load(new BufferedReader(new InputStreamReader(Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path)))
            );
        }
    }


    private SimpleUniverse universe;
    private Scene wolf;
    private BranchGroup root;
    private Canvas3D canvas;
    private TransformGroup wholeWolf = new TransformGroup();

    private Background background;
    private Map<String, Shape3D> nameMap;

    private Animation animation;
    public static void main(String[] args) {
        try {
            new Main();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Main() throws IOException {
        {
            this.setTitle("VOLK");
            this.setSize(1000, 700);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        {
            this.canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
            this.canvas.setDoubleBufferEnable(true);
            this.getContentPane().add(canvas, BorderLayout.CENTER);

            this.root = new BranchGroup();
            this.universe = new SimpleUniverse(canvas);
            this.universe.getViewingPlatform().setNominalViewingTransform();
        }
        {
            wolf = Resources.loadModel(Resources.Paths.wolfObj);
        }
        {
            nameMap = wolf.getNamedObjects();
            wholeWolf.addChild(wolf.getSceneGroup());
            wholeWolf.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            root.addChild(wholeWolf);
        }
        {
            var wolfAppearance = new Appearance();
            wolfAppearance.setTexture(Resources.loadTexture(Resources.Paths.wolfTexture, canvas));

            var attrs = new TextureAttributes();
            attrs.setTextureMode(TextureAttributes.MODULATE);

            wolfAppearance.setTextureAttributes(attrs);
            wolfAppearance.setMaterial(Resources.createMaterial());

            var plane = nameMap.get("wolf1_material__wolf_col_tga_0");
            plane.setAppearance(wolfAppearance);
        }
        {
            background = new Background(Resources.textureLoader(Resources.Paths.background, canvas).getImage());
            background.setImageScaleMode(Background.SCALE_FIT_MAX);
            background.setApplicationBounds(new BoundingSphere(new Point3d(), 2000));
            background.setCapability(Background.ALLOW_IMAGE_WRITE);
            root.addChild(background);
        }
        {
            var bounds = new BoundingSphere();
            var dirlight = new DirectionalLight(
                new Color3f(Color.WHITE),
                new Vector3f(4.0f, -7.0f, -12.0f)
            );
            dirlight.setInfluencingBounds(bounds);
            root.addChild(dirlight);

        }
        { // More light
            var ambientLight = new AmbientLight(new Color3f(Color.WHITE));
            var directionalLight = new DirectionalLight(
                    new Color3f(Color.BLACK),
                    new Vector3f(-1F, -1F, -1F)
            );
            var influenceRegion = new BoundingSphere(new Point3d(), 1000);
            ambientLight.setInfluencingBounds(influenceRegion);
            directionalLight.setInfluencingBounds(influenceRegion);
            root.addChild(ambientLight);
            root.addChild(directionalLight);
        }
        {
            root.compile();
            universe.addBranchGraph(root);
        }
        {
            // set this last!
            this.setVisible(true);
        }
        Transform3D tr = new Transform3D();
        wholeWolf.getTransform(tr);
        animation = new Animation(canvas, wholeWolf, tr, this);
    }
}
