package tdd;

import cgc.Cgc;
import org.junit.Assert;
import org.junit.Test;
import security.InfraredCamera;
import security.SecuritySystem;
import security.VoltageMonitor;

public class TestCase {
    Cgc cgc = new Cgc();
    SecuritySystem securitySystem = new SecuritySystem();
    VoltageMonitor voltageMonitor = new VoltageMonitor();
    InfraredCamera infraredCamera = new InfraredCamera();

    @Test
    public void test_EmergencySystems()
    {
        Assert.assertEquals(true, securitySystem.isEmergency(infraredCamera.isAliceVisible(true), voltageMonitor.voltageOff()));
    }

}
