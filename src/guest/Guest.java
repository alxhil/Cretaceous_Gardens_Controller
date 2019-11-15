package guest;


import java.awt.*;
import java.util.UUID;

public class Guest {

    private String uuid;
    private String name;
    private Point location;
    Guest(String name, Point p){
        this.name = name;
        this.location = p;
        this.uuid = new UUID(123456789,123456789).toString();
    }

    public String getUUID() {
        return this.uuid;
    }

    public void move(int x, int y) {
        this.location.setLocation(x,y);
    }



}
