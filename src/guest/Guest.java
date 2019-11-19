package guest;


import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.UUID;

public class Guest {

    private String uuid;
    private int number;
    private Point location;
    private Boolean draw;
    private Circle c;

    public Guest(int number, Point p){
        this.number = number;
        this.location = p;
        this.c = new Circle(9);
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

    public Boolean getIntersection(Rectangle r) {
        if(this.c.intersects(r.getLayoutBounds())){
            return true;
        } else {
            return false;
        }
    }

    public Circle getC (){
        return this.c;
    }



}
