package FantasyPackage;

// Interface class for zone purchasable fun things 
// Root for all purchasable items
public abstract class FunThing implements FunThingInterface {
	public abstract String getName();
	public abstract String getDescription();
	public abstract double getCost();
	// Returns category of purchasable thing
	public abstract String getType();
}
