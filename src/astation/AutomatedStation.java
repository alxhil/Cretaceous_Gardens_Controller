package astation;

import guest.Guest;
import interfaces.Resource;
import cgc.Cgc;

import java.util.UUID;
import java.lang.UnsupportedOperationException;

public class AutomatedStation implements Resource {

    private Cgc controller;

    public AutomatedStation(Cgc controller) {
        this.controller = controller;
        this.emergency = false;
    }

    private boolean emergency;

    public boolean sendStatus(){
        throw new UnsupportedOperationException();
    }

    private void registerVisitor(Guest guest){
        this.controller.registerGuest(guest);
    }
    public void setEmergency(boolean emergency){
        this.emergency = emergency;
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


}
