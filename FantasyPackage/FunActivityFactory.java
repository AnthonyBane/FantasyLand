package FantasyPackage;

// Factory to return new instances of fun activities
public class FunActivityFactory extends FunThingFactory {
	@Override
	public FunThing getThing(String name, String description, double cost) {
		//Checks to determine field validity
		if ((name == null) || (description == null) || (cost < 0)) {
			return null;
		}
		return new FunActivity(name, description, cost);
	}
}
