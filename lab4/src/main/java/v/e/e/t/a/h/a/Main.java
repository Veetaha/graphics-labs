package v.e.e.t.a.h.a;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main  extends Applet implements ActionListener {
    static class Textures {
        static final Texture earth = loadTexture("earth_texture.jpg");
        static final Texture jupiter = loadTexture("jupiter_texture.jpg");
        static final Texture mars = loadTexture("mars_texture.jpg");
        static final Texture sun = loadTexture("sun_texture.jpg");
        static final Texture uranus = loadTexture("uranus_texture.jpg");
        static final Texture venus = loadTexture("venus_texture.jpg");

        static Texture loadTexture(String resourceName) {
            var resourcesDir = "/home/veetaha/dev/graphics-labs/lab4/src/main/resources/" + resourceName;
            return new TextureLoader(resourcesDir, new Container()).getTexture();
        }
    }
    static class SphericalSpaceBody {
        TransformGroup tg;
        float orbitRotVelocity;
        float selfRotVelocity;
        float orbitAngle;
        float selfRotAngle = 0;
        SphericalSpaceBody(TransformGroup tg, float orbitRotVelocity, float selfRotVelocity, float orbitAngle) {
            this.tg = tg;
            this.orbitRotVelocity = orbitRotVelocity;
            this.selfRotVelocity = selfRotVelocity;
            this.orbitAngle = orbitAngle;
        }
    }

    private final Timer timer = new Timer(16, this);
    SphericalSpaceBody[] spaceBodies = new SphericalSpaceBody[] {
            new SphericalSpaceBody(createSun(0.15), 0, 0.001f, 0),
            new SphericalSpaceBody(
                createPlanet(Textures.earth, new Vector3f(0.5f, 0, 0), 0.03),
                0.001f,
                0.01f,
                0.004f
            ),
            new SphericalSpaceBody(
                createPlanet(Textures.venus, new Vector3f(0.2f, 0, 0), 0.02),
                    0.001f,
                0.01f,
                0.010f
            ),
            new SphericalSpaceBody(
                createPlanet(Textures.mars, new Vector3f(0.6f, 0, 0), 0.045),
                    0.001f,
                0.01f,
                    0.005f
            ),
            new SphericalSpaceBody(
                createPlanet(Textures.jupiter, new Vector3f(0.8f, 0, 0), 0.1),
                0.001f,
                0.01f,
                0.003f
            ),
            new SphericalSpaceBody(
                createPlanet(Textures.uranus, new Vector3f(0.9f, 0, 0), 0.07),
                0.0001f,
                0.01f,
                0.002f
            )
    };

    public static void main(String[] args) {
        new MainFrame(new Main(), 1920, 1080).run();
    }

    public Main() {
        setLayout(new BorderLayout());
        var canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        add("Center", canvas);

        var universe = new SimpleUniverse(canvas);
        var rootGroup = new BranchGroup();

        for (var spaceBody : spaceBodies) {
            rootGroup.addChild(spaceBody.tg);

            rootGroup.addChild(createOrbitTorus(getTranslate(spaceBody.tg).length()));
        }

        // Set light
        {
            var bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100);

            var sunLightColor = new Color(200, 255, 253);
            var lightDirect = new DirectionalLight(new Color3f(sunLightColor), new Vector3f(4.0f, -7.0f, -12.0f));
            lightDirect.setInfluencingBounds(bounds);
            rootGroup.addChild(lightDirect);

            var ambientLightNode = new AmbientLight(new Color3f(new Color(255, 226, 142)));
            ambientLightNode.setInfluencingBounds(bounds);
            rootGroup.addChild(ambientLightNode);
        }
        // Set camera position
        {

            var viewingTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
            var viewingTransform = new Transform3D();

            Point3d eye = new Point3d(0, 0.6, 2.3);
            Vector3d up = new Vector3d(0, 1, 0);
            viewingTransform.lookAt(eye, new Point3d(0, 0, 0), up);
            viewingTransform.invert();
            viewingTransformGroup.setTransform(viewingTransform);


        }
        universe.addBranchGraph(rootGroup);

        timer.start();
    }

    TransformGroup createSun(double scale) {
        var sunBody = createSpaceSphericalBody(Textures.sun, true);

        var sunTransform = new Transform3D();
        sunTransform.setScale(scale);

        var light = new PointLight();
        light.setColor(new Color3f(255, 255, 255));

        var g = transform(sunBody, sunTransform);
        g.addChild(light);
        return g;
    }

    TransformGroup createPlanet(Texture texture, Vector3f trasnlation, double scale) {
        var planet = createSpaceSphericalBody(texture, false);
        var transform = new Transform3D();
        transform.setScale(scale);
        transform.setTranslation(trasnlation);

        return transform(planet, transform);
    }

    TransformGroup transform(Node node, Transform3D transformation) {
        var tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

        tg.setTransform(transformation);
        tg.addChild(node);
        return tg;
    }

    BranchGroup createSpaceSphericalBody(Texture texture, boolean isEmissive) {
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 1.0f, 0.0f));
        var texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);

        var ap = new Appearance();

        ap.setTexture(texture);
        ap.setTextureAttributes(texAttr);

        var material = new Material();
        if (isEmissive) {
            material.setEmissiveColor(255, 255, 255);
        }
        ap.setMaterial(material);

        int primFlags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;

        var sphere = new Sphere(0.5f, primFlags, ap);

        var group = new BranchGroup();
        group.addChild(sphere);

        return group;
    }

    protected Node createOrbitTorus(float radius) {
        return new Torus(radius, 0.001f, 50, 50);
    }

    static Transform3D getTransform(TransformGroup tg) {
        var transform = new Transform3D();
        tg.getTransform(transform);
        return transform;
    }

    static Vector3f getTranslate(TransformGroup tg){
        var result = new Vector3f();
        getTransform(tg).get(result);
        return result;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        for (var spaceBody: spaceBodies) {
            var transform = getTransform(spaceBody.tg);
            {
                spaceBody.selfRotAngle += spaceBody.selfRotVelocity % 2 * Math.PI;
                transform.setRotation(new AxisAngle4f(new Vector3f(0, 1, 0),spaceBody.selfRotAngle));
            }
            {
                var orbitRot = new Transform3D();
                orbitRot.rotY(spaceBody.orbitAngle);
                transform.mul(orbitRot, transform);
            }
            spaceBody.tg.setTransform(transform);
        }
    }
}


