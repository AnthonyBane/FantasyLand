package FantasyPackage;

// Overrides fun zone factory to supply a Movie Zone with default initialisation
class FoodCourtFactory extends FunZoneFactory {
	FoodCourtFactory(boolean debugging) {
		super(debugging);
		zone = new ZoneFoodCourt();
		zone.setDebugging(debugging);
		zone.init(this,
				"activity:Eat in:Waiters fee upfront!:5.00:5\n"+
				"item:Coke:Regular:2.50:5\n"+
				"item:Burgur:So good:4.50:5\n"+
				"item:Chips:Crispy!:1.50:5\n"
				);
	}
}

// FunZone subclass
public class ZoneFoodCourt extends FunZone {
	ZoneFoodCourt() {
		this.setName("Food Court");
	}
}
