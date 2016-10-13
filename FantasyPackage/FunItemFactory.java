package FantasyPackage;

// Factory to create new fun item instances
public class FunItemFactory extends FunThingFactory {
	@Override
	public FunThing getThing(String name, String description, double cost) {
		//Checks to confirm field validity
		if ((name == null) || (description == null) || (cost < 0)) {
			return null;
		}
		return new FunItem(name, description, cost);
	}
}
