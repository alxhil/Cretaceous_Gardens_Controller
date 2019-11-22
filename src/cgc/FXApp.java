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
import javafx.scene.layout.Pane;
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

    public Pane root = new Pane();
    private Boolean DEBUG = true;
    private Boolean DEBUGSTART = false;
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
        v.move(450, 470);
        controller.registerResource(v);
        Circle c = new Circle(WIDTH/2,HEIGHT/2 , RATIO);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.BLACK);
        root.getChildren().addAll(v.getR(), c, v.getText());
    }

    @Override
    public void start(Stage primaryStage) {
        root.setPrefSize(WIDTH,HEIGHT);
        primaryStage.setTitle("Cretaceous Gardens Simulation");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int mouseX = (int) mouseEvent.getSceneX();
                int mouseY = (int) mouseEvent.getSceneY();
                System.out.println("Mouse x: "+mouseX+" Mouse y: "+mouseY);
                Guest g = new Guest(new Point(mouseX,mouseY));
                g.move(mouseX,mouseY);
                System.out.println("Placed at ("+ g.getLocation().getX()+" ,"+ g.getLocation().getY()+")");
                controller.registerGuest(g);
                root.getChildren().add(g.getC());
                g.getC().setTranslateX(mouseX);
                g.getC().setTranslateY(mouseY);
            }
        });
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.V) {
                    DEBUGSTART = true;
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

        for(Vehicle v : controller.getVehicles()) {
            //if(v.getLocation().)
        }

        for (Vehicle v : controller.getVehicles()) {
            if(v.isMoving()) {
                h += .01;
                //System.out.println("moving");
                v.move(-Math.cos(h) * RATIO, -Math.sin(h) * RATIO);
                continue;
            }




            if(DEBUGSTART){
                int rX = (int) (Math.random()*999);
                int rY = (int) (Math.random()*999);
                Guest g = new Guest(new Point(rX, rY));
                controller.registerGuest(g);
                g.move(rX,rY);
                root.getChildren().add(g.getC());
            }
            for(Guest guest : controller.getGuests()) {
                pathFinding(guest, v.getLocation());
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

        double x1 = g.getC().getTranslateX();
        double y1 = g.getC().getTranslateY();
        double x2 = p.getX();
        double y2 = p.getY();
        double distance = distance(x1,x2,y1,y2);

        double xSlope = 0;
        double ySlope = 0;

        if (y2 > y1){
            ySlope = .1;
        } else if (y2 == y1) {
            ySlope = 0;
        } else {
            ySlope = -.1;
        }


        if(x2 > x1) {
            xSlope = .1;
        } else if (x2 == x1) {
            xSlope = 0;
        } else {
            xSlope = -.1;
        }
        System.out.println("x1: " +x1+" y1: "+y1 +" x2: "+x2+" y2: "+y2);
        System.out.println("xSlope: " +xSlope+" ySlope: "+ySlope);
        System.out.println("moving to (" +(g.getC().getTranslateX() +( (distance) * xSlope))+") , ("+(g.getC().getTranslateY() + ((distance) * ySlope)) );
        g.move(g.getC().getTranslateX() +( (distance) * xSlope), g.getC().getTranslateY() + ((distance) * ySlope));

    }

    public double distance(double x1, double x2, double y1, double y2){
        return Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1)));
    }


}