package cgc;

import astation.AutomatedStation;
import guest.Guest;
import javafx.util.Duration;
import vehicle.Vehicle;
import security.SecuritySystem;
import interfaces.Resource;

import java.awt.*;
import java.util.LinkedList;

public class Cgc{

    private LinkedList<Guest> guestList;
    private LinkedList<Vehicle> vehicleList;
    private LinkedList<AutomatedStation> stationList;
    private SecuritySystem securitySystem;
    private int guestCount = 0;

    public Cgc(){
        this.guestList = new LinkedList<Guest>();
        this.vehicleList = new LinkedList<Vehicle>();
        this.stationList = new LinkedList<AutomatedStation>();
    }

    public void registerGuest(Guest guest){
        this.guestList.add(guest);
    }

    public <T> boolean register(Object resource){
        String className = resource.getClass().getSimpleName();
        if (className.equals("Guest")){
            this.registerGuest((Guest)resource);
        } else if (resource instanceof Resource) {
            registerResource((Resource)resource);
        } else {
            System.out.printf("Unknown type attempted to register '%s'\n", className);
            return false;
        }
        return true;
    }

    // Noop for now
    public <T> void handleEvent(Object update){}

    // Noop for now
    public void registerAlert(Resource resource) {}

    public <T> void registerResource(Resource resource){
        String className = resource.getClass().getSimpleName();
        if (className.equals("Vehicle")) {
            this.vehicleList.add((Vehicle)resource);
            return;
        } else if (className.equals("AutomatedStation")) {
            this.stationList.add((AutomatedStation)resource);
        } else {
            System.out.printf("Unknown type registered '%s'\n", className);
        }
    }

    public LinkedList<Vehicle> getVehicles(){
        return this.vehicleList;
    }

    public LinkedList<Guest> getGuests(){
        return this.guestList;
    }

}