package FantasyPackage;

// Abstract Factory pattern returns class factories and declare the list of default factories
// Additional empty zones factories can be created on the fly but are not singletons.
public class ZoneProducer {	

	// Singleton instantiations of fun zones
	static private FunZoneFactoryInterface games_room_factory = null;
	static private FunZoneFactoryInterface food_court_factory = null;
	static private FunZoneFactoryInterface clothes_shop_factory = null;
	static private FunZoneFactoryInterface movie_zone_producer = null;
	
	static private ZoneProducer zone_producer = new ZoneProducer();
	static private String[] zone_names = {"Clothes Shop","Food Court","GamesRoom","MovieTheatre"}; 
	
	// Private constructor to avoid multiple instances
	private ZoneProducer() {
	}
	// Return list of supported singleton factories
	public String[] getZoneNames() {
		return zone_names;
	}
	// Return singleton class instance
	public static ZoneProducer getZoneProducer() {
		return zone_producer;
	}
	// Return singleton instances of zone factories and non-singleton extension zone factories
	public FunZoneFactoryInterface getFactory(String choice, boolean debugging) {
		if (choice == "GamesRoom") {
			if (games_room_factory == null) {
				games_room_factory = new GamesRoomFactory(debugging);
			}
			return games_room_factory;
		} else if (choice == "Food Court") {
			if (food_court_factory == null) {
				food_court_factory = new FoodCourtFactory(debugging);
			}
			return food_court_factory;
		} else if (choice == "Clothes Shop") {
			if (clothes_shop_factory == null) {
				clothes_shop_factory = new ClothesShopFactory(debugging);
			}
			return clothes_shop_factory;
		} else if (choice == "MovieTheatre") {
			if (movie_zone_producer == null) {
				movie_zone_producer = new MovieZoneFactory(debugging);
			}
			return movie_zone_producer;
		}
		/*
		 	option to add new zones on the fly for debugging - **not** singleton factories
		*/
		else if (choice.startsWith("extra:") && (choice.length() > 6)) {
			return new ZoneExtraFactory(debugging, choice.substring(6));
		} else {
			return null;
		}
	}
}
