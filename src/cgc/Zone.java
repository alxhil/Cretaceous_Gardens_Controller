package cgc;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.*;


public class Zone {

    private Rectangle rectangle;
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

        public Point getRandomPoint() {
            double x = this.location.getX();
            double y = this.location.getY();
            return new Point((int)(x+ (Math.random() * this.width)), (int) y);
        }

    }

    public Zone(Point point, int width, int height, String name) {
        this.visitorZone = false;
        this.parkingZone = false;
        this.rectangle = new Rectangle(width, height);
        if(name.toLowerCase().startsWith("parking")){
            this.parkingZone = true;
            this.rectangle.setFill(Color.RED);
        } else if (name.equalsIgnoreCase("south_end")){
            this.visitorZone = true;
            this.rectangle.setFill(Color.GREEN);
        } else if (name.equalsIgnoreCase("exhibit")) {
            this.rectangle.setFill(Color.BLUE);
        }
        this.rectangle.toBack();
        this.rectangle.setTranslateX(point.getX());
        this.rectangle.setTranslateY(point.getY());

    }



    /**
     * Returns a point that is randomly assigned in the zone. It will only randomize the X coordinate and will not
     *Influence the Y Coordinate. This will be used for parking purposes.
     */
    public Point getRandomPoint() {
        double xZ = this.rectangle.getX();
        double yZ = this.rectangle.getY();
        double wZ = this.rectangle.getWidth();
        return new Point((int)(xZ+ (Math.random()*wZ)), (int) yZ);

    }

    public Shape getShape(){
        return this.rectangle;
    }

}
