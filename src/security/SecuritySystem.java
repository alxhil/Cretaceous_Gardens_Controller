package security;

import cgc.Cgc;
import interfaces.Resource;
import security.VoltageMonitor;

import java.lang.UnsupportedOperationException;
import java.util.LinkedList;

public class SecuritySystem implements Resource {

    private Cgc controller;
    private boolean emergency;
    private VoltageMonitor monitor;

    public class SecurityUpdate {
        public final float voltage;
        public final boolean aliceVisible;

        public SecurityUpdate(float voltage, boolean aliceVisible){
            this.voltage = voltage;
            this.aliceVisible = aliceVisible;
        }
    }

    public SecuritySystem(Cgc controller) {
        this.emergency = false;
        this.monitor = new VoltageMonitor();
        this.controller = controller;
    }

    public boolean sendStatus() {
        this.controller.handleEvent(new SecurityUpdate(this.monitor.getVoltage(), true));
        return true;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
        this.triggerTranquilizer();
    }

    public void playAudio(String filePath) {
        // Open file and play it
    }

    // Its a noop in the simulation
    private void triggerTranquilizer() {}
}
