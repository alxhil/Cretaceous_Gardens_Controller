package cgc;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.*;


public class Zone {

    private Rectangle r;
    private boolean visitorZone;
    private boolean parkingZone;

    public enum DefaultZone {
        PARKING_SOUTH (new Point(0, 160), 100, 40),
        PARKING_NORTH (new Point(0, -160), 100, 40),
        SOUTH_END     (new Point(0, 300), 200, 80),
        EXHIBIT       (new Point(0, -250), 200, 80);

        private final Point location;
        private final int width;
        private final int height;


        DefaultZone(Point location, int width, int height) {
            this.location = location;
            this.width = width;
            this.height = height;

        }

        public Point getLocation(){
            return this.location;
        }

        public int getHeight(){return this.height;}

        public int getWidth(){return this.width;}

    }

    public Zone(Point p, int width, int height, String name) {
        this.visitorZone = false;
        this.parkingZone = false;
        this.r = new Rectangle(width, height);
        if(name.toLowerCase().startsWith("parking")){
            this.parkingZone = true;
            this.r.setFill(Color.RED);
        } else if (name.equalsIgnoreCase("south_end")){
            this.visitorZone = true;
            this.r.setFill(Color.GREEN);
        } else if (name.equalsIgnoreCase("exhibit")) {
            this.r.setFill(Color.BLUE);
        }
        this.r.toBack();
        this.r.setTranslateX(p.getX());
        this.r.setTranslateY(p.getY());

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

    public Shape getShape(){
        return this.r;
    }

}
