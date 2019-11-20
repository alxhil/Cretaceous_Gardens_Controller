package guest;


import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import vehicle.Vehicle;

import java.awt.*;
import java.util.UUID;

public class Guest {

    private UUID uuid;
    private int number;
    private Point location;
    private Boolean draw;
    private Boolean inVehicle;
    private Circle c;

    public Guest(Point p){
        this.inVehicle = false;
        this.location = p;
        this.c = new Circle(this.location.getX(), this.location.getY(), 9);
        this.draw = true;
        this.uuid = new UUID(123456789,123456789);
    }

    public UUID getUUID() {
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
        return this.c.getBoundsInParent().intersects(v.getR().getBoundsInParent());
    }

    public Circle getC (){
        return this.c;
    }

    public Boolean isInVehicle(){
        return this.inVehicle;
    }

    public void setInVehicle(Boolean b) {
        this.inVehicle = b;
    }

    /*
    https://stackoverflow.com/a/58800861
     */
    public void setInvisible() {
        this.c.setVisible(false);
        this.c.managedProperty().bind(c.visibleProperty());
    }

    public void setVisible() {
        this.c.setVisible(true);
        this.c.managedProperty().bind(c.visibleProperty());
    }

}
