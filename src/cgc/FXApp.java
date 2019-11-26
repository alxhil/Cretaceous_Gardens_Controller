package cgc;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.BorderPane;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import astation.AutomatedStation;
import guest.Guest;
import vehicle.Vehicle;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FXApp extends Application {

    public StackPane root = new StackPane();
    private final double WIDTH = 900;
    private final double HEIGHT = 900;
    private final double RATIO = (WIDTH + HEIGHT)/Math.sqrt(WIDTH/2);
    private double h = 4.71;
    private Cgc controller = new Cgc();

    public static void main(String[] args) {
        launch(args);
    }




    public void start(Stage primaryStage) throws FileNotFoundException {
        root.setPrefSize(WIDTH,HEIGHT);

        primaryStage.setTitle("Cretaceous Gardens Simulation");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                Point p = Zone.DefaultZone.SOUTH_END.getRandomPoint();
                Guest guest = new Guest(p);
                guest.move(p.getX(), p.getY());
                System.out.println("Placed at (" + p.getX() + " ," + p.getY() + ")");
                controller.registerGuest(guest);
                Shape visitorShape = guest.getShape();
                root.getChildren().add(visitorShape);
                visitorShape.setTranslateX(p.getX());
                visitorShape.setTranslateY(p.getY());
            }
        });
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.V) {
                    //DEBUGSTART = true;
                } else if (ke.getCode() == KeyCode.E) {
                    controller.handleEvent(new AppUpdate(true));
                } else if (ke.getCode() == KeyCode.R) {
                    controller.handleEvent(new AppUpdate(false));
                }

            }
        });
        startUp();
        zoneStart();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.0133), event -> {
            gameLoop();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    public void gameLoop() {
        // In case the security system goes down,
        // in a real implementation this would be done in the security
        // systems event loop



        /**
         * Tick system **Keep this first in loop**
         *
         */
        for(Vehicle vehicle : controller.getVehicles()) {
            vehicle.tick();
        }

        for(Guest guest : controller.getGuests()) {
            guest.tick();
        }





        for(Guest guest : controller.getGuests()) {
            if(!guest.isInVehicle()){
                guest.setMovingPoint(controller.getVehicles().get(0).getLocation());
            }
        }


        /**
         * Checking for collision if moving
         * PARKING_NORTH COLLISION
         */

        for(Zone zone : controller.getZoneList()) {
            for (Vehicle vehicle : controller.getVehicles()) {
                if (vehicle.isMoving()) {


                    if (vehicle.getIntersection(zone) && vehicle.isMoving() && zone.getName().equalsIgnoreCase("parking_north") && !vehicle.getIntermission()) {
                        vehicle.setMoving(false);
                        vehicle.resetSecond();
                        Point parking = Zone.DefaultZone.PARKING_NORTH.getRandomPoint();
                        vehicle.move(parking.getX(), parking.getY());
                        for (Guest guest : controller.getGuests()) {
                            if (vehicle.getGuestsInVehicle().contains(guest)) {

                                vehicle.removeFromVehicle(guest);
                                guest.getShape().setTranslateX(vehicle.getShape().getTranslateX());
                                guest.getShape().setTranslateY(vehicle.getShape().getTranslateY() - 50);
                                guest.setVisible();
                                guest.setMovingPoint(Zone.DefaultZone.EXHIBIT.getRandomPoint());

                            }
                        }
                    }
                    else if (vehicle.getIntersection(zone) && vehicle.isMoving() && zone.getName().equalsIgnoreCase("parking_south") && vehicle.getIntermission()) {
                        vehicle.setIntermission();
                    }
                } else if (vehicle.getIntersection(zone) && !vehicle.isMoving() && zone.getName().equalsIgnoreCase("parking_north")
                        && vehicle.getSecond() >= 10) {
                    vehicle.resetSecond();
                    vehicle.setIntermission();

                    for (Guest guest : controller.getGuests()) {

                        guest.setMovingPoint(vehicle.getLocation());
                        guest.setInVehicle(false);

                    }
                }
            }
        }




        controller.getSecuritySystem().sendStatus();
        for (AutomatedStation station : controller.getStations()) {
            station.sendStatus();
        }
        for (Vehicle vehicle : controller.getVehicles()) {
            // Currently a noop
            vehicle.sendStatus();
            if(vehicle.isMoving()) {
                h += .01;

                //System.out.println("moving");

                vehicle.move(-Math.cos(h) * 2*RATIO, -Math.sin(h) *2* RATIO);
                //v.rotateVehicle(Math.asin(-h));


                vehicle.move(-Math.cos(h) * 2*RATIO, -Math.sin(h) *2* RATIO);
                // Hacky hand-tuned parameter for rotating
                vehicle.rotateVehicle(0.57);
               continue;

            }


            /**
             * PathFinding Uses guest
             */
            for(Guest guest : controller.getGuests()) {
                pathFinding(guest);
                if(guest.getIntersection(vehicle) && !guest.isInVehicle()){
                    vehicle.addToVehicle(guest);
                    vehicle.resetSecond();
                    guest.setInvisible();
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
        guest.move(x1 + ((distance * xSlope)/5), y1 + ((distance * ySlope)/5));

    }

    public double distance(double x1, double x2, double y1, double y2){
        return Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1)));
    }

    public BorderPane getTreePane(String imageName) throws FileNotFoundException {
        BorderPane treePane = new BorderPane();

        ImageView tree1 = new ImageView(new Image(new FileInputStream(imageName)));
        tree1.setFitHeight(50);
        ImageView tree2 = new ImageView(new Image(new FileInputStream(imageName)));
        tree2.setFitHeight(50);
        ImageView tree3 = new ImageView(new Image(new FileInputStream(imageName)));
        tree3.setFitHeight(50);
        ImageView tree4 = new ImageView(new Image(new FileInputStream(imageName)));
        tree4.setFitHeight(50);
        ImageView tree5 = new ImageView(new Image(new FileInputStream(imageName)));
        tree5.setFitHeight(50);

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

        Image carImage = new Image(new FileInputStream("static/img/sideViewCar.png"));

        Vehicle vehicle = new Vehicle(Zone.DefaultZone.PARKING_SOUTH.getRandomPoint(), carImage);
        controller.register(vehicle);

        // Trees are on the border pane
        // getTreePane method give borderPane with trees

        BorderPane longTreePane = getTreePane("static/img/longTree.png");
        StackPane.setAlignment(longTreePane,Pos.CENTER_LEFT);
        StackPane.setMargin(longTreePane, new Insets(300,50,650,550));

        BorderPane shortTreePane = getTreePane("static/img/shortTree.png");
        StackPane.setAlignment(shortTreePane,Pos.CENTER_RIGHT);
        StackPane.setMargin(shortTreePane, new Insets(300,550,650,50));

        ImageView pathImage = new ImageView(new Image(new FileInputStream("static/img/circularRoad.png")));

        pathImage.setFitWidth(5*RATIO);
        pathImage.setFitHeight(5*RATIO);
        StackPane.setAlignment(pathImage, Pos.CENTER);

        ImageView grassImage = new ImageView(new Image(new FileInputStream("static/img/Grass.jpg")));
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
        payStationImage.setFitWidth(150);
        payStationImage.setFitHeight(150);
        StackPane.setAlignment(payStationImage, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(payStationImage, new Insets(200,100,200,150));

        root.getChildren().addAll(
                grassImage,
                longTreePane,
                shortTreePane,
                pathImage,
                trexImage,
                fenceImage,
                payStationImage,
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
            zone.getShape().toFront();
        }

    }

}