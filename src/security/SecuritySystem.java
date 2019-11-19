package security;

public class SecuritySystem {
    public boolean isEmergencyOn(Boolean isFenceCompromised)
    {
        boolean isEmergencyOn = false;
        if(isFenceCompromised == true)
        {
            isEmergencyOn = true;
        }
        return isEmergencyOn;
    }

}