package cgc;

import astation.AutomatedStation;
import guest.Guest;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
    private final double WIDTH = 900;
    private final double HEIGHT = 900;
    private final double RATIO = (WIDTH + HEIGHT)/Math.sqrt(WIDTH/2);
    private int guestCount = 0;
    private double h = 4.71;

    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Testing | Don't change
     * VVVVVVV
     */
    public void startUp() {

        Circle c = new Circle(0,0 , RATIO);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.BLACK);

        Vehicle v = new Vehicle(new Point(10, 10));
        v.move(Math.cos(4.71), Math.sin(4.71));
        aStation.addVehicle(v);
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
                guestCount++;
                int mouseX = (int) mouseEvent.getX();
                int mouseY = (int) mouseEvent.getY();
                System.out.println("Mouse x: "+mouseX+" Mouse y: "+mouseY);
                Guest g = new Guest(guestCount, new Point(mouseX,mouseY));
                root.getChildren().add(g.getC());
                g.getC().setTranslateX(mouseX);
                g.getC().setTranslateY(mouseY);
                aStation.addGuest(g);
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

        Vehicle v = aStation.getVehicleList().get(0);




        for(int i = 0; i < aStation.getGuestlist().size(); i++) {
            pathFinding(aStation.getGuestlist().get(i));
        }

        for(int i = 0; i < aStation.getGuestlist().size(); i++) {
            //System.out.println(""+aStation.getGuestlist().get(i).getIntersection(v));
            if(aStation.getGuestlist().get(i).getIntersection(v) && !aStation.getGuestlist().get(i).getInVehicle()){
                //System.out.println("debug 1");
                v.addToVehicle(aStation.getGuestlist().get(i));
            }
            //System.out.println(i);
        }

        if(v.isMoving()) {
            h += .01;
            //System.out.println("moving");
            v.move(-Math.cos(h) * RATIO, -Math.sin(h) * RATIO);
        }

    }

    public void pathFinding(Guest g) {
        LinkedList<Vehicle> vList = aStation.getVehicleList();
        int nearestV;
        double temp = 0;

        double x1 = g.getLocation().getX();
        double y1 = g.getLocation().getY();
        double x2 = vList.get(0).getLocation().getX();
        double y2 = vList.get(0).getLocation().getY();
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
        if(!vList.get(0).isMoving() || g.getInVehicle()) {
            g.move(g.getLocation().getX() + (distance) * xSlope, g.getLocation().getY() + ((distance) * ySlope));
        }

    }

    public double distance(double x1, double x2, double y1, double y2){

        double distance = Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1)));
        //System.out.println(distance);
        return distance;
    }




}