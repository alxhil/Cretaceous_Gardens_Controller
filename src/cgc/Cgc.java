package cgc;

import astation.AutomatedStation;
import guest.Guest;
import javafx.util.Duration;
import vehicle.Vehicle;
import security.SecuritySystem;
import interfaces.Resource;
import cgc.AppUpdate;

import java.awt.*;
import java.util.LinkedList;

public class Cgc{

    private LinkedList<Guest> guestList;
    private LinkedList<Vehicle> vehicleList;
    private LinkedList<AutomatedStation> stationList;
    private SecuritySystem securitySystem;
    private int guestCount = 0;
    private boolean emergency = false;


    public Cgc(){
        this.guestList = new LinkedList<Guest>();
        this.vehicleList = new LinkedList<Vehicle>();
        this.stationList = new LinkedList<AutomatedStation>();
        this.securitySystem = new SecuritySystem(this);
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

    public void handleEvent(Object updateObj){
        String className = updateObj.getClass().getSimpleName();
        if (className.equals("SecurityUpdate")) {
            SecuritySystem.SecurityUpdate update = (SecuritySystem.SecurityUpdate)updateObj;
            if (update.voltage <= 0.0 && !this.emergency) {
                System.out.printf("Voltage is %f, enabling emergency mode!");
                this.setEmergency(true);
            }
        } else if(className.equals("AppUpdate")) {
            // Handle input from the GUI
            AppUpdate update = (AppUpdate)updateObj;
            this.setEmergency(update.emergency);
        }
    }

    // Noop for now
    public void registerAlert(Resource resource) {}

    public void registerResource(Resource resource){
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

    public LinkedList<AutomatedStation> getStations(){
        return this.stationList;
    }

    public SecuritySystem getSecuritySystem() {return this.securitySystem;}

    private void setEmergency(boolean emergency){
        if (this.emergency == emergency) {
            return;
        }
        System.out.printf("CGC transitioning to emergency: %b\n", emergency);
        this.emergency = emergency;
        for (Guest g : this.guestList) {
            g.setEmergency(emergency);
        }
        for (Vehicle v : this.vehicleList) {
            v.setEmergency(emergency);
        }
        for (AutomatedStation station : this.stationList){
            station.setEmergency(emergency);
        }
        this.securitySystem.setEmergency(emergency);
    }

}