package FantasyPackage;

import java.util.List;

// Base strategy class implementing InitUsersStrategy that initialises users via a string
// This string may be obtained by several alternative classes
public class InitUsersStrategyString implements InitUsersStrategy {
	// The string array of names
	private String[] user_names = null;
	// Setter method for a string list
	public void setUserNames(String[] _string) {
		user_names = _string;
	}
	// Setter method for a string list
	public void setUserNames(List<String> list) {
		user_names = (String[]) list.toArray(new String[list.size()]);
	}
	// The actual initialisation
	protected void doInit(FantasyCoreInterface fantasyCore) {
		// If we have any names and a fantasyCore to initialise
		if ((user_names != null) && (fantasyCore != null)) {
			// Iterator pattern to iterate over the user names
			for (String name:user_names ) {
				// Initialise with names after removing leading and trailing whitespace
				fantasyCore.registerUser(name.trim());
			}
		}
	}
	// Public method to run the initialisation
	public void init(FantasyCoreInterface fantasyCore) {
		doInit(fantasyCore);
	} 
	// Constructor using a string with names separated by comma delimited fields
	public InitUsersStrategyString(String _string) {
		setUserNames(_string.split(","));
	}
	public InitUsersStrategyString() {
	}
}
