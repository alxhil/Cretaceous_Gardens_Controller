package vehicle;

import guest.Guest;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
    private Text text;
    private Point location;
    private UUID identifier;
    private Rectangle r;
    private LinkedList<Guest> guestsInVehicle;


    private boolean emergency;

    /**
     *
     *
     */
    public Vehicle(Point p, Image carImage) throws FileNotFoundException {
        this.text = new Text(0, 0, "0");
        this.guestsInVehicle = new LinkedList<Guest>();
        this.r = new Rectangle(50,50);
        this.r.setFill(new ImagePattern(carImage));

        this.location = p;
        this.currentCapacity = 0;
        this.isMoving = false;
        this.isFull = false;
        this.identifier = UUID.randomUUID();
        this.emergency = false;
        this.move(p.getX(), p.getY());
    }

    public boolean sendStatus(){
        throw new UnsupportedOperationException();
    }

    public void setEmergency(boolean emergency){
        this.emergency = emergency;
    }

    void increaseCapacity() {
        if (this.currentCapacity < 10) {
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
    Boolean validate(Guest g, LinkedList<UUID> uuid){
        if(uuid.contains(g.getUUID())){
            return true;
        } else {
            return false;
        }

    }

    public boolean verifyEntryRFID(UUID userToken){
        // Mocked for the simulation
        return true;
    }

    public boolean registerSeatRFID(UUID userToken){
        // Mocked for the simulation
        return true;
    }



    void checkCapacity() {
        if(this.currentCapacity >= 10){
            this.isFull = true;
            this.isMoving = true;
        } else {
            this.isFull = false;
        }
    }

    public void checkMoving() { // Implementing a timer later
        if(this.isFull) {
            this.isMoving = true;
        } else {
            this.isMoving = false;
        }
    }

    public void move(double x, double y){
        this.location.setLocation(x,y);
        this.r.setTranslateX(x);
        this.r.setTranslateY(y);
        this.text.setTranslateX(x+3);
        this.text.setTranslateY(y + this.r.getWidth());
    }

    public Point getLocation() {
        return this.location.getLocation();
    }

    public Shape getShape() {
        return this.r;
    }

    public Boolean isMoving() {
        return this.isMoving;
    }

    public void setMoving() {
        this.isMoving = !this.isMoving;
    }

    public void addToVehicle(Guest g) {
        if(!g.isInVehicle()) {
            this.guestsInVehicle.add(g);
            increaseCapacity();
            this.text.setText("" + currentCapacity);
            g.setInVehicle(true);
        } else {
            System.out.println("Already in vehicle");
        }
    }

    public void removeFromVehicle(Guest g) {
        for(Guest carGuest: this.guestsInVehicle){
            if(g == carGuest) {
                this.guestsInVehicle.remove(g);
            } else {
                System.out.println("Error 2: failed to find in list");
            }
        }
    }

    public void rotateVehicle(double d) {
        double centerX = this.r.getX() + this.r.getWidth() / 2;
        double centerY = this.r.getY() + this.r.getWidth() / 2;
        Rotate rotate = new Rotate(d, centerX, centerY);
        this.r.getTransforms().add(rotate);

    }

    public boolean isOverCapcityDetected() {
        return this.currentCapacity > 10;
    }

    public Text getText(){
        return this.text;
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
