package vehicle;

import guest.Guest;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.lang.UnsupportedOperationException;

import java.awt.*;
import java.util.LinkedList;
import java.util.UUID;
import interfaces.Resource;

public class Vehicle implements Resource {
    private int currentCapacity;
    private boolean isMoving;
    private boolean isFull;
    private Text text;
    private Point location;
    private String number;
    private Rectangle r;
    private LinkedList<Guest> guestsInVehicle;

    private boolean emergency;

    /**
     *
     *
     */
    public Vehicle(Point p){
        this.text = new Text(0, 0, "0");
        this.guestsInVehicle = new LinkedList<Guest>();
        this.r = new Rectangle(20,10);
        this.location = p;
        this.currentCapacity = 0;
        this.isMoving = false;
        this.isFull = false;
        this.number = Math.random()*1000+"";
        this.emergency = false;
    }

    public boolean sendStatus(){
        throw new UnsupportedOperationException();
    }

    public void setEmergency(boolean emergency){
        this.emergency = emergency;
    }

    void increaseCapacity() {
        if (this.currentCapacity < 999) {
            this.currentCapacity++;
            checkCapacity();
        } else {
            System.out.println("Max Capacity Reached already Car number"+this.number);
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
        if(this.currentCapacity >= 999){
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
        this.text.setTranslateY(y+20);
    }

    public Point getLocation() {
        return this.location.getLocation();
    }

    public Rectangle getR() {
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

    public boolean isOverCapcityDetected() {
        return this.currentCapacity > 999;
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
