package cgc;
/* Means of communicating from the application (GUI) to the controller
* changes should be appropriately made to Cgc.handleEvent to handle these
* events
*/
public class AppUpdate {

    public final boolean emergency;

    public AppUpdate(boolean emergency){
        this.emergency = emergency;
    }

}