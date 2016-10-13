package FantasyPackage;

// Factory class for returning a fun zone and n associated thing factory
public abstract class FunZoneFactory extends Debugger  implements FunZoneFactoryInterface {
	// Singleton fun item factory
	private static FunItemFactory fun_item_factory = new FunItemFactory();
	// Singleton fun activity factory
	private static FunActivityFactory fun_activity_factory = new FunActivityFactory();
	// The zone being returned by subclasses
	protected FunZoneInterface zone = null;
	public FunZoneInterface getZone() { return zone; }
	// Could be overridden by a subclass to extend FunThingFactory variants
	public FunThingFactory getThingFactory(String type) {
		if (type.equalsIgnoreCase("activity")) {
			return fun_activity_factory;
		} else if (type.equalsIgnoreCase("item")) {
			return fun_item_factory;
		} else {
			return null;
		}
	}
	// Debugging is used by implementations to enable/disable debug for factories
	FunZoneFactory(boolean debugging) {
		setName("FunZoneFactory");
		setDebug(debugging);
	}
}

