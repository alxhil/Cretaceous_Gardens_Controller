package security;


public class VoltageMonitor{

    private float voltage;

    public VoltageMonitor(){
        this.voltage = 2.5f;
    }

    public float getVoltage(){
        return voltage;
    }

    public void disable(){
        this.voltage = 0.0f;
    }

    public void enable(){
        this.voltage = 2.5f;
    }
    //testing

}
