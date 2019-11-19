package guest;


import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import vehicle.Vehicle;

import java.awt.*;
import java.util.UUID;

public class Guest {

    private String uuid;
    private int number;
    private Point location;
    private Boolean draw;
    private Boolean inVehicle;
    private Circle c;

    public Guest(int number, Point p){
        this.inVehicle = false;
        this.number = number;
        this.location = p;
        this.c = new Circle(this.location.getX(), this.location.getY(), 9);
        this.draw = true;
        this.uuid = new UUID(123456789,123456789).toString();
    }

    public String getUUID() {
        return this.uuid;
    }



    public void move(double x, double y) {
        this.location.setLocation(x,y);
        this.c.setTranslateX(x);
        this.c.setTranslateY(y);
    }

    public Point getLocation() {
        return this.location.getLocation();
    }


    /*
    https://gamedev.stackexchange.com/a/79628 thank you
     */
    public Boolean getIntersection(Vehicle v) {
        if(this.c.getBoundsInParent().intersects(v.getR().getBoundsInParent())){
            return true;
        } else {
            return false;
        }
    }

    public Circle getC (){
        return this.c;
    }

    public Boolean getInVehicle(){
        return this.inVehicle;
    }

    public void setInVehicle(Boolean b) {
        this.inVehicle = b;
    }




}
