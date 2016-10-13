package FantasyPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

/*
 * FantasyCore is a class with a singleton instance
 * It provides access to registered zones and users and interaction logic
 * Default zones are fetched from abstract factories with default contents
 * Users are added by an external application
 * Zones and users are accessed via iterators, by name and their index
 * Removing access by index decreases internal visibility and can be implemented
 * Zones and Users storage implementation is hidden
 * Items can be purchased from and returned to zones (buy and unbuy) 
 * Users can be set and removed from being observers of zones
 * Debugging can be suppressed
 */
public class FantasyCore extends Debugger implements FantasyCoreInterface {
	// Singleton private instance retrieved via a public static method
	static private FantasyCore fantasy_singleton = null;

	// Hidden implementation of User storage
	private List<UserInterface> users = new ArrayList<UserInterface>();

	// Hidden implementation of FunZone storage
	private List<FunZoneInterface> zones = new ArrayList<FunZoneInterface>();
	
	// Expose zone count
	public int getZoneCount() {
    	return zones.size();
    }
	
	// Provide zone iterator
    public Iterator<FunZoneInterface> getZoneIterator() {
    	return zones.iterator();
    }
    
    // Provide zone access by name
    // Zones must be unique identifiers which places a slight -
    // restriction on implementation
    public FunZoneInterface getZone(String name) {
    	for (FunZoneInterface zone:zones) {
    		if (zone == null) {
    			error("null zone in zones");
    		}
    		else if (zone.getName().matches(name)){
    			return zone;
    		}
    	}
    	return null;
    }

    // Provide zone access by index
    public FunZoneInterface getZone(int index) {
    	if ((index >= 0) && (index < getZoneCount())){
    		return zones.get(index);
    	}
    	return null;
    }

    // Zone registration
    public boolean registerZone(FunZoneInterface zone) {
    	if (zone == null) {
    		error("Error: Attempted to register a null zone");
    		return false;
    	}  	
   		// Checks for duplicates
   		if (getZone(zone.getName()) == null){
   			zone.setDebugging(isDebugging());
   			zones.add(zone);
   			debug("Zone " + zone.getName() + " registered");
   			return true;
   		}	
    	debug("Zone registration of " + zone.getName() + "failed");
    	return false;
    }

    // Zone unregistration
    public boolean unregisterZone(String name) {
        FunZoneInterface zone = getZone(name);
        if (zone != null) {
        	// Remove all observers
			// Unregister user as observer of current zones
			for (UserInterface user:users) {
				unregisterObserver((Observer)user, zone);
			}
        	// Removes the zone itself
			debug("Zone " + zone.getName() + " is unregistered");
        	zones.remove(zone);
        	return true;
        }
		debug("Zone " + name + " is unknown");
    	return false;
    }

    // Factory Method Pattern - encapsulates creation of FunThings
    public FunThingInterface createFunThing(String type, String name, String description, double cost) {
    	FunThingFactory factory = FunThingFactoryProducer.getFactory(type);
    	if (factory != null) {
    		return factory.getThing(name, description, cost);
    	}
		return null;
    }

    // Expose user count
    public int getUserCount() {
    	return users.size();
    }
    
    // Provide user iterator	
    public Iterator<UserInterface> getUserIterator() {
    	return users.iterator();
    }
    
    // provide user access by name
    public UserInterface getUser(String name) {
    	for (UserInterface user:users) {							// Null name not required to be tested as catered for within the test
    		if (user.getName().matches(name)){
    			return user;
    		}
    	}
    	return null;
    }

    // Provide user access by index
    public UserInterface getUser(int index) {
    	if ((index >= 0) && (index < getUserCount())){
    		return users.get(index);
    	}
    	return null;
    }
        
    // User registration
    public boolean registerUser(String name) {
    	// Guard against empty and duplicate registration
		if ((name != null) && (!name.isEmpty()) && (getUser(name) == null)) {
			User user = new User(name);
			user.setDebug(isDebugging());
    		users.add(user);
    		debug("User " + name + " registered");
    		return true;
    	}
		debug("User registration of " + name + "failed");
    	return false;
    }
    
