package cgc;

import astation.AutomatedStation;
import guest.Guest;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import javafx.util.Duration;
import vehicle.Vehicle;

import java.awt.*;
import java.util.LinkedList;

public class Cgc extends Application {

    public StackPane root = new StackPane();
    private static LinkedList<String> uuidList;
    private AutomatedStation aStation = new AutomatedStation();
    private Boolean DEBUG = true;
    private double i = 0.0;

    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Testing | Don't change
     * VVVVVVV
     */
    public void startUp() {
        Guest g = new Guest("Nice", new Point(0, 0));
        Vehicle v = new Vehicle(new Point(10, 10));
        g.move(0, 0);
        v.move(80, 80);
        aStation.addGuest(g);
        aStation.addVehicle(v);
        root.getChildren().addAll(g.getC(), v.getR());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cretaceous Gardens Simulation");

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
        startUp();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.0133), event -> {
            testLoop();

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    public void testLoop() {

        Vehicle v = aStation.getVehicleList().get(0);
        Guest g = aStation.getGuestlist().get(0);

        i += .1;
        g.move(Math.cos(i)*50, Math.sin(i)*50);
        v.move(-Math.cos(i)*50, -Math.sin(i)*50);


    }


}