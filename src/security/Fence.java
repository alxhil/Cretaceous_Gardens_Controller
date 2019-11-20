package security;

public class Fence {

    private SecuritySystem securitySystem;
    private int fenceId;
    private int voltage;
    private int minimumVoltage;

    public Fence(SecuritySystem securitySystem, int fenceId) {
        this.securitySystem = securitySystem;
        this.fenceId = fenceId;
        this.voltage = 100;
        this.minimumVoltage = 75;
    }

    // Send Emergency on Fence failure
    public void sendFenceFailure() {
        securitySystem.setEmergency(true);
    }

    public boolean isVoltageLow() {
        return this.voltage < this.minimumVoltage;
    }

    public int getVoltage() {
        return voltage;
    }

    public int getFenceId() {
        return this.fenceId;
    }

}