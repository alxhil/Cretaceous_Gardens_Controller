package security;

import cgc.Cgc;
import interfaces.Resource;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import security.VoltageMonitor;

import java.io.File;
import java.lang.UnsupportedOperationException;
import java.util.LinkedList;

public class SecuritySystem implements Resource {

    private Cgc controller;
    private boolean emergency;
    private VoltageMonitor monitor;
    private Media media;
    private MediaPlayer mediaPlayer;

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

    public void setAudio(String filePath) {
        this.media = new Media(new File(filePath).toURI().toString());
        this.mediaPlayer = new MediaPlayer(this.media);
        this.mediaPlayer.setVolume(.25);
    }

    public void playAudio(Boolean b){
        if(b){
            this.mediaPlayer.setAutoPlay(true);
        } else {
            this.mediaPlayer.stop();
        }
    }

    // Its a noop in the simulation
    private void triggerTranquilizer() {}
}
