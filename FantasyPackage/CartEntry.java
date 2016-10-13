package FantasyPackage;

/*
 * CartEntry object as base class for Cart storage
 */
public class CartEntry {
	/*
	 *  String to indicate where item was purchased from
	 *  Can be used to identify where to send back to if -
	 *  cancelling a purchase
	 */
	protected String vendorName;
	// The fun thing to be bought
	protected FunThingInterface thing;
	// The count of fun things being bought
	protected int count = 0;
	
	public String getVendorName() {
		return vendorName;
	}
	
	public FunThingInterface getFunThing() {
		return thing;
	}

	public int getCount() {
		return count;
	}
	
	// Constructor for a single entry
	CartEntry(FunThingInterface _thing, String vendor) {
		thing = _thing;
		vendorName = vendor;
	}
	// Constructor for multiple things in a single entry 
	CartEntry(FunThingInterface t_thing, String vendor, int number) {
		this(t_thing, vendor);
		count = number;
	}
}
