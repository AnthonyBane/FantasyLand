package FantasyTest;
import java.util.Random;

import FantasyPackage.FantasyCoreInterface;
import FantasyPackage.UserInterface;
import FantasyPackage.FunZoneFactoryInterface;
import FantasyPackage.FunZoneInterface;
import FantasyPackage.ZoneProducer;

public class FantasyTestZones extends AbstractTest {
	@Override
	public String run_test(FantasyCoreInterface fantasy, UserInterface user, FunZoneInterface zone) {
		Random random = new Random();
		String new_zone = "extra:Zone" + random.nextInt(99);

        // Get singleton abstract factory
        ZoneProducer zone_producer = ZoneProducer.getZoneProducer();
        FunZoneFactoryInterface factory = zone_producer.getFactory(new_zone, fantasy.isDebugging());
        if (factory != null) {
        	FunZoneInterface azone = factory.getZone();
        	if (fantasy.registerZone(azone) && fantasy.unregisterZone(azone.getName())) {    	
        		return "Success: Added and removed new zone " + new_zone;
        	}
        }
        return "Fail: Adding and removing new zone " + new_zone;
	}
	FantasyTestZones() {
		super("Add/Remove new zone");
	}

}
