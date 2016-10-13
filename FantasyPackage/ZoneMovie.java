package FantasyPackage;

// Overrides fun zone factory to supply a Movie Zone with default initialisation
class MovieZoneFactory extends FunZoneFactory {
	MovieZoneFactory(boolean debugging) {
		super(debugging);
		zone = new ZoneMovie();
		zone.setDebugging(debugging);
		zone.init(this,
				"activity:Watch - Eragon:It's a great film!:9.70:5\n"+
				"activity:Watch - The Plank:It's a classic!:5.00:5\n"+
				"item:Popcorn:Sweet or Salted!:3.50:5\n"+
				"item:Coke:Will rot your teeth!:2.00:5\n"+
				"item:Air Horn:If you get bored!:4.00:5\n"
				);
	}
}

// FunZone subclass
public class ZoneMovie extends FunZone {
	ZoneMovie() {
		this.setName("Movie Theatre");
	}
}
