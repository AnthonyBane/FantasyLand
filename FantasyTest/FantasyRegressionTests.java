package FantasyTest;
import FantasyPackage.Debugger;
import FantasyPackage.FantasyCore;
import FantasyPackage.FunZoneInterface;
import FantasyPackage.UserInterface;

import java.util.ArrayList;
import java.util.List;

public class FantasyRegressionTests {
	
	private Debugger debugger;
	private List<AbstractTest> tests = new ArrayList<AbstractTest>();
	
	private void addTest(AbstractTest test) {
		tests.add(test);
	}
	
	public void runTests() {
		FantasyCore fantasy = FantasyCore.getFantasy(debugger.isDebugging(),"default");
		fantasy.registerUser("Tester");
		FunZoneInterface zone = fantasy.getZone(0);
		UserInterface user = fantasy.getUser(0);
		debugger.debug("Running regressions");
		for (AbstractTest test:tests) {
			String result = test.run_test(fantasy, user, zone);
			System.out.println(result);
		}
		debugger.debug("Regressions done");
	}
	
	public FantasyRegressionTests(boolean debugging) {
		debugger = new Debugger();
		debugger.setName("Regressions");
		debugger.setDebug(debugging);
		addTest(new FantasyTestCart());
		addTest(new FantasyTestZones());
		addTest(new FantasyTestUsers());
	}
	
	public static void main(String[] args) {
		FantasyRegressionTests regressions = new FantasyRegressionTests(false);
		System.out.println("Starting Tests");
		regressions.runTests();
		System.out.println("Tests complete");
	}
}
