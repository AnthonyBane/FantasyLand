package FantasyTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import FantasyGui.FantasySwing;

// Menu management for FantasyLand tests
// Maintains a menu with associated tests that are run when clicked
public class FantasyTestMenu {
	private FantasySwing gui;
	protected JMenu menu;

	public void addTest(final AbstractTest test, int virtual_key) {
		JMenuItem anItem = new JMenuItem(test.getName());
		anItem.setToolTipText(test.getHelp());
		if (virtual_key != 0) {
			anItem.setMnemonic(virtual_key);
		}
		anItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
            		String result = test.run_test(gui.getFantasy(), gui.getCurrentUser(), gui.getCurrentZone());
            		JOptionPane.showMessageDialog(null, result, test.getName(), JOptionPane.PLAIN_MESSAGE);
            		gui.updateInfo();
            }
		});
		menu.add(anItem);
	}
	public void addTest(final AbstractTest test) {
		addTest(test, 0);
	}
	public void init(FantasySwing _fantasySwing) {
		gui = _fantasySwing;
	}
	public JMenu getMenu() {
		return menu;
	}
	public FantasyTestMenu(String name, int virtual_key) {
		menu = new JMenu(name);
		if (virtual_key != 0)
			menu.setMnemonic(virtual_key);
	}
	public FantasyTestMenu(String name, FantasySwing _fantasySwing, int virtual_key) {
		menu = new JMenu(name);
		if (virtual_key != 0)
			menu.setMnemonic(virtual_key);
		init(_fantasySwing);
	}
}
