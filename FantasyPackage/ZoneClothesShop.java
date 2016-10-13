package FantasyPackage;

// Overrides fun zone factory to supply a Movie Zone with default initialisation
class ClothesShopFactory extends FunZoneFactory {
	ClothesShopFactory(boolean debugging) {
		super(debugging);
		zone = new ZoneClothesShop();
		zone.setDebugging(debugging);
		zone.init(this,
				"activity:Customised shirt:Your own styling:15.00:5\n"+
				"item:Red Shirt:Simple Red:5.00:5\n"+
				"item:White Shirt:Simple:4.00:5\n"+
				"item:Trainers:Good for your feet:20.00:5\n"
				);
	}
}

// FunZone subclass
public class ZoneClothesShop extends FunZone {
	ZoneClothesShop() {
		this.setName("Clothes Shop");
	}
}