    public boolean unregisterUser(String name) {
		if ((name != null) && !name.isEmpty()) {
			UserInterface user = getUser(name);
			if (user != null) {
				// Empty cart of any entries
				CartInterface cart = user.getCart();
				if (cart != null) {
					// Progressively unbuy cart entries
					while (cart.getCartEntryCount() > 0) {
						// (re)get the first cart entry
						CartEntry entry = cart.getCartEntry(0);
						// unbuy it and tries to return it to a zone
						unbuy(user, entry.thing.getName());
					}
				}
				// Unregister user as observer of current zones
				for (FunZoneInterface zone:zones) {
					unregisterObserver((Observer)user, zone);
				}
				// Remove user from list of users
				users.remove(user);
	    		debug("User " + name + " unregistered");
				// user now drops out of scope and becomes garbage collected whenever Java feels like it
				return true;
			}
    	}
		return false;
    }
    
    // Encapsulates act of user purchase
    public boolean buy(UserInterface user, FunZoneInterface zone, String product_name) {
    	if (user != null && zone != null) {
    		// The user now places in cart
    		CartInterface cart = user.getCart();
    		if (cart != null) {
        		// Remove the item from the vendor to decreases stock
        		FunThingInterface thing = zone.removeItem(product_name);
        		// If it was in stock
        		if (thing != null) {
        			// Add it to the cart
        			cart.addItem(thing, zone.getName());
        			debug("Bought " + product_name);
        			return true;
        		}
    		}
    	}
    	return false;
    }
    
    // Encapsulates the act of a user returning a product
    public boolean unbuy(UserInterface user, String product_name) {
    	if (user != null) {
    		// Get the user's cart
    		CartInterface cart = user.getCart();
    		// If there is a cart
    		if (cart != null) {
    			// Get the cart entry for this product
    			CartEntry entry = cart.getCartEntry(product_name);
    			// If the cart contains one
    			if (entry != null) {
    				// Find where it came from
    				FunZoneInterface zone = getZone(entry.vendorName);
    				// If the zone exists
    				if (zone != null) {
    					// Add it back to stock
    					zone.addThing(entry.thing, 1);
            			debug("Returned " + product_name + " to " + zone.getName());
    				}
					// Lastly, remove it from the cart
					cart.removeItem(product_name);
        			debug("Removed " + product_name + " from Cart");
					return true;
    			}
    		}
    	}
    	return false;
    }
    
    public boolean registerObserver(Observer user, FunZoneInterface zone) {
    	if ((user != null) && (zone != null)) {
    		((FunZone)zone).addObserver(user);
    		return true;
    	}
    	return false;
    }
    
    public boolean unregisterObserver(Observer user, FunZoneInterface zone) {
    	if ((user != null) && (zone != null)) {
    		((FunZone)zone).deleteObserver(user);
    		return true;
    	}
    	return false;
    }
    
    // Method for initialisation of zones
    public void initZones() {		
    	
    	// Get singleton abstract factory
		ZoneProducer zone_producer = ZoneProducer.getZoneProducer();
		
		// Create zones from zone factories returned from by the zone producer
    	// Zone producer supplies list of valid zone name types
    	String[] zone_names = zone_producer.getZoneNames();
    	for (int i = 0; i < zone_names.length; i++) {
    		FunZoneFactoryInterface factory = zone_producer.getFactory(zone_names[i], isDebugging());
    		// The private method registerZone assumes the zones are unique and non-null
    		if (factory != null) {
    			registerZone(factory.getZone());    	
    		} else {
    			debug("null zone returned from zone factory");
    		}
    	}
    }
    
	public void setDebugging (boolean onOff){
		setDebug(onOff);
		for (FunZoneInterface zone: zones){
			zone.setDebugging(onOff);
		}
		for (UserInterface user: users){
			user.setDebug(onOff);
		}
	}
    
    // Private constructor guarding against multiple instances
    private FantasyCore(boolean debugging) {
    	setName("FantasyCore");
    	setDebug(debugging);
		// Initialisation separated out so could be overridden
		initZones();
    }
    
    // Return single instance class object
	public static FantasyCore getFantasy(boolean debugging, String strategy) {
		if (fantasy_singleton == null) {
			fantasy_singleton = new FantasyCore(debugging);
			InitUsers initUsers = new InitUsers();
			InitUsersStrategyFactory factory = InitUsersStrategyFactory.getFactory();
			initUsers.setStrategy(factory.getStrategy(strategy));
			initUsers.init(fantasy_singleton);
		}
		// If fantasy_singleton is already created, debugging might need to be set -
		// differently to when it was originally constructed 
		fantasy_singleton.setDebugging(debugging);
		return fantasy_singleton;
	}

}
