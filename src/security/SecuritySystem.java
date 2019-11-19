package security;

import cgc.Cgc;
import interfaces.Resource;
import security.VoltageMonitor;
import java.lang.UnsupportedOperationException;

public class SecuritySystem implements Resource{

    private Cgc controller;
    private boolean emergency;
    private VoltageMonitor monitor;

    public SecuritySystem(Cgc controller){
        this.emergency = false;
        this.monitor = new VoltageMonitor();
        this.controller = controller;
    }

    public boolean isEmergencyOn(Boolean isFenceCompromised)
    {
        boolean isEmergencyOn = false;
        if(isFenceCompromised == true)
        {
            isEmergencyOn = true;
        }
        return isEmergencyOn;
    }

    public boolean sendStatus(){
        throw new UnsupportedOperationException();
    }

    public void setEmergency(boolean emergency){
        this.emergency = emergency;
        if (this.emergency) {
            this.triggerTranquilizer();
        }
    }

    public void playAudio(String filePath){
        // Open file and play it
    }

    // Its a noop in the simulation
    private void triggerTranquilizer(){}
}
