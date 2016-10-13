package FantasyPackage;

public class InitUsersStrategyDefault implements InitUsersStrategy {
	public void init(FantasyCoreInterface fantasy) {
		fantasy.registerUser("Anthony");
		fantasy.registerUser("Jessica");
		fantasy.registerUser("Jenny");
		fantasy.registerUser("Rae");
		fantasy.registerUser("Nick");		
	}
}
