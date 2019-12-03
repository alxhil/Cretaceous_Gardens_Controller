package astation;

import guest.Guest;
import interfaces.Resource;
import cgc.Cgc;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.UUID;
import java.lang.UnsupportedOperationException;

public class AutomatedStation implements Resource {

    private Cgc controller;
    private Rectangle r;

    public AutomatedStation(Cgc controller, Image paystationImage) {
        this.controller = controller;
        this.emergency = false;
        this.r = new Rectangle(0,0, 50,50);
        this.r.setFill(new ImagePattern(paystationImage));
        this.r.setTranslateX(300);
        this.r.setTranslateY(300);

    }

    private boolean emergency;

    public boolean sendStatus(){
        return true;
    }

    private void registerVisitor(Guest guest){
        this.controller.registerGuest(guest);
    }
    public void setEmergency(boolean emergency){
        this.emergency = emergency;
    }

    public void registerGuest(Guest guest) {
        guest.registerGuest();
    }

    // Its a noop in the simulation
    private void takePhoto() {}

    // Its a noop in the simulation
    private boolean validWaiver(Object waiver) {return true;}

    // Its a noop in the simulation
    private String startTransaction() {return "";}

    // Its a noop in the simulation
    private void cancelTransaction(String transactionID) {}

    // Its a noop in the simulation
    private void completeTransaction(String transactionID) {}

    // Its a noop in the simulation
    private void depositToken(UUID userID){}

    public Rectangle getShape() {
        return this.r;
    }


}
