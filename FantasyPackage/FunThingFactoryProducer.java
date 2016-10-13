package FantasyPackage;

public class FunThingFactoryProducer {
	static public FunThingFactory getFactory(String type) {
    	if (type.equalsIgnoreCase("activity")) {
    		return new FunActivityFactory();
    	} else if (type.equalsIgnoreCase("item")) {
    		return new FunItemFactory();
    	}
    	return null;
	}

}
