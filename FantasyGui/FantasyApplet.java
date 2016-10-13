package FantasyGui;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;

/*
<applet code="FantasyApplet" width=300 height=500>
</applet>
 */

@SuppressWarnings("serial")
public class FantasyApplet extends JApplet {
	
	private static FantasyApplet fantasy_applet = new FantasyApplet();
	
	// Instance of the swing application
	private FantasySwing fantasy_swing = new FantasySwing();
    
	public void init() {
		// Set size
        setSize(Runtime.width(), Runtime.height());

		// Attach swing menubar
		setJMenuBar(fantasy_swing.getMenuBar());
		
		// Panel located at the top of the pane
		getContentPane().add(fantasy_swing.getPanel(), BorderLayout.PAGE_START);

        // Display initial help
        fantasy_swing.showHelp();
	}

	public static void main(String[] args) throws InterruptedException {
		// Main entry point
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	fantasy_applet.setVisible(true);
            }
		});
	}

}
