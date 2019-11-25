package cgc;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;


public class Zone {

    private Rectangle r;
    private boolean visitorZone;
    private boolean parkingZone;
    private String s;

    public Zone(Point p, String s) {
        this.r = new Rectangle(p.getX(), p.getY(), 100, 40);
        this.r.toBack();
        this.visitorZone = false;
        this.parkingZone = false;
        this.s = s;
        if(s.equalsIgnoreCase("parking")){
            this.parkingZone = true;
            this.r.setFill(Color.RED);
        } else if (s.equalsIgnoreCase("visitor")){
            this.visitorZone = true;
            this.r.setFill(Color.GREEN);
        }

    }



    /**
     * Returns a point that is randomly assigned in the zone. It will only randomize the X coordinate and will not
     *Influence the Y Coordinate. This will be used for parking purposes.
     */
    public Point getRandomPoint() {
        double xZ = this.r.getX();
        double yZ = this.r.getY();

        double wZ = this.r.getWidth();

        return new Point((int)(xZ+ (Math.random()*wZ)), (int) yZ);

    }

    public Rectangle getR(){
        return this.r;
    }

    public String getMode() {
        return this.s;
    }

}
