package FantasyPackage;

// Overrides fun zone factory to supply a Movie Zone with default initialisation
class GamesRoomFactory extends FunZoneFactory {
	GamesRoomFactory(boolean debugging) {
		super(debugging);
		zone = new ZoneGamesRoom();
		zone.setDebugging(debugging);
		zone.init(this,
				"activity:Pacman arcade:A classic!:1.00:5\n"+
				"item:Popcorn:Sweet or Salted!:3.50:5\n"+
				"item:Coke:Will rot your teeth!:2.00:5\n"+
				"item:Air Horn:If you get bored!:4.00:5\n"
				);
	}
}

// FunZone subclass
public class ZoneGamesRoom extends FunZone {
	ZoneGamesRoom() {
		this.setName("Games Room");
	}
}
