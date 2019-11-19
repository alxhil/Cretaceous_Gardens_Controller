package security;

public class Fence {
    int voltage = 100;
    public Fence(int voltage)
    {
        this.voltage = voltage;
    }

    public boolean isVoltageLow()
    {
        boolean isVoltageLow = false;
        if(voltage < 75)
        {
            isVoltageLow = true;
        }
        return isVoltageLow;
    }

}

