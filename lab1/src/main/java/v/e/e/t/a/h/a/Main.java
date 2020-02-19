package v.e.e.t.a.h.a;

import javafx.scene.paint.Color;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.shape.*;

public class Main extends Application {

    static class Colors {
        static Color background = Color.SILVER;
        static Color ear = Color.BLUE;
        static Color eye = Color.YELLOW;
        static Color body = Color.rgb(128, 0, 255);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
       primaryStage.setTitle("Hello World!");

        var root = new Group();
        var scene = new Scene(root, 300, 250);

        scene.setFill(Colors.background);
        attachHuman(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void attachHuman(Group root) {
        var body = new Polygon(
            207, 243,
            207, 220,
            197, 220,
            197, 150,
            216, 150,
            216, 80,
            173, 80,
            173, 34,
            120, 34,
            120, 80,
            80, 80,
            80, 155,
            103, 155,
            104, 243,
            150, 243,
            150, 220,
            138, 220,
            138, 174,
            167, 174,
            167, 243
        );
        body.setFill(Colors.body);

        var leftEye = new Rectangle(134, 46, 7, 7);
        var rightEye = new Rectangle(154, 45, 7, 7);
        leftEye.setFill(Colors.eye);
        rightEye.setFill(Colors.eye);

        var leftEar = new Polygon(
            122, 36,
            122, 4,
            92, 14
        );
        var rightEar = new Polygon(
            204, 36,
            172, 6,
            170, 36
        );
        leftEar.setFill(Colors.ear);
        rightEar.setFill(Colors.ear);


        root.getChildren().addAll(
            body, leftEye, rightEye, leftEar, rightEar
        );
    }

}
