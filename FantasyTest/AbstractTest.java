package FantasyTest;
import FantasyPackage.FantasyCoreInterface;
import FantasyPackage.FunZoneInterface;
import FantasyPackage.UserInterface;

// AbstractTest is an abstract class which runs a test using a specific zone and user
// Help text is used as a tooltip by the gui manager
public abstract class AbstractTest {
	protected String name;
	protected String help;
	public String getName() { return name; }
	public String getHelp() { return help; }
	public abstract String run_test(FantasyCoreInterface fantasy, UserInterface user, FunZoneInterface zone);
	public AbstractTest(String name) {
	  this.name=name;
	  help = name;
    }
	public AbstractTest(String name, String help) {
		this.name = name;
		this.help = help;
	}
}
