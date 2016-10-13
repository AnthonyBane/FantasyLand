package FantasyPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

// Class to encapsulate a fun zone
// It would be good to extend a Debugger but as we extend an Observable
// we must implement the DebugInterface and pass it through to an embedded debugger object 
// Is an observable class which notifies registered observers of changes
public class FunZone extends Observable implements FunZoneInterface {
	// Zone identifier
	protected String name;
	// Private debugger object
	private DebuggerInterface debugger = new Debugger();
	// Private implementation of fun thing storage
	private List<FunZoneStock> fun_stock = new ArrayList<FunZoneStock>();
	// Accessor method for zone identifier
	public String getName() {
		return name;
	}
	// part of debugger implementation but doesn't set the name itself
	@Override
	public void setName(String txt) {
		if (txt == null) {
			debugger.error("Attempt to setName to null");
		} else if (txt.isEmpty()) {
			debugger.error("Attempt to setName to empty string");
		} else {		
			this.name = txt;
			debugger.setType(getName());
		}
	}
	
	// Add fun things
	public boolean addThing(FunThingInterface thing, int count) {
		boolean added = false;
		// Check input arguments
		if ((thing == null) || (count < 0)) {
			return false;
		}
		// Iterator pattern to iterate over stock
		for (FunZoneStock stock:fun_stock) {
			// If a stock item already exists
			if (stock.getThing() == thing) {
				// increment its count
				stock.setCount(stock.getCount()+1);
				added = true;
				break;
			}
		}
		// If not already added
		if (added == false) {
			// Create a new stock item
			FunZoneStock stock = new FunZoneStock(thing, count);
			// And add it
			fun_stock.add(stock);
		}
		debugger.debug("adding " + thing.getName());
		// Notify observers
		this.setChanged();
		this.notifyObservers(thing);
		// Success
		return true;
	}
	
	// Export a stock iterator for external objects who may implement
	// an iterator pattern using it.
	public Iterator<FunZoneStock> getIterator() {
		return fun_stock.iterator();
	}
	
	// Remove and return a fun thing by name
	public FunThingInterface removeItem(String _string) {
		if (_string == null){
			return null;
		}
		// Iterator pattern to iterate over stock
		for (FunZoneStock stock:fun_stock) {
			// If we have the item in stock
			if (stock.getName() == _string) {
				// And there is any
				if (stock.getCount() > 0) {
					// Decrement the stock count
					stock.setCount(stock.getCount()-1);
					// If we have none left
					if (stock.getCount() == 0) {
						// Remove it
						fun_stock.remove(stock);
					}
					// Return the identified item
					return stock.getThing();
				}
			}
		}
		// Nothing removed
		return null;
	}
	
	// Get a fun thing by name
	public FunThingInterface getItem(String _string) {
		// Check input parameters
		if (_string == null){
			return null;
		}
		// Iterator pattern used to iterate over stock
		for (FunZoneStock stock:fun_stock) {
			// If found
			if (stock.getThing().getName() == _string) {
				// Return a reference to it 
				return stock.getThing();
			}
		}
		// Item not in stock
		return null;
	}
	
	// Get a fun thing by index
	public FunThingInterface getItem(int index) {
		// Check input parameters
		if ((index >= 0) && (index < fun_stock.size())) {
			// And return the thing by index
			return fun_stock.get(index).getThing();
		}
		// Out of bounds input parameters
		return null;
	}

	// Export fun stock item count
	public int getCount() {
		return this.fun_stock.size();
	}
	
	// Export distinct fun things of a certain type
	// Type not checked as comparison is still valid regardless
	public int getCount(String type)
	{
		int count = 0;
		for (FunZoneStock stock:fun_stock) {
			if (stock.getType() == type) {
				count++;
			}
		}
		return count;
	}
	
	// Initialisaton method passing in the fun zone factory for creating fun things and a configuration string
	// Syntax is a newline delimited set of concatenated strings representing fun things using colon delimited fields
	// Fields are type:name:description:cost
	// Parsing is limited to parameter count and type
	public boolean init(FunZoneFactory factory, String string) {
		// Check input parameters
		if ((factory == null) || (string == null)) {
			return false;
		}
		// Split string into a list of lines 
		String[] init_strings = string.split("\n");
		// Iterate (though not an iterator pattern?) over lines
		for (int i=0; i < init_strings.length; i++) {
			// Split comma delimited fields into string list
			String[] params = init_strings[i].split(":");
			// The first argument specifies a thing factory
			FunThingFactory fun_thing_factory = factory.getThingFactory(params[0]);
			// If one is found
			if (fun_thing_factory != null) {
				// If we have the expected number of parameters
				if (params.length == 5) {
					// Add a thing created by them
					addThing(fun_thing_factory.getThing(params[1], params[2], Double.parseDouble(params[3])), Integer.parseInt(params[4]));
				} else {
					// Otherwise complain
					debugger.debug("init failed - wrong parameter count " + params.length + " not 5");
				}
			} else {
				// Factory not found of this type
				debugger.debug("Unknown init type (" + params[0] + ")");
			}
		}
		return true;
	}
	
	// Debug control
	public void setDebugging (boolean onOff){
		debugger.setDebug(onOff);
	}
	
	public FunZone() {
		debugger.setName("FunZone");
	}

	// DebuggerInterface implementation uses embedded debugger methods
	public boolean isDebugging() {
		return debugger.isDebugging();
	}
	public void setType(String txt) {
		debugger.setType(txt);
	}
	public void setDebug(boolean enable) {
		debugger.setDebug(enable);
	}
	public void debug(String _string) {
		debugger.debug(_string);		
	}
	public void error(String text) {
		debugger.error(text);
	}
	
}
