package v.e.e.t.a.h.a;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    static class Origin {
        Point2D loc;
        Origin(double x, double y) { loc = new Point2D(x, y); }
        double x(double offset) { return loc.getX() + offset; }
        double y(double offset) { return loc.getY() + offset; }
        Origin add(double offsetX, double offsetY) {
            return new Origin(x(offsetX), y(offsetY));
        }
        Group translate(Group group) {
            group.setTranslateX(loc.getX());
            group.setTranslateY(loc.getY());
            return group;
        }
    }

    Origin o = new Origin(400, 400);

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        var root = new Group();
        var scene = new Scene(root, 1000, 1000);

        var children = root.getChildren();

        children.add(createSmileyFace());

        addSmileMouth(root);
        var leftEye = o.add(-40, -10).translate(createEye());

        var rightEye = o.add(30, -10).translate(createEye());

        rightEye.setScaleX(-1);

        children.addAll(leftEye, rightEye);

        var leftBrow = createBrow();
        leftBrow.setScaleX(1.7);
        leftBrow.setScaleY(1.7);
        leftBrow.setTranslateX(-30);
        leftBrow.setTranslateY(-55);

        var rightBrow = createBrow();
        rightBrow.setScaleX(-1.7);
        rightBrow.setScaleY(1.7);
        rightBrow.setTranslateX(50);
        rightBrow.setTranslateY(-55);


        children.add(o.translate(new Group(leftBrow, rightBrow)));

        attachAnnimation(root);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static class Colors {
        static Color face = Color.rgb(255, 199, 14);
        static Color eye = Color.rgb(0, 156, 227);
    }

    private Shape createSmileyFace() {
        return new Circle(o.x(0), o.y(0), 100, Colors.face);
    }

    private Path createSmileCurve(Origin lo, double height, Color color) {
        var mov = new MoveTo(lo.x(-50), lo.y(0));
        var bezier = new CubicCurveTo(
            lo.x(-40), lo.y(height),
            lo.x(40), lo.y(height),
            lo.x(60), lo.y(0)
        );
        var path = new Path(mov, bezier);
        path.setFill(color);
        path.setStrokeWidth(1);
        return path;
    }

    private void addSmileMouth(Group root) {
        root.getChildren().addAll(
            createSmileCurve(o.add(0, 30), 50, Color.BLACK),
            createSmileCurve(o.add(0, 30), 35, Color.WHITE),
            createSmileCurve(o.add(0, 30), 20, Colors.face)
        );
    }

    private Group createEye() {
        var coloredIris = createEyeShape(Colors.eye);
        coloredIris.setScaleX(0.6);
        coloredIris.setScaleY(0.6);
        coloredIris.setTranslateY(9);

        var iris = createEyeShape(Color.BLACK);
        iris.setScaleX(0.45);
        iris.setScaleY(0.45);
        iris.setTranslateY(12);

        var circle = new Circle(9, -10, 4, Color.AQUA);
        circle.setStrokeWidth(0);

        return new Group(
            createEyeShape(Color.WHITE),
            coloredIris,
            iris,
            circle
        );
    }

    private Path createEyeShape(Color color) {
        var begin = new MoveTo(-14, 6);
        var bezier = new CubicCurveTo(
            -15, -54,
            +25, -57,
            +32, +2
        );
        var path = new Path(begin, bezier);
        path.setFill(color);
        return path;
    }

    private Path createBrow() {
        var p1 = new MoveTo(-15, 0);
        var p2 = new CubicCurveTo(
            -11, -3,
            3, -8,
            6, -2
        );
        var p3 = new CubicCurveTo(
            -11, -3,
            3, -7,
            6, -3
        );

        var path = new Path(
            p1,
            p2,
            p3
        );
        path.setFill(Color.BLACK);
        return path;
    }

    private void attachAnnimation(Group root) {
        int cycleCount = 2;
        int time = 2000;

        var scaleTransition = new ScaleTransition(Duration.millis(time), root);
        scaleTransition.setToX(300);
        scaleTransition.setToY(400);
        scaleTransition.setAutoReverse(true);

        var rotateTransition = new RotateTransition(Duration.millis(time), root);
        rotateTransition.setByAngle(360f);
        rotateTransition.setCycleCount(cycleCount);
        rotateTransition.setAutoReverse(true);

        var translateTransition = new TranslateTransition(Duration.millis(time), root);
        translateTransition.setFromX(150);
        translateTransition.setToX(500);
        translateTransition.setFromY(200);
        translateTransition.setToY(50);
        translateTransition.setCycleCount(cycleCount + 1);
        translateTransition.setAutoReverse(true);

        var scaleTransition2 = new ScaleTransition(Duration.millis(time), root);
        scaleTransition2.setToX(0.1);
        scaleTransition2.setToY(0.1);
        scaleTransition2.setCycleCount(cycleCount);
        scaleTransition2.setAutoReverse(true);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
            scaleTransition2,
            translateTransition,
            rotateTransition,
            scaleTransition
        );
        parallelTransition.setCycleCount(Timeline.INDEFINITE);
        parallelTransition.play();
    }

}
