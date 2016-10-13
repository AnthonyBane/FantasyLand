package FantasyTest;

import java.util.Random;

import FantasyPackage.FantasyCoreInterface;
import FantasyPackage.FunZoneInterface;
import FantasyPackage.UserInterface;

public class FantasyTestUsers extends AbstractTest {
	public String run_test(FantasyCoreInterface fantasy, UserInterface user, FunZoneInterface zone) {
		int count = fantasy.getUserCount();
		Random random = new Random();
        int number = random.nextInt(50);

		for (int i = 0; i < number; i++) {
			fantasy.registerUser("User" + count+i);
		}
		
		for (int i = 0; i < number; i++) {
			fantasy.unregisterUser("User" + count+i);
		}
		
		if (count == fantasy.getUserCount()) {
			return "Success: Added and removed " + number + " users";
		} else {
			return "Fail: Add and remove " + number + " users. Should leave " + count + " left but " + fantasy.getUserCount() + " remain";			
		}
	}
	FantasyTestUsers() {
		super("Add/remove new users","Add and remove new users");
	}


}
