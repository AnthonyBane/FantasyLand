package FantasyPackage;

public interface FunZoneFactoryInterface extends DebuggerInterface {
	// retrieves a zone
	public FunZoneInterface getZone();
	// retrieves a FunThing Factory based on type
	public FunThingFactory getThingFactory(String type);
}
