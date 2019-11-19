package security;

import interfaces.Resource;

import java.lang.UnsupportedOperationException;

public class SecuritySystem implements Resource{

    private boolean emergency;

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
    }
}
