package FantasyGui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class FantasyLand extends JFrame {
	// Singleton instance of the application
	private static FantasyLand fantasy_land = new FantasyLand();
	
	// Instance of the swing application
	private FantasySwing fantasy_swing = new FantasySwing();
    
	public FantasyLand() {
		
        // Frame size
        setSize(Runtime.width(), Runtime.height());

        // Swing menubar
		setJMenuBar(fantasy_swing.getMenuBar());
		
		// Panel located at the top of the pane
		getContentPane().add(fantasy_swing.getPanel(), BorderLayout.PAGE_START);
         
        // Exit on closing the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        // Frees frame from a particular screen location
        setLocationRelativeTo(null);

        // Display initial help
        fantasy_swing.showHelp();
	}

	public static void main(String[] args)  throws InterruptedException {
		// Main entry point
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	fantasy_land.setVisible(true);
            }
		});
	}

}
