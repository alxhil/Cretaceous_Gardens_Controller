package guest;


import cgc.Zone;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import vehicle.Vehicle;

import java.awt.*;
import java.util.UUID;

public class Guest {

    private UUID uuid;
    private int number;
    private Boolean draw;
    private Boolean inVehicle;
    private Circle circle;

    public Guest(Point p){
        this.inVehicle = false;
        this.circle = new Circle(0, 0, 9);
        this.draw = true;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUUID() {
        return this.uuid;
    }



    public void move(double x, double y) {
        this.circle.setTranslateX(x);
        this.circle.setTranslateY(y);
    }

    public Point getLocation() {
        return new Point((int) this.circle.getTranslateX(), (int) this.circle.getTranslateY());
    }


    /*
    https://gamedev.stackexchange.com/a/79628 thank you
     */
    public Boolean getIntersection(Vehicle v) {
        return this.circle.getBoundsInParent().intersects(v.getShape().getBoundsInParent());
    }



    public Shape getShape (){
        return this.circle;

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
        this.circle.setVisible(false);
        this.circle.managedProperty().bind(this.circle.visibleProperty());
    }
    public void setVisible() {
        this.circle.setVisible(true);
        this.circle.managedProperty().bind(this.circle.visibleProperty());
    }
    // Noop for now, but eventually may want to behave differently
    public void setEmergency(boolean emergency){};

}
