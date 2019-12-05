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
    private final int height;
    private final int width;

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
            int x = (int)this.location.getX() - this.width / 2;
            int y = (int)this.location.getY() - this.height / 2;
            Random rand = new Random();
            int randomX = rand.nextInt(this.width);
            int randomY = rand.nextInt(this.height);
            return new Point(x + randomX, y + randomY);
        }

        public String getName() {
            return this.name;
        }

        /*
        * Indicates that a point is within a zone
        */
        public boolean isWithin(Point point) {
            int originX = (int) this.location.getX();
            int originY = (int) this.location.getY();
            int endX = (int) this.width + originX;
            int endY = (int) this.height + originY;
            return (originX <= point.getX() && endX >= point.getX()
                    && originY <= point.getY() && endY >= point.getY());
        }

    }

    public Zone(Point point, int width, int height, String name) {
        this.visitorZone = false;
        this.name = name;
        this.parkingZone = false;
        this.height = height;
        this.width = width;
        this.rectangle = new Rectangle(width, height);
        if(name.toLowerCase().startsWith("parking")){
            this.parkingZone = true;
            this.rectangle.setFill(Color.TRANSPARENT);
            //DEBUG this.rectangle.setStroke(Color.RED);
        } else if (name.equalsIgnoreCase("south_end")){
            this.visitorZone = true;
            this.rectangle.setFill(Color.TRANSPARENT);
            //DEBUG this.rectangle.setStroke(Color.BLACK);

        } else if (name.equalsIgnoreCase("exhibit")) {
            this.rectangle.setFill(Color.TRANSPARENT);
            //DEBUG this.rectangle.setStroke(Color.BLUE);
        }
        this.rectangle.toFront();
        this.rectangle.setX(point.getX());
        this.rectangle.setY(point.getY());
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

    /*
    * Indicates that a point is within a zone
    */
    public boolean isWithin(Point point) {
        int originX = (int) this.rectangle.getLayoutX();
        int originY = (int) this.rectangle.getLayoutY();
        int endX = (int) this.width + originX;
        int endY = (int) this.height + originY;
        return (originX <= point.getX() && endX >= point.getX()
                && originY <= point.getY() && endY >= point.getY());
    }

}
