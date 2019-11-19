package astation;

import guest.Guest;
import vehicle.Vehicle;
import interfaces.Resource;
import java.lang.UnsupportedOperationException;

import java.util.LinkedList;

public class AutomatedStation implements Resource {


    private LinkedList<Guest> guestList;
    private LinkedList<Vehicle> vehicleList;

    private boolean emergency;

    public boolean sendStatus(){
        throw new UnsupportedOperationException();
    }

    public void setEmergency(boolean emergency){
        this.emergency = emergency;
    }

    public AutomatedStation() {

        this.vehicleList = new LinkedList<Vehicle>();
        this.guestList = new LinkedList<Guest>();

    }

    public void removeGuest(Guest a){
        for(int i = 0; i < this.guestList.size(); i++){
            if(a == guestList.get(i)){
                guestList.remove(i);
            } else {
                System.out.println("Error 1");
            }
        }
    }

    public void addGuest(Guest a) {
        this.guestList.add(a);
    }

    public void addVehicle(Vehicle v) {
        this.vehicleList.add(v);
    }

    public LinkedList<Guest> getGuestlist () {
        return this.guestList;
    }

    public LinkedList<Vehicle> getVehicleList() {
        return vehicleList;
    }
}