// Taken from https://github.com/lemtzas/Java3D-Project-1/blob/master/Project%201/src/objects/Torus.java
// :D
class Torus extends Shape3D {
    public Torus(float majorRadius, float minorRadius, int majorSamples, int minorSamples) {
        setGeometry(createGeometry(majorRadius, minorRadius, majorSamples, minorSamples));

        Appearance meshApp = new Appearance();
        Material meshMat = new Material();
        meshMat.setDiffuseColor(0f, 1f, 0f);
        meshApp.setMaterial(meshMat);
        meshApp.setColoringAttributes(new ColoringAttributes(0f, 0f, 0f, ColoringAttributes.SHADE_GOURAUD));
        setAppearance(meshApp);
    }

    private Geometry createGeometry(float majorRadius, float minorRadius, int majorSamples, int minorSamples) {
        IndexedQuadArray geometry = new IndexedQuadArray(majorSamples * minorSamples, GeometryArray.COORDINATES, 4 * majorSamples * minorSamples);

        Point3f[] vertices = new Point3f[majorSamples * minorSamples];
        for (int i = 0; i < minorSamples; i++)
            vertices[i] = new Point3f((float)Math.cos(i * 2 * Math.PI / minorSamples) * minorRadius + majorRadius, (float)Math.sin(i * 2 * Math.PI / minorSamples) * minorRadius, 0);

        for (int i = 1; i < majorSamples; i++) {
            Transform3D t3d = new Transform3D();
            t3d.rotY(i * 2 * Math.PI / majorSamples);
            for (int j = 0; j < minorSamples; j++) {
                vertices[i * minorSamples + j] = new Point3f();
                t3d.transform(vertices[j], vertices[i * minorSamples + j]);
            }
        }

        int[] quadIndices = new int[4 * majorSamples * minorSamples];
        for (int i = 0; i < majorSamples; i++)
            for (int j = 0; j < minorSamples; j++) {
                quadIndices[4 * (i * minorSamples + j)] = i * minorSamples + j;
                quadIndices[4 * (i * minorSamples + j) + 1] = (i + 1) % majorSamples * minorSamples + j;
                quadIndices[4 * (i * minorSamples + j) + 2] = (i + 1) % majorSamples * minorSamples + (j + 1) % minorSamples;
                quadIndices[4 * (i * minorSamples + j) + 3] = i * minorSamples + (j + 1) % minorSamples;
            }

        geometry.setCoordinates(0, vertices);
        geometry.setCoordinateIndices(0, quadIndices);

        // Utility code to automatically generate normals.
        GeometryInfo gInfo = new GeometryInfo(geometry);
        new NormalGenerator().generateNormals(gInfo);
        return gInfo.getIndexedGeometryArray();
    }
}
