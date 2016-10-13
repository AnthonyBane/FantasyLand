package FantasyPackage;

// Utility class for debug assistance
// Outputs a string to the console prefixed by an identifier
// Can be enabled/disabled
public class Debugger implements DebuggerInterface {
	// Enable/disable debug output
	private boolean show_debug = true;
	// Prefix for debug instance identification
	private String main_prefix = "";
	// Additional text option
	private String sub_prefix = "";
	// Accessor to see if debugging is enabled
	public boolean isDebugging() {
		return show_debug;
	}
	public void setName(String name) {
		main_prefix = name;
	}
	// Qualify debug item by type
	public void setType(String txt) {
		sub_prefix = txt;
	}
	// Retrieve combined debug prefix
	private String getPrefix() {
		if (sub_prefix != null) {
			return main_prefix+"("+sub_prefix+")";
		} else {
			return main_prefix;
		}
	}
	// Expose enable/disable debug
	public void setDebug(boolean enable) {
		show_debug = enable;
	}
	// Submit string for output
	public void debug(String _string) {
		if (show_debug){
			System.out.println(getPrefix() + ": " + _string);
		}
	}
	// Unconditionally submit string for output
	public void error(String text) {
		System.err.println("ERROR: " + getPrefix() + ": " + text);
	}
}
