package vehicle;

import cgc.Zone;
import guest.Guest;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.UnsupportedOperationException;

import java.awt.*;
import java.util.LinkedList;
import java.util.UUID;
import interfaces.Resource;
import javafx.scene.transform.Rotate;

public class Vehicle implements Resource {
    private int currentCapacity;
    private boolean isMoving;
    private boolean isFull;
    private int tick;
    private int second;
    private Text text;
    private Text timerText;
    private Point location;
    private UUID identifier;
    private Rectangle rectangle;
    private LinkedList<Guest> guestsInVehicle;
    private boolean waiting;
    private String destination;

    public static final int MAX_CAPACITY = 10;


    private boolean emergency;

    /**
     *
     *
     */
    public Vehicle(Point point, Image carImage) throws FileNotFoundException {

        this.initializeText();

        this.guestsInVehicle = new LinkedList<Guest>();
        this.rectangle = new Rectangle(50,50);
        this.rectangle.setFill(new ImagePattern(carImage));

        this.waiting = false;
        this.tick = 0;
        this.second = 0;
        this.location = point;
        this.currentCapacity = 0;
        this.isMoving = false;
        this.isFull = false;
        this.identifier = UUID.randomUUID();
        this.emergency = false;
        this.move(point.getX(), point.getY());
    }

    public boolean sendStatus(){return true;}

    public void setEmergency(boolean emergency){
        this.emergency = emergency;
        if (emergency) {
            this.setDestination(Zone.DefaultZone.PARKING_SOUTH.getName());
        }
    }

    /***
     * Init text on vehicle for debugging purposes (Might keep in final build)
     */
    public void initializeText(){
        this.text = new Text(0, 0, "0");
        this.text.setFont(Font.font("Alatsi", 20));
        this.text.setStroke(Color.WHITE);
        this.text.setFill(Color.BLACK);
        this.timerText = new Text(0, 0, "0");
        this.timerText.setFont(Font.font("Didact Gothic", 20));
        this.timerText.setStroke(Color.WHITE);
        this.timerText.setFill(Color.SKYBLUE);
    }


    /*
    * Indicates that the vehicle is waiting on a timer
    */
    public boolean isWaiting() {
        return this.waiting;
    }

    /*
    * Toggles the vehicle as to whether it is waiting or not.
    */
    public void toggleWaiting(){
        this.waiting = !this.waiting;
    }

    public void increaseCapacity() {
        if (this.currentCapacity < MAX_CAPACITY) {
            this.currentCapacity++;
            checkCapacity();
        } else {
            System.out.println("Max Capacity Reached already Car identifier: " + this.identifier);
        }
    }

    /**
     *
     *
     * @return  Returns true if LinkedList from parameter has guest from parameter, else return false.
     */
    boolean validate(Guest guest, LinkedList<UUID> uuid){
        return uuid.contains(guest.getUUID());
    }

    public boolean verifyEntryRFID(UUID userToken){
        // Mocked for the simulation
        return true;
    }

    public boolean registerSeatRFID(UUID userToken){
        // Mocked for the simulation
        return true;
    }



    public void checkCapacity() {
        if((this.currentCapacity == MAX_CAPACITY) || (this.second >= 15 && this.currentCapacity > 0)) {
            this.isFull = true;
            // TODO make this sensible
            this.isMoving = true;
        } else {
            this.isFull = false;
        }
    }



    public void move(double x, double y){
        if (this.emergency) {
            return;
        }
        this.location.setLocation(x,y);
        this.rectangle.setTranslateX(x);
        this.rectangle.setTranslateY(y);
        this.text.setTranslateX(x);
        this.text.setTranslateY(y);
        this.timerText.setTranslateX(x);
        this.timerText.setTranslateY(y + 50);
    }

    public Point getLocation() {
        return this.location.getLocation();
    }

    public Shape getShape() {
        return this.rectangle;
    }

    public boolean isMoving() {
        return this.isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public boolean getIntersection(Zone zone) {
        return this.getShape().getBoundsInParent().intersects(zone.getShape().getBoundsInParent());
    }

    public void addToVehicle(Guest guest) {
        if(!guest.isInVehicle()) {
            this.guestsInVehicle.add(guest);
            increaseCapacity();
            this.text.setText("" + currentCapacity);
            guest.setInVehicle(true);
        } else {
            System.out.println("Already in vehicle");
        }
    }

    public int getCapacity(){
        return this.currentCapacity;
    }

    public void removeFromVehicle(Guest guest) {
        for(int i = 0; i < guestsInVehicle.size(); i++ ){
            if(guestsInVehicle.get(i) == guest) {
                guestsInVehicle.remove(i);
                currentCapacity--;
                this.text.setText(""+currentCapacity);
            }
        }
    }

    public LinkedList<Guest> getGuestsInVehicle() {
        return this.guestsInVehicle;
    }

    public void rotateVehicle(double rotation) {
        if (this.emergency) {
            return;
        }
        double centerX = this.rectangle.getX() + this.rectangle.getWidth() / 2;
        double centerY = this.rectangle.getY() + this.rectangle.getWidth() / 2;
        Rotate rotate = new Rotate(rotation, centerX, centerY);
        this.rectangle.getTransforms().add(rotate);
    }

    public void tick() {
        this.tick++;
        if(this.tick >= 60){
            this.second++;
            this.timerText.setText(""+this.second);
            this.tick = 0;
        }
    }

    public void setDestination(String zoneName){
        this.destination = zoneName;
    }

    public String getDestination(){
        return this.destination;
    }

    public int getSecond() {
        return this.second;
    }

    public void resetSecond() {
        this.second = 0;
    }

    public Text getText(){
        return this.text;
    }

    public Text getTimerText() {
        return this.timerText;
    }

    // Is a noop in the simulation
    public boolean isObstructionDetected() {return false;}

    // Is a noop in the simulation
    public void openDoor() {}

    // Is a noop in the simulation
    public void closeDoor() {}

    // Is a noop in the simulation
    public void playAudio(String filePath){}


}
