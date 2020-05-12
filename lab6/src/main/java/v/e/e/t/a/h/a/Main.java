package v.e.e.t.a.h.a;

import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JFrame;
import com.sun.j3d.loaders.*;

import java.util.Hashtable;

class Main extends JFrame {

    private Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
    private Hashtable<String, Object> nameMap;

    public static void main(String[] args){
        new Main();
    }
    private Main() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var simpUniv = new SimpleUniverse(canvas);
        simpUniv.getViewingPlatform().setNominalViewingTransform();

        createSceneGraph(simpUniv);
        configureLight(simpUniv);

        var beh = new OrbitBehavior(canvas);
        beh.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE));
        simpUniv.getViewingPlatform().setViewPlatformBehavior(beh);

        setTitle("LAB 6 LADYBUG cracked by std::Veetaha");
        setSize(1920, 1080);
        getContentPane().add("Center", canvas);
        setVisible(true);
    }

    private void createSceneGraph(SimpleUniverse su) {
        Scene bugScene = Resources.loadModel(Resources.Paths.ladybug.obj);


        Transform3D scaling = new Transform3D();
        scaling.setScale(0.2);

        Transform3D ladybugTransform3D = new Transform3D();
        ladybugTransform3D.rotY(+2.7 * Math.PI / 2);
        ladybugTransform3D.rotX(0.5);
        ladybugTransform3D.mul(scaling);
        // ladybugTransform3D.setTranslation(new Vector3d(+0.4f, 0.1f, 0.0f));

        var ladybug = new TransformGroup(ladybugTransform3D);

        var sceneGroup = new TransformGroup();

        nameMap = bugScene.getNamedObjects();

        var scene = new BranchGroup();

        var tgBody = new TransformGroup();
        var ladyBugBody = (Shape3D) nameMap.get("ladybug");
        ladyBugBody.setAppearance(Resources.loadTexture(Resources.Paths.ladybug.texture, true));
        tgBody.addChild(ladyBugBody.cloneTree());


        sceneGroup.addChild(createLeg("leg1", (float) Math.PI / 8, 100));
        sceneGroup.addChild(createLeg("leg2", (float) Math.PI / 8, 200));
        sceneGroup.addChild(createLeg("leg3", (float) Math.PI / 8, 300));
        sceneGroup.addChild(createLeg("leg4", -(float) Math.PI / 8, 100));
        sceneGroup.addChild(createLeg("leg5", -(float) Math.PI / 8, 200));
        sceneGroup.addChild(createLeg("leg6", -(float) Math.PI / 8, 300));

        sceneGroup.addChild(tgBody.cloneTree());

        var crawling = new Transform3D();
        crawling.rotY(-Math.PI/2);

        long crawlTime = 20000;
        var crawlAlpha = new Alpha(1, Alpha.INCREASING_ENABLE, 0, 0, crawlTime, 0, 0, 0, 0, 0);

        float crawlDistance = 10.0f;
        var posICrawl = new PositionInterpolator(crawlAlpha, sceneGroup, crawling, -3.0f, crawlDistance);

        posICrawl.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE));
        sceneGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sceneGroup.addChild(posICrawl);

        ladybug.addChild(sceneGroup);
        scene.addChild(ladybug);

        configureBackground(scene);
        scene.compile();

        su.addBranchGraph(scene);
    }

    void configureBackground(BranchGroup root) {
        var background = new Background(Resources.textureLoader(Resources.Paths.background, canvas).getImage());
        background.setImageScaleMode(Background.SCALE_FIT_ALL);
        background.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        root.addChild(background);
    }

    TransformGroup createLeg(String elementName, float vel, int l) {
        int timeRotationHour = 300;
        var boundingSphere = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);

        var legRotAxis = new Transform3D();
        legRotAxis.rotZ(Math.PI / 2);
        var leg2RotAxis = new Transform3D();

        var alpha = new Alpha(
            1000,
            Alpha.INCREASING_ENABLE,
            l,
            0,
            timeRotationHour,
            0,
            0,
            0,
            0,
            0
        );

        var leg = new TransformGroup();
        leg.addChild(((Shape3D) nameMap.get(elementName)).cloneTree());

        var legRotation = new RotationInterpolator(alpha , leg, legRotAxis, vel, 0.0f);
        legRotation.setSchedulingBounds(boundingSphere);
        leg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        leg.addChild(legRotation);
        return leg;
    }

    void configureLight(SimpleUniverse universe){
        var light = new DirectionalLight(new Color3f(1, 1, 1), new Vector3f(-1, 0, -0.5f));
        light.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100));

        var branchGroup = new BranchGroup();
        branchGroup.addChild(light);
        universe.addBranchGraph(branchGroup);
    }
}
