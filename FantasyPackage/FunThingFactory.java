package FantasyPackage;

// Abstract class to return generic new fun thing instances
public abstract class FunThingFactory {
	public abstract FunThingInterface getThing(String name, String description, double cost);
}
