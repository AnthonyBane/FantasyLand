package FantasyPackage;

public interface DebuggerInterface {
	// Accessor to see if debugging is enabled
	boolean isDebugging();

	// set name of this debugger
	void setName(String name);
	
	// Qualify debug item by type
	void setType(String txt);

	// Expose enable/disable debug
	void setDebug(boolean enable);
	
	// Submit string for output
	void debug(String _string);
	
	// Unconditionally submit string for output
	void error(String text);

}
