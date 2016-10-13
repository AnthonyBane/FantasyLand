package FantasyPackage;

//Overrides fun zone factory to supply a ZoneExtra with default initialisation
class ZoneExtraFactory extends FunZoneFactory {
	ZoneExtraFactory(boolean debugging, String name) {
		super(debugging);
		zone = new ZoneExtra();
		zone.setName(name);
		zone.setDebugging(debugging);
		// Can be made more generic by passing in a string array as part of the constructor
		zone.init(this,
				"activity:" + name + "_A1:its a lot of fun:50:5\n"+
				"activity:" + name + "_A2:its a lot of fun:50:5\n"+
				"item:" + name + "_I1:its good:10:5\n"+
				"item:" + name + "_I2:its expensive:500:5\n"+
				"item:" + name + "_I3:its cheap:1:5\n"
				);
	}
}

// FunZone subclass
public class ZoneExtra extends FunZone {
	ZoneExtra() {
		this.name = "ZoneExtra";
	}
}
