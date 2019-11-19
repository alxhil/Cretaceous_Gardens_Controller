package vehicle;

import guest.Guest;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.LinkedList;

public class Vehicle {
    private int currentCapacity;
    private boolean isMoving;
    private boolean isFull;
    private Point location;
    private String number;
    private Rectangle r;

    /**
     *
     * @param Point p , This will be used by GUI to determine where to draw the guests.
     */
    public Vehicle(Point p){
        this.r = new Rectangle(20,10);
        this.location = p;
        this.currentCapacity = 0;
        this.isMoving = false;
        this.isFull = false;
        this.number = Math.random()*1000+"";
    }



    void increaseCapacity() {
        if (this.currentCapacity < 10) {
            this.currentCapacity++;
            checkCapacity();
        } else {
            System.out.println("Max Capacity Reached already Car number"+this.number);
        }
    }

    /**
     *
     * @param Guest g
     * @param LinkedList<String> uuid   Contains all UUIDs that are assigned to guests
     * @return  Returns true if LinkedList from parameter has guest from parameter, else return false.
     */
    Boolean validate(Guest g, LinkedList<String> uuid){
        if(uuid.contains(g.getUUID())){
            return true;
        } else {
            return false;
        }

    }

    void checkCapacity() {
        if(this.currentCapacity >= 10){
            this.isFull = true;
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
    }

    public Point getLocation() {
        return this.location.getLocation();
    }

    public Rectangle getR() {
        return this.r;
    }


}
