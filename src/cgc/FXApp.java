package cgc;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import astation.AutomatedStation;
import security.VoltageMonitor;
import guest.Guest;
import vehicle.Vehicle;


import java.awt.*;
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
    private Lighting lighting = new Lighting();
    private ImageView fenceImage;



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
        generateGuestButton.setTranslateX(310);
        generateGuestButton.setTranslateY(10);

        //StackPane.setAlignment(generateGuestButton, Pos.BOTTOM_LEFT);
        //StackPane.setMargin(generateGuestButton, new Insets(400,600,150,150));

        this.voltageMonitorButton = new Button("Toggle Fence");
        this.voltageMonitorButton.setFont(new Font(15));
        this.voltageMonitorButton.setStyle("-fx-background-color: #fff000;");
        this.voltageMonitorButton.setTranslateX(310);
        this.voltageMonitorButton.setTranslateY(60);
        //StackPane.setAlignment(voltageMonitorButton, Pos.BOTTOM_LEFT);
        //StackPane.setMargin(voltageMonitorButton, new Insets(400,600,190,150));

        ImageView imageView = new ImageView(new Image( new FileInputStream("static/img/emergency.png")));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        this.emergencyButton = new Button();
        double r=1.5;
        this.emergencyButton.setShape(new Circle(r));
        this.emergencyButton.setMinSize(2*r, 2*r);
        this.emergencyButton.setMaxSize(2*r, 2*r);
        this.emergencyButton.setGraphic(imageView);
        this.emergencyButton.setTranslateX(310);
        this.emergencyButton.setTranslateY(150);
        //StackPane.setAlignment(emergencyButton,Pos.BOTTOM_LEFT);
        //StackPane.setMargin(emergencyButton, new Insets(400,600,160,50));

        this.lighting.setDiffuseConstant(2.0);
        this.lighting.setSpecularConstant(0.0);
        this.lighting.setSpecularExponent(0);
        this.lighting.setSurfaceScale(0);

        this.generateGuestButton.setOnAction(e->{
            Point point = Zone.DefaultZone.SOUTH_END.getRandomPoint();
            Guest guest = new Guest(point);
            guest.setMovingPoint(point);
            guest.move(point.getX(), point.getY());
            System.out.println("Placed at (" + point.getX() + " ," + point.getY() + ")");
            this.controller.registerGuest(guest);
            Shape visitorShape = guest.getShape();
            root.getChildren().add(visitorShape);
            visitorShape.setTranslateX(point.getX());
            visitorShape.setTranslateY(point.getY());
        });

        this.voltageMonitorButton.setOnAction(e -> {
            VoltageMonitor voltageMonitor = this.controller.getSecuritySystem().getVoltageMonitor();
            if (voltageMonitor.getVoltage() == 0.0f) {
                voltageMonitor.enable();
            } else {
                voltageMonitor.disable();
            }
        });

       this.emergencyButton.setOnAction(e->{
           this.controller.handleEvent(new AppUpdate(!this.controller.isEmergency()));
       });

        startUp();
        zoneStart();
        this.controller.getSecuritySystem().setAudio("static/audio/siren.mp3");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.0133), event -> {
            gameLoop();
        }));
        Rectangle buttonBox = new Rectangle(150,300);
        buttonBox.setTranslateX(310);
        buttonBox.setTranslateY(100);
        buttonBox.setFill(Color.WHITE);
        buttonBox.setStroke(Color.GREY);
        buttonBox.setStrokeWidth(5);
        buttonBox.toBack();
        Rectangle water = new Rectangle(950,100,Color.DARKBLUE);
        water.setStroke(Color.TAN);
        water.setTranslateY(450);
        water.setStrokeWidth(15);
        water.setBlendMode(BlendMode.SRC_ATOP);
        root.getChildren().add(water);
        ImageView ferryImage = new ImageView(new Image(new FileInputStream("static/img/ferry.png")));
        ferryImage.setTranslateX(0);
        ferryImage.setTranslateY(370);
        ferryImage.setFitHeight(200);
        ferryImage.setFitWidth(200);
        root.getChildren().add(ferryImage);

        Text controlCenterText = new Text(0, 0, "Control Center");
        controlCenterText.setTranslateX(305);
        controlCenterText.setTranslateY(-30);

        controlCenterText.setFill(Color.BLACK);
        controlCenterText.setFont(new Font("Verdana", 15));

        controlCenterText.toFront();





        root.getChildren().add(buttonBox);
        root.getChildren().add(controlCenterText);

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    public void gameLoop() {
        /**
         * Tick system **Keep this first in loop**
         *
         */


        generateGuestButton.toFront();
        emergencyButton.toFront();
        voltageMonitorButton.toFront();
        for(Vehicle vehicle : this.controller.getVehicles()) {
            vehicle.tick();
        }

        for(Guest guest : this.controller.getGuests()) {
            guest.tick();
        }

        boolean isEmergency = this.controller.isEmergency();

        for(Zone zone : this.controller.getZoneList()) {
            for (Vehicle vehicle : this.controller.getVehicles()) {
                String zoneName = zone.getName();
                if (zoneName.equals(Zone.DefaultZone.PARKING_NORTH.getName())){
                    if (vehicle.isMoving() && vehicle.getIntersection(zone) &&
                            vehicle.getDestination().equals(zoneName)) {
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
                    if (vehicle.getIntersection(zone) && !vehicle.isMoving()) {
                        for (Guest guest : this.controller.getGuests()) {
                            if (isEmergency || guest.isInVehicle() || guest.isLeaving()) {
                                continue;
                            }
                            if(guest.guestRegisterStatus()) {
                                guest.setMovingPoint(vehicle.getLocation());
                            } else {
                                // Move to the automation station point
                                guest.setMovingPoint(new Point(300, 300));
                            }
                        }
                    }
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
                        if (!isEmergency){
                            vehicle.setDestination(Zone.DefaultZone.PARKING_NORTH.getName());
                        }
                    }
                } else if (zoneName.equals(Zone.DefaultZone.EXHIBIT.getName())){
                    for (Guest guest : this.controller.getGuests()) {
                        if (!guest.getIntersection(zone) || (!isEmergency && vehicle.getSecond() < 10)){
                            continue;
                        }
                        guest.setMovingPoint(vehicle.getLocation());
                    };
                }
            }
        }




        // In case the security system goes down,
        // in a real implementation this would be done in the security
        // systems event loop
        this.controller.getSecuritySystem().sendStatus();
        for (AutomatedStation station : this.controller.getStations()) {
            station.sendStatus();
        }
        for (Vehicle vehicle : this.controller.getVehicles()) {
            // Currently a noop
            vehicle.sendStatus();
            if(vehicle.isMoving()) {
                // Should either be 1 or -1 to flip the sign of h
                int flip = 1;
                if (isEmergency){
                    double degrees = this.getDegreesFromCenter(vehicle.getLocation());
                    if ((degrees > 90.0 && degrees < 180.0) || (degrees < -90.0 && degrees > -180.0)){
                        flip = -1;
                    }
                }
                this.h += .01 * flip;
                vehicle.move(-Math.cos(h) * 2 * RATIO, -Math.sin(h) * 2 * RATIO);
                // Hacky hand-tuned parameter for rotating
                vehicle.rotateVehicle(0.5655 * flip);
            }


            /**
             * PathFinding Uses guest
             */
            for(Guest guest : this.controller.getGuests()) {
                if (guest.isInVehicle()){
                    continue;
                }
                if (guest.hasExited()) {
                    guest.setInvisible();
                    continue;
                }
                pathFinding(guest);
                if (!guest.isLeaving() && guest.getIntersection(vehicle) && vehicle.getCapacity() < Vehicle.MAX_CAPACITY) {
                    vehicle.resetSecond();
                    vehicle.addToVehicle(guest);
                    guest.setInvisible();
                } else if (vehicle.getCapacity() >= Vehicle.MAX_CAPACITY &&
                           !Zone.DefaultZone.SOUTH_END.isWithin(guest.getMovingPoint())){
                    Point newDest = Zone.DefaultZone.SOUTH_END.getRandomPoint();
                    guest.setMovingPoint(newDest);
                }
                if(!guest.guestRegisterStatus() && guest.getIntersection(controller.getAstation())){
                    guest.registerGuest();
                }
            }
        }
        if (controller.getSecuritySystem().getVoltageMonitor().getVoltage() == 0.0f) {
            this.lighting.setLight(new Light.Distant(45, 45, Color.RED));
        } else {
            this.lighting.setLight(new Light.Distant(45, 45, Color.GREEN));
        }
        this.fenceImage.setEffect(lighting);

    }

    private double getDegreesFromCenter(Point location){
        return Math.atan2(location.getY(), location.getX()) * 180 / Math.PI;
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
        Image payStation = new Image(new FileInputStream("static/img/payStation.png"));
        ImageView payStationImage = new ImageView(new Image(new FileInputStream("static/img/payStation.png")));
        Vehicle vehicle = new Vehicle(Zone.DefaultZone.PARKING_SOUTH.getRandomPoint(), carImage);
        vehicle.move(-Math.cos(h) * 2 * RATIO, -Math.sin(h) * 2 * RATIO);
        this.controller.setAutomatedStation(new AutomatedStation(controller, payStation));
        vehicle.setDestination(Zone.DefaultZone.PARKING_NORTH.getName());
        this.controller.register(vehicle);
        root.getChildren().add(controller.getAstation().getShape());

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

        fenceImage = new ImageView(new Image(new FileInputStream("static/img/fence.png")));
        fenceImage.setFitHeight(200);
        fenceImage.setFitWidth(300);
        fenceImage.setRotate(180.0);
        StackPane.setAlignment(fenceImage,Pos.TOP_CENTER);
        StackPane.setMargin(fenceImage, new Insets(50));



        payStationImage.setFitWidth(50);
        payStationImage.setFitHeight(50);
        payStationImage.setTranslateX(300);
        payStationImage.setTranslateY(300);
        //StackPane.setAlignment(payStationImage, Pos.BOTTOM_CENTER);
        //StackPane.setMargin(payStationImage, new Insets(0,0,100,0));

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
                //waterImage,
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
            this.controller.registerZone(zone);
            root.getChildren().add(zone.getShape());
        }

    }

}