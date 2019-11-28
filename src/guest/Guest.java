package guest;


import cgc.Zone;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import vehicle.Vehicle;

import java.awt.*;
import java.util.UUID;
import java.lang.Math;

public class Guest {

    private UUID uuid;
    private int tick;
    private int second;
    private Boolean inVehicle;
    private Circle circle;
    private Point movingPoint;
    private Boolean leaving;

    public Guest(Point point){
        this.movingPoint = new Point(0,0);
        this.inVehicle = false;
        this.tick = 0;
        this.second = 0;
        this.leaving = false;
        this.circle = new Circle(0, 0, 9);
        this.circle.setStroke(Color.WHITE);
        this.circle.setFill(Color.PURPLE);
        this.uuid = UUID.randomUUID();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setMovingPoint(Point point) {
        this.movingPoint = point;
    }

    public Point getMovingPoint() {
        return this.movingPoint;
    }

    public void markForExit() {
        this.leaving = true;
    }

    public Boolean hasExited() {
        return this.leaving && Math.abs((int)this.circle.getTranslateX() - (int)this.movingPoint.getX()) <= 2
               && Math.abs((int)this.circle.getTranslateY() - (int)this.movingPoint.getY()) <= 2;
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
    public Boolean getIntersection(Vehicle vehicle) {
        return this.getShape().getBoundsInParent().intersects(vehicle.getShape().getBoundsInParent());
    }

    public Boolean getIntersection(Zone zone) {
        return this.getShape().getBoundsInParent().intersects(zone.getShape().getBoundsInParent());
    }



    public Shape getShape (){
        return this.circle;

    }
    public Boolean isInVehicle(){
        return this.inVehicle;
    }
    public void setInVehicle(Boolean isInVehicle) {
        this.inVehicle = isInVehicle;
    }

    public void tick() {
        this.tick++;
        if(this.tick >= 60){
            this.second++;
            this.tick = 0;
        }
    }

    public int getSecond() {
        return this.second;
    }

    public void resetSecond() {
        this.second = 0;
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
    public void setEmergency(Boolean emergency){};

}
