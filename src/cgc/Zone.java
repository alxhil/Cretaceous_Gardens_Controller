package cgc;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.*;
import java.util.Random;


public class Zone {

    private Rectangle rectangle;
    private boolean visitorZone;
    private boolean parkingZone;
    private String name;

    public enum DefaultZone {
        PARKING_SOUTH (new Point(0, 160), 100, 40, "parking_south"),
        PARKING_NORTH (new Point(0, -160), 100, 40, "parking_north"),
        SOUTH_END     (new Point(0, 300), 200, 80, "south_end"),
        EXHIBIT       (new Point(0, -250), 200, 80, "exhibit");

        private final Point location;
        private final int width;
        private final int height;
        private final String name;


        DefaultZone(Point location, int width, int height, String name) {
            this.location = location;
            this.width = width;
            this.height = height;
            this.name = name;

        }

        public Point getLocation(){
            return this.location;
        }

        public int getHeight(){return this.height;}

        public int getWidth(){return this.width;}

        public Point getRandomPoint() {
            int ran = (new Random().nextInt(this.width) - (this.width/2));
            double x = this.location.getX();
            double y = this.location.getY();
            return new Point((ran), (int) y);
        }

        public String getName() {
            return this.name;
        }

    }

    public Zone(Point point, int width, int height, String name) {
        this.visitorZone = false;
        this.name = name;
        this.parkingZone = false;
        this.rectangle = new Rectangle(width, height);
        if(name.toLowerCase().startsWith("parking")){
            this.parkingZone = true;
            this.rectangle.setFill(Color.TRANSPARENT);
            this.rectangle.setStroke(Color.RED);
        } else if (name.equalsIgnoreCase("south_end")){
            this.visitorZone = true;
            this.rectangle.setFill(Color.TRANSPARENT);
            this.rectangle.setStroke(Color.BLACK);

        } else if (name.equalsIgnoreCase("exhibit")) {
            this.rectangle.setFill(Color.TRANSPARENT);
            this.rectangle.setStroke(Color.BLUE);
        }
        this.rectangle.toBack();
        this.rectangle.setTranslateX(point.getX());
        this.rectangle.setTranslateY(point.getY());

    }



    /**
     * Returns a point that is randomly assigned in the zone. It will only randomize the X coordinate and will not
     *Influence the Y Coordinate. This will be used for parking purposes.
     */



    public Shape getShape(){
        return this.rectangle;
    }

    public String getName() {
        return this.name;
    }

}
