package FantasyPackage;

import java.net.URI;

// Factory class to create InitUsersStrategy objects selected by a string
public class InitUsersStrategyFactory {
	// Singleton pattern to limit the instances to exactly one
	// This may be unnecessary but limits resources being wasted
	private static InitUsersStrategyFactory singleton = new InitUsersStrategyFactory();
	// The factory method that returns strategy objects by string parameter
	public InitUsersStrategy getStrategy(String _string) {
		if (_string.startsWith("default")) {
			return new InitUsersStrategyDefault();
		}
		else if (_string.startsWith("file:")) {
			return new InitUsersStrategyFile(_string.substring(5));
		}
		else if (_string.startsWith("string:")) {
			return new InitUsersStrategyString(_string.substring(7));
		}
		else if (_string.startsWith("url:")) {
			return new InitUsersStrategyUrl(URI.create(_string.substring(4)));
		}
		return new InitUsersStrategyNull();
	}
	// Accessor for singleton object (singleton pattern implementation) 
	public static InitUsersStrategyFactory getFactory() {
		return singleton;
	}
}
