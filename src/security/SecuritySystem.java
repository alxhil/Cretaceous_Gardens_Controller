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
    private LinkedList<Fence> fenceList;

    public SecuritySystem(Cgc controller) {
        this.emergency = false;
        this.monitor = new VoltageMonitor();
        this.controller = controller;
        this.fenceList = new LinkedList<>();
    }

    public void pushFence(Fence fence) {
        this.fenceList.push(fence);
    }

    public LinkedList<Fence> getFences() {
        return this.fenceList;
    }

    public boolean sendStatus() {
        throw new UnsupportedOperationException();
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
        this.triggerTranquilizer();
    }

    public void playAudio(String filePath) {
        // Open file and play it
    }

    // Its a noop in the simulation
    private void triggerTranquilizer() {
    }
}
