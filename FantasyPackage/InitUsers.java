package FantasyPackage;

// Helper class used to implement strategy patterns
public class InitUsers {
	// Stores the strategy in use
	InitUsersStrategy strategy = null;
	// Accessor to set the strategy
	public void setStrategy(InitUsersStrategy _string) { strategy = _string; }
	// Method used to initilise a FantasyCore via the current strategy
	public boolean init(FantasyCore core) {
		// If no strategy
		if (strategy == null) {
			// return failure
			return false;
		}
		// Perform initialisation
		strategy.init(core);
		// Success
		return true;
	}
}
