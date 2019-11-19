package vehicle;

import guest.Guest;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.lang.UnsupportedOperationException;

import java.awt.*;
import java.util.LinkedList;
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
        if (this.currentCapacity < 10) {
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
        if(!g.getInVehicle()) {
            this.guestsInVehicle.add(g);
            increaseCapacity();
            this.text.setText("" + currentCapacity);
            g.setInVehicle(true);
            System.out.println("added to vehicle");
        } else {
            System.out.println("Already in vehicle");
        }
    }

    public void removeFromVehicle(Guest g) {
        for(int i = 0; i < this.guestsInVehicle.size(); i++){
            if(g == this.guestsInVehicle.get(i)) {
                this.guestsInVehicle.remove(g);
            } else {
                System.out.println("Error 2: failed to find in list");
            }
        }
    }

    public Text getText(){
        return this.text;
    }


}
