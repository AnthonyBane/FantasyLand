package FantasyTest;

import java.util.Random;
import FantasyPackage.FunThingInterface;
import FantasyPackage.FantasyCoreInterface;
import FantasyPackage.FunZoneInterface;
import FantasyPackage.UserInterface;

public class FantasyTestCart extends AbstractTest {
	int count = 0;
	@Override
	public String run_test(FantasyCoreInterface fantasy, UserInterface user, FunZoneInterface zone) {
		Random random = new Random();
        int number = random.nextInt(50);
        int bought = 0;
        int returned = 0;
        int tries = 0;
        for (int i = 0; i < number; i++) {
        	FunThingInterface item = zone.getItem(random.nextInt(zone.getCount()));
        	tries++;
        	if (fantasy.buy(user, zone, item.getName())) {
        		bought++; 
        	}
        	if (fantasy.unbuy(user, item.getName())) {
        		returned++;
        	}
        }
        if ((bought == returned) && (bought == tries)) {
        	return "Success: Bought and returned " + tries + " items to cart";
        }
        else {
        	return "Fail: out of " + tries + " bought " + bought + " and returned " + returned;
        }
	}
	FantasyTestCart() {
		super("Add/Remove items from cart","Add random number of items to cart");
	}
}
