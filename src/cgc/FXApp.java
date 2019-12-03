package cgc;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.BorderPane;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import astation.AutomatedStation;
import guest.Guest;
import vehicle.Vehicle;


import java.awt.*;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FXApp extends Application {

    public StackPane root = new StackPane();
    private final double WIDTH = 900;
    private final double HEIGHT = 900;
    private final double RATIO = (WIDTH + HEIGHT)/Math.sqrt(WIDTH/2);
    private double h = 4.71;
    private Cgc controller = new Cgc();
    private Button generateGuestButton;
    private Button emergencyButton;
    private Button voltageMonitorButton;

    public static void main(String[] args) {
        launch(args);
    }




    public void start(Stage primaryStage) throws FileNotFoundException {
        root.setPrefSize(WIDTH,HEIGHT);

        primaryStage.setTitle("Cretaceous Gardens Simulation");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);

        this.generateGuestButton = new Button("Generate Guests");
        this.generateGuestButton.setFont(new Font(15));
        this.generateGuestButton.setStyle("-fx-background-color: #ff0000;");
        StackPane.setAlignment(generateGuestButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(generateGuestButton, new Insets(400,600,150,150));

        this.voltageMonitorButton = new Button("Disable Fence");
        this.voltageMonitorButton.setFont(new Font(15));
        this.voltageMonitorButton.setStyle("-fx-background-color: #fff000;");
        StackPane.setAlignment(voltageMonitorButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(voltageMonitorButton, new Insets(400,600,190,150));

        ImageView imageView = new ImageView(new Image( new FileInputStream("static/img/emergency.png")));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        this.emergencyButton = new Button();
        double r=1.5;
        this.emergencyButton.setShape(new Circle(r));
        this.emergencyButton.setMinSize(2*r, 2*r);
        this.emergencyButton.setMaxSize(2*r, 2*r);
        this.emergencyButton.setGraphic(imageView);
        StackPane.setAlignment(emergencyButton,Pos.BOTTOM_LEFT);
        StackPane.setMargin(emergencyButton, new Insets(400,600,160,50));



        this.generateGuestButton.setOnAction(e->{
            Point p = Zone.DefaultZone.SOUTH_END.getRandomPoint();
            Guest guest = new Guest(p);
            guest.setMovingPoint(p);
            guest.move(p.getX(), p.getY());
            System.out.println("Placed at (" + p.getX() + " ," + p.getY() + ")");
            controller.registerGuest(guest);
            Shape visitorShape = guest.getShape();
            root.getChildren().add(visitorShape);
            visitorShape.setTranslateX(p.getX());
            visitorShape.setTranslateY(p.getY());
        });

        this.voltageMonitorButton.setOnAction(e -> {
            controller.getSecuritySystem().getVoltageMonitor().disable();
        });

       this.emergencyButton.setOnAction(e->{
           controller.handleEvent(new AppUpdate(!controller.isEmergency()));
       });

        startUp();
        zoneStart();
        controller.getSecuritySystem().setAudio("static/audio/siren.mp3");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.0133), event -> {
            gameLoop();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    public void gameLoop() {
        /**
         * Tick system **Keep this first in loop**
         *
         */
        for(Vehicle vehicle : controller.getVehicles()) {
            vehicle.tick();
            vehicle.checkCapacity();
        }

        for(Guest guest : controller.getGuests()) {
            guest.tick();
        }

        for(Zone zone : controller.getZoneList()) {
            Vehicle vehicle = controller.getVehicles().get(0);
            String zoneName = zone.getName();
            if (zoneName.equals(Zone.DefaultZone.PARKING_SOUTH.getName())){
                if (!vehicle.getIntersection(zone) || vehicle.isMoving()) {
                    break;
                }
                for (Guest guest : controller.getGuests()) {
                    if (guest.isInVehicle()) {
                        continue;
                    }
                    guest.setMovingPoint(vehicle.getLocation());
                }
            }
        }



        /**
         * Checking for collision if moving
         * PARKING_NORTH COLLISION
         */

        for(Zone zone : controller.getZoneList()) {
            for (Vehicle vehicle : controller.getVehicles()) {
                String zoneName = zone.getName();
                if (zoneName.equals(Zone.DefaultZone.PARKING_NORTH.getName())){
                    if (vehicle.isMoving() && vehicle.getIntersection(zone) && vehicle.getDestination().equals(zoneName)) {
                        vehicle.setMoving(false);
                        vehicle.resetSecond();
                        vehicle.toggleWaiting();
                        Point parking = Zone.DefaultZone.PARKING_NORTH.getRandomPoint();
                        LinkedList<Guest> guestsToRemove = new LinkedList();
                        for (Guest guest : vehicle.getGuestsInVehicle()) {
                            guestsToRemove.add(guest);
                            guest.getShape().setTranslateX(vehicle.getShape().getTranslateX());
                            guest.getShape().setTranslateY(vehicle.getShape().getTranslateY() - 50);
                            guest.setVisible();
                            guest.setMovingPoint(Zone.DefaultZone.EXHIBIT.getRandomPoint());
                        }
                        // Don't want to concurrently modify the list, so have to
                        // create a separate list
                        for (Guest guest: guestsToRemove) {
                            vehicle.removeFromVehicle(guest);
                        }
                        vehicle.setDestination(Zone.DefaultZone.PARKING_SOUTH.getName());
                    } else if (!vehicle.isMoving() && vehicle.getSecond() > 10 && !vehicle.isWaiting()) {
                        vehicle.toggleWaiting();
                        vehicle.resetSecond();
                    }
                } else if (zoneName.equals(Zone.DefaultZone.PARKING_SOUTH.getName())){
                    if (vehicle.isMoving() && vehicle.getIntersection(zone) && vehicle.getDestination().equals(zoneName)) {
                        vehicle.setMoving(false);
                        vehicle.resetSecond();
                        Point parking = Zone.DefaultZone.PARKING_SOUTH.getRandomPoint();
                        LinkedList<Guest> guestsToRemove = new LinkedList();
                        for (Guest guest : vehicle.getGuestsInVehicle()) {
                            guestsToRemove.add(guest);
                            guest.getShape().setTranslateX(vehicle.getShape().getTranslateX());
                            guest.getShape().setTranslateY(vehicle.getShape().getTranslateY() - 50);
                            guest.setVisible();
                            guest.setMovingPoint(Zone.DefaultZone.SOUTH_END.getRandomPoint());
                        }
                        for (Guest guest : guestsToRemove) {
                            vehicle.removeFromVehicle(guest);
                            // These guests will leave the park after getting to their
                            // next moving point
                            guest.markForExit();
                        }
                        vehicle.setDestination(Zone.DefaultZone.PARKING_NORTH.getName());
                    }
                } else if (zoneName.equals(Zone.DefaultZone.EXHIBIT.getName())){
                    for (Guest guest : controller.getGuests()) {
                        if (!guest.getIntersection(zone) || vehicle.getSecond() < 10){
                            continue;
                        }
                        // TODO Support getting vehicles by zone
                        guest.setMovingPoint(vehicle.getLocation());
                        guest.setInVehicle(false);

                    };
                } else {
                    continue;
                }

            }
        }




        // In case the security system goes down,
        // in a real implementation this would be done in the security
        // systems event loop
        controller.getSecuritySystem().sendStatus();
        for (AutomatedStation station : controller.getStations()) {
            station.sendStatus();
        }
        for (Vehicle vehicle : controller.getVehicles()) {
            // Currently a noop
            vehicle.sendStatus();
            if(vehicle.isMoving()) {
                h += .01;

                vehicle.move(-Math.cos(h) * 2*RATIO, -Math.sin(h) *2* RATIO);
                // Hacky hand-tuned parameter for rotating
                vehicle.rotateVehicle(0.565);
            }


            /**
             * PathFinding Uses guest
             */
            for(Guest guest : controller.getGuests()) {
                if (guest.hasExited()) {
                    guest.setInvisible();
                    continue;
                }
                pathFinding(guest);
                if(!vehicle.isMoving() && guest.getIntersection(vehicle) &&
                   !guest.isInVehicle() && vehicle.getCapacity() < Vehicle.MAX_CAPACITY){
                    vehicle.resetSecond();
                    vehicle.addToVehicle(guest);
                    guest.setInvisible();
                } else if (vehicle.getCapacity() >= Vehicle.MAX_CAPACITY &&
                           !Zone.DefaultZone.SOUTH_END.isWithin(guest.getMovingPoint())){
                    Point newDest = Zone.DefaultZone.SOUTH_END.getRandomPoint();
                    guest.setMovingPoint(newDest);
                }
            }
        }

    }

    public void pathFinding(Guest guest) {
        double x1 = guest.getShape().getTranslateX();
        double y1 = guest.getShape().getTranslateY();
        double x2 = guest.getMovingPoint().getX();
        double y2 = guest.getMovingPoint().getY();
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
        guest.move(x1 + ((distance * xSlope)/10), y1 + ((distance * ySlope)/10));

    }

    public double distance(double x1, double x2, double y1, double y2){
        return Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1)));
    }

    public BorderPane getTreePane(String imageName) throws FileNotFoundException {
        BorderPane treePane = new BorderPane();

        ImageView tree1 = new ImageView(new Image(new FileInputStream(imageName))) ,
                tree2 = new ImageView(new Image(new FileInputStream(imageName))) ,
                tree3 = new ImageView(new Image(new FileInputStream(imageName))) ,
                tree4 = new ImageView(new Image(new FileInputStream(imageName))) ,
                tree5 = new ImageView(new Image(new FileInputStream(imageName)));
        tree1.setFitHeight(75);
        tree2.setFitHeight(75);
        tree3.setFitHeight(75);
        tree4.setFitHeight(75);
        tree5.setFitHeight(75);
        tree1.setFitWidth(75);
        tree2.setFitWidth(75);
        tree3.setFitWidth(75);
        tree4.setFitWidth(75);
        tree5.setFitWidth(75);


        treePane.setTop(tree1);
        treePane.setBottom(tree2);
        treePane.setLeft(tree3);
        treePane.setRight(tree4);
        treePane.setCenter(tree5);

        BorderPane.setAlignment(tree1, Pos.CENTER);
        BorderPane.setAlignment(tree2, Pos.CENTER);

        return treePane;

    }

    public void startUp() throws FileNotFoundException {

        Image carImage = new Image(new FileInputStream("static/img/car.png"));

        Vehicle vehicle = new Vehicle(Zone.DefaultZone.PARKING_SOUTH.getRandomPoint(), carImage);
        vehicle.setDestination(Zone.DefaultZone.PARKING_NORTH.getName());
        controller.register(vehicle);

        // Trees are on the border pane
        // getTreePane method give borderPane with trees

        BorderPane longTreePane = getTreePane("static/img/longTree.png");
        StackPane.setAlignment(longTreePane,Pos.CENTER_LEFT);
        StackPane.setMargin(longTreePane, new Insets(300,50,650,650));

        BorderPane shortTreePane = getTreePane("static/img/shortTree.png");
        StackPane.setAlignment(shortTreePane,Pos.CENTER_RIGHT);
        StackPane.setMargin(shortTreePane, new Insets(50,600,50,0));



        ImageView pathImage = new ImageView(new Image(new FileInputStream("static/img/circularRoad.png")));

        pathImage.setFitWidth(5*RATIO);
        pathImage.setFitHeight(5*RATIO);
        StackPane.setAlignment(pathImage, Pos.CENTER);

        ImageView grassImage = new ImageView(new Image(new FileInputStream("static/img/Grass.png")));
        grassImage.setFitWidth(900);
        grassImage.setFitHeight(900);

        ImageView trexImage = new ImageView(new Image(new FileInputStream("static/img/alice.gif")));
        trexImage.setFitHeight(80);
        trexImage.setFitWidth(150);
        StackPane.setAlignment(trexImage,Pos.TOP_CENTER);
        StackPane.setMargin(trexImage, new Insets(100));

        ImageView fenceImage = new ImageView(new Image(new FileInputStream("static/img/fence.png")));
        fenceImage.setFitHeight(200);
        fenceImage.setFitWidth(300);
        fenceImage.setRotate(180.0);
        StackPane.setAlignment(fenceImage,Pos.TOP_CENTER);
        StackPane.setMargin(fenceImage, new Insets(50));

        ImageView payStationImage = new ImageView(new Image(new FileInputStream("static/img/payStation.png")));
        payStationImage.setFitWidth(50);
        payStationImage.setFitHeight(50);
        StackPane.setAlignment(payStationImage, Pos.BOTTOM_CENTER);
        StackPane.setMargin(payStationImage, new Insets(0,0,100,0));

        ImageView waterImage = new ImageView(new Image(new FileInputStream("static/img/water.png")));
        waterImage.setFitWidth(200);
        waterImage.setFitHeight(200);
        StackPane.setAlignment(waterImage, Pos.CENTER);
        StackPane.setMargin(waterImage, new Insets(50));

        root.getChildren().addAll(
                grassImage,
                longTreePane,
                shortTreePane,
                generateGuestButton,
                voltageMonitorButton,
                emergencyButton,
                pathImage,
                trexImage,
                fenceImage,
                payStationImage,
                waterImage,
                vehicle.getShape(),
                vehicle.getText(),
                vehicle.getTimerText()
        );
    }


    public void zoneStart() {
        /**
         * Zone Order : PARKING_SOUTH, PARKING_NORTH,
         *              SOUTH_END, EXHIBIT, refer to Zone.DefaultZone Enum
         * ZONES BEING COLORED ON THE GUI IS TEMPRORARY, JUST FOR DEBUGGING PURPOSES AT THE MOMENT
         */
        for(Zone.DefaultZone defZone : Zone.DefaultZone.values()){
            Zone zone = new Zone(
                defZone.getLocation(),
                defZone.getWidth(),
                defZone.getHeight(),
                defZone.getName()
            );
            controller.registerZone(zone);
            root.getChildren().add(zone.getShape());
        }

    }

}