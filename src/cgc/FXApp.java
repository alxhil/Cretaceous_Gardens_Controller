package cgc;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import javafx.util.Duration;

import astation.AutomatedStation;
import cgc.Cgc;
import guest.Guest;
import vehicle.Vehicle;

import java.awt.*;
import java.util.LinkedList;

public class FXApp extends Application {

    public StackPane root = new StackPane();
    private Boolean DEBUG = true;
    private final double WIDTH = 900;
    private final double HEIGHT = 900;
    private final double RATIO = (WIDTH + HEIGHT)/Math.sqrt(WIDTH/2);
    private final Point NORTH_END = new Point(40, 40);
    private final Point SOUTH_END = new Point(10, 10);
    private double h = 4.71;
    private Cgc controller = new Cgc();

    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Testing | Don't change
     * VVVVVVV
     */
    public void startUp() {
        Vehicle v = new Vehicle(new Point(10, 10));
        v.move(Math.cos(4.71), Math.sin(4.71));
        controller.registerResource(v);
        Circle c = new Circle(0,0 , RATIO);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.BLACK);
        root.getChildren().addAll(v.getR(), c, v.getText());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cretaceous Gardens Simulation");

        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
        primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int mouseX = (int) mouseEvent.getX();
                int mouseY = (int) mouseEvent.getY();
                System.out.println("Mouse x: "+mouseX+" Mouse y: "+mouseY);
                Guest g = new Guest(new Point(mouseX,mouseY));
                controller.registerGuest(g);
                root.getChildren().add(g.getC());
                g.getC().setTranslateX(mouseX);
                g.getC().setTranslateY(mouseY);
            }
        });
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.V) {
                    for(Guest g : controller.getGuests()) {
                        g.setVisible();
                    }
                }
            }
        });
        startUp();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.0133), event -> {
            testLoop();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public void testLoop() {
        for (Vehicle v : controller.getVehicles()) {
            if(v.isMoving()) {
                h += .01;
                //System.out.println("moving");
                v.move(-Math.cos(h) * RATIO, -Math.sin(h) * RATIO);
                continue;
            }
            for(Guest guest : controller.getGuests()) {
                pathFinding(guest, new Point(controller.getVehicles().get(0).getLocation().x,
                        controller.getVehicles().get(0).getLocation().y));
            }

            for(Guest guest : controller.getGuests()) {
                if(guest.getIntersection(v) && !guest.isInVehicle()){
                    v.addToVehicle(guest);
                    guest.setInvisible();
                }
            }

        }

    }

    public void pathFinding(Guest g, Point p) {
        double x1 = g.getLocation().getX();
        double y1 = g.getLocation().getY();
        double x2 = p.getX();
        double y2 = p.getY();
        double distance = distance(x1,x2,y1,y2);

        double xSlope = 0;
        double ySlope = 0;

        if(y2 > y1){
            ySlope = .1;
        } else {
            ySlope = -.1;
        }


        if(x2 > x1) {
            xSlope = .1;
        } else {
            xSlope = -.1;
        }
        g.move(g.getLocation().getX() + (distance) * xSlope, g.getLocation().getY() + ((distance) * ySlope));
    }

    public double distance(double x1, double x2, double y1, double y2){
        return Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1)));
    }


}