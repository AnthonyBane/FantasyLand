package FantasyPackage;

import java.util.Iterator;

//import FantasyPackage.FunZone.ZoneStockIterator;

public interface FunZoneInterface extends DebuggerInterface {

	// Accessor methods for zone identifier
	String getName();
	void setName(String new_name);
		
	// Add fun things
	boolean addThing(FunThingInterface thing, int count);
	
	// A stock iterator
	Iterator<FunZoneStock> getIterator();
	
	// Remove and return a fun thing by name
	FunThingInterface removeItem(String _string);
	
	// Get a fun thing by name
	FunThingInterface getItem(String _string);
	
	// Get a fun thing by index
	FunThingInterface getItem(int index);

	// Export fun stock item count
	int getCount();
	
	// Export count of fun things of a certain type
	int getCount(String type);
	
	// Initialisaton method passing in the fun zone factory for creating fun things and a configuration string
	// Syntax is a newline delimited set of concatenated strings representing fun things using colon delimited fields
	// Fields are type:name:description:cost
	// Parsing is limited to parameter count and type
	boolean init(FunZoneFactory factory, String string);	

	// Debug control
	void setDebugging (boolean onOff);

}
