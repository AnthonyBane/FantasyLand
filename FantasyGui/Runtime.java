package FantasyGui;

public class Runtime {
	static public boolean isDevelopment() { return true; }
	static public int width() { return 400; }
	static public int height() { return 500; }
	static public String getLanguage() { return "en"; }
	static public String getcountry() { return "GB"; }
	static public boolean internationalisation() { return false; }
	// strategies are null, default, string, file, url
	//Examples of strategy selection strings, see InitUsersStrategyFactory.java
//	static public String getUsersStrategyString() { return "file:/home/ant/users.txt"; }
//	static public String getUsersStrategyString() { return "url:http://anthonybane.com/FantasyLand/users.txt"; }
	static public String getUsersStrategyString() { return "string:Anthony,Jess,Jenny,Rae,Nick"; }
//	static public String getUsersStrategyString() { return "default"; }
}
