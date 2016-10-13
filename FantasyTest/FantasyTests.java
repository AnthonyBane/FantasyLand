package FantasyTest;

import java.awt.event.KeyEvent;

import FantasyGui.FantasySwing;

// FantasyTests creates a menu to access the tests
public class FantasyTests extends FantasyTestMenu {
	@Override
	public void init(FantasySwing _fantastyswing) { 
		super.init(_fantastyswing);		
		addTest(new FantasyTestCart(), KeyEvent.VK_C);
		addTest(new FantasyTestZones(), KeyEvent.VK_Z);
		addTest(new FantasyTestUsers(), KeyEvent.VK_U);
	}
	public FantasyTests() {
		super("Tests", KeyEvent.VK_T);
	}
}
