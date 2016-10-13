package FantasyPackage;

// Base class for FunZone stock entries
public class FunZoneStock {
	// The thing that is stocked
	private FunThingInterface thing;
	// The number of things in stock
	private int count = 0;
	// Accessors
	public FunThingInterface getThing() {
		return thing;
	}
	public String getName() {
		return thing.getName();
	}
	public String getType() {
		return thing.getType();
	}
	public int getCount() {
		return count;
	}
	public void setCount(int c) {
		count = c;
	}
	// Single item constructor
	FunZoneStock(FunThingInterface _thing) {
		thing = _thing;
	}
	// Multiple item constructor
	FunZoneStock(FunThingInterface _thing, int number) {
		this(_thing);
		count = number;
	}
	
	
}
