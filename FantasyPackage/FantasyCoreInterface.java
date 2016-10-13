package FantasyPackage;
import java.util.Iterator;
import java.util.Observer;

public interface FantasyCoreInterface extends DebuggerInterface {
		
		// Expose zone count
		int getZoneCount();
		
		// Provide zone iterator
	    Iterator<FunZoneInterface> getZoneIterator();
	    
	    // Provide zone access by name
	    // Zones must be unique identifiers which places a slight -
	    // restriction on implementation
	    FunZoneInterface getZone(String name);

	    // Provide zone access by index
	    FunZoneInterface getZone(int index);

	    // Zone registration
	    boolean registerZone(FunZoneInterface zone);
	    
	    // Zone unregistration
	    boolean unregisterZone(String name);

	    // exposes creation of FunThings
	    public FunThingInterface createFunThing(String type, String name, String description, double cost);
	    
	    // Expose user count
	    int getUserCount();
	    
	    // Provide user iterator	
	    Iterator<UserInterface> getUserIterator();
	    
	    // provide user access by name
	    UserInterface getUser(String name);
	    
	    // Provide user access by index
	    UserInterface getUser(int index);
	        
	    // User registration
	    boolean registerUser(String name);
	    
	    boolean unregisterUser(String name);

	    // Encapsulates act of user purchase
	    boolean buy(UserInterface user, FunZoneInterface zone, String product_name);
	    
	    // Encapsulates the act of a user returning a product
	    boolean unbuy(UserInterface user, String product_name);
	    
	    // register an observer of a zone
	    boolean registerObserver(Observer user, FunZoneInterface zone);
	    
	    // unregister an observer from a zone
	    boolean unregisterObserver(Observer user, FunZoneInterface zone);
	    
	    // Method for initialisation of zones
	    void initZones();

	    // set debugging on or off
		void setDebugging (boolean onOff);
		
}
