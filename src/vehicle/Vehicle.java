package vehicle;

import guest.Guest;

import java.awt.*;
import java.util.LinkedList;

public class Vehicle {
    private int currentCapacity;
    private boolean isMoving;
    private boolean isFull;
    private Point location;
    private String number;

    Vehicle(Point p){
        this.location = p;
        this.currentCapacity = 0;
        this.isMoving = false;
        this.isFull = false;
        this.number = Math.random()*1000+"";
    }

    void increaseCapacity() {
        if (this.currentCapacity < 10) {
            this.currentCapacity++;
        } else {
            System.out.println("Max Capacity Reached already Car number"+this.number);
        }
    }

    Boolean validate(Guest g, LinkedList<String> uuid){
        if(uuid.contains(g.getUUID())){
            return true;
        } else {
            return false;
        }

    }

    void move(int x, int y){
        this.location.setLocation(x,y);
    }


}
