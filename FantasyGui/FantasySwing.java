package FantasyGui;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import FantasyPackage.CartEntry;
import FantasyPackage.CartInterface;
import FantasyPackage.Debugger;
import FantasyPackage.FantasyCoreInterface;
import FantasyPackage.FunThingInterface;
import FantasyPackage.FunZoneInterface;
import FantasyPackage.FunZoneStock;
import FantasyPackage.UserInterface;
import FantasyTest.FantasyTests;

// Required for singleton access via object's static method
import FantasyPackage.FantasyCore;

@SuppressWarnings("serial")
public class FantasySwing extends Debugger {
	
	// Locale currentLocale;
	ResourceBundle messages;
	
	// Instance of object implementing Controller interface
	private FantasyCoreInterface fantasy = null;
	
	// Accessor for tests
	public FantasyCoreInterface getFantasy() {
		return fantasy;
	}
			
	// Tests menu
	// Tests can be hidden for production and enabled for debug
	private boolean enable_tests = Runtime.isDevelopment();

	// Swing menubar
    private JMenuBar menubar = new JMenuBar();
    JMenuBar getMenuBar() { return menubar; }
    
    // Access for about menu
	private JMenu aboutMenu;
	// menu option to add tests
    private JMenuItem addTests;
          
	// Object for status information 
	private JLabel info = new JLabel();
	private JScrollPane scroller;
	private JPanel panel;
	public JPanel getPanel() { return panel; }

	// Context used for storing current user and zone
	// A multiple concurrent user variation might use multiple contexts
	private Context context = new Context();
	
	public UserInterface getCurrentUser() { if (context != null) return context.user; return null; }
	public FunZoneInterface getCurrentZone() { if (context != null) return context.zone; return null; }
	    
    // Wrapper for setting the current user and updating information status
    private void setUser(String _user) {
    	context.user = fantasy.getUser(_user);
    	updateInfo();
    }
    
    // Wrapper for setting the current zone and updating information status
    private void setZone(String z) {
    	context.zone = fantasy.getZone(z);
    	updateInfo();
    }
    
    // Returns HTML encoded test string of cart information
    private String getCartString() {

    	String string = "<html><h3>(empty)</h3>";
    	CartInterface cart = context.user.getCart();
    	int count = cart.getCartEntryCount();
    	int counted = 0;
		double total = 0;
    	if (count > 0) {
    		string = "<html><h3>Goods:</h3>";
    		counted = 0;
    		Iterator<CartEntry> iterator = cart.getCartEntryIterator();
    		while (iterator.hasNext()) {
    			CartEntry entry = iterator.next();
    			FunThingInterface thing = entry.getFunThing();
    			if (thing.getType() == "item") {
    				string += "<li>" + thing.getName() + "(" + entry.getCount() +") &pound;" + thing.getCost() + "</li>";
    				total += thing.getCost() * entry.getCount();
    				counted++;
    			}
    		}
    		if (counted == 0) {
    			string += "<ul>(none)</ul>";
    		}
    		string += "<html><h3>Activities:</h3>";
    		counted = 0;
			iterator = cart.getCartEntryIterator();
			while (iterator.hasNext()) { 
				CartEntry entry = iterator.next();
				FunThingInterface thing = entry.getFunThing();
    			if (thing.getType() == "activity") {
    				string += "<li>" + thing.getName() + "(" + entry.getCount() + ") &pound;" + thing.getCost() + "</li>";
    				total += thing.getCost() * entry.getCount();
    				counted++;
    			}
    		}
    		if (counted == 0) {
    			string += "<ul>(none)</ul>";
    		}
    	}
		string += "<h3>Credit:</h3><p>Paid: &pound;" + cart.getCredit() + "</p>";
		string += "<h3>To Pay:</h3><p>Total: &pound;" + (total - cart.getCredit()) + "</p></html>";
    	return string;
    }
 
    private FunThingInterface[] stockPossibility(String type){
    	FunZoneInterface _zone = context.zone;
       	
    	Iterator<FunZoneStock> items = _zone.getIterator();
    	FunThingInterface[] possibilities = new FunThingInterface[_zone.getCount(type)];
    	
    	int index = 0;
    	while (items.hasNext()) {
    		FunZoneStock stock = items.next();
    		if (stock.getType() == type){
    			possibilities[index++] = (FunThingInterface)stock.getThing();
    		}
    	}
    	return possibilities;
    }
   
    // Method to create and display status information on info object
    public void updateInfo() {
    	String displayText = "<html>";
    	String name = "";
    	if (context.user != null) {
    		name = context.user.getName();
    	}
    	displayText += "<h2>User: " + name + "</h2>"; 
    	if (context.zone != null)
    		displayText += "<hr/><h2>Zone: " + context.zone.getName() + "</h2>";
 		int notificationCount = context.user.getNotificationCount();
    	if (notificationCount > 0) {
    		displayText += "<hr/><h2>Notifications:</h2>";
    	   	Iterator<String> iterator = context.user.getNotificationIterator();
   			while(iterator.hasNext()) {
    			displayText += "<p>" + iterator.next() + "</p>";
    		}
    	}
    	notificationCount = context.user.getCart().getCartEntryCount();
    	if (notificationCount > 0) {
    		displayText += "<hr/><h2>Cart:</h2>"+getCartString();
    	}
    	displayText += "</html>";
    	info.setText(displayText);
    	
    	// Resize panel height to info height and scroller to visible rectangle of panel
    	if ((panel != null) && (scroller != null)) {
    		Rectangle bounds = panel.getBounds();
    		bounds.height = info.getPreferredSize().height;
    		panel.setBounds(bounds);
    		scroller.setBounds(panel.getVisibleRect());
    	}
    }
        
    // Display user help
    public void showHelp() {
		JOptionPane.showMessageDialog(null, 
				"<html>"
				+ "<h1>Please purchase items/activities from the FunZones</h1>"
				+ "<h2>You can:</h2>"
				+ "<li>Select current user</li>"
				+ "<li>Select current fun zone</li>"
				+ "<li>Add items or activities to the cart</li>"
				+ "<li>Watch/Stop watching a zone for new products</li>"
				+ "<li>Review product selection</li>"
				+ "<li>and Checkout!</li>"
				+ "</html>",
				"Welcome To FantasyLand!", JOptionPane.PLAIN_MESSAGE);
    }
    
    // Add tests menu
    private void AddTests() {
    	// Add only if the about menu is available
    	if (aboutMenu != null) {
    		
    		// Get test set
    		FantasyTests tests = new FantasyTests();
    		
    		// Initialise with instance information
    	    tests.init(this);

    	    // Add the menu
    		aboutMenu.add(tests.getMenu());
    		
    		// Hide the test addition request menu entry
	    	addTests.setVisible(false);
    	}
    }

    // Action that displays a dialog with the current cart status
    private class MenuItemCartListAction extends AbstractAction {
        public MenuItemCartListAction(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
           	JTextPane textPane = new JTextPane();
        	textPane.setContentType("text/html");
        	textPane.setText(getCartString());
        	textPane.setCaretPosition(0);
        	textPane.setBackground(panel.getBackground());
        	JScrollPane scroller = new JScrollPane(textPane);                	
        	scroller.setPreferredSize(new Dimension(300, 400));
        	scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        	scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        	
        	JOptionPane.showMessageDialog(null, scroller, "Cart Contents", JOptionPane.PLAIN_MESSAGE);        		
        }
    }
    
    // Action that selects a cart 'thing' and deletes it if requested
    private class MenuItemCartDeleteAction extends AbstractAction {
        public MenuItemCartDeleteAction(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	CartInterface cart = context.user.getCart();
        	Object[] possibilities = new String[cart.getCartEntryCount()];
        	if ( cart.getCartEntryCount() > 0) {
            	Iterator<CartEntry> iterator = cart.getCartEntryIterator();
            	int index = 0;
        		while (iterator.hasNext()) {
        			possibilities[index++] = iterator.next().getFunThing().getName();
        		}
    			String _string = (String)JOptionPane.showInputDialog(
	    						null,
	    	                    "Choose item to remove or Cancel:\n",
	    	                    "Remove Item",
	    	                    JOptionPane.PLAIN_MESSAGE,
	    	                    null,
	    	                    possibilities,
	    	                    "");

        		// If a string was returned
        		if ((_string != null) && (_string.length() > 0)) {
        			fantasy.unbuy(context.user, _string);
            		updateInfo();
        	    	return;
        		}
        	} else {
            	JOptionPane.showMessageDialog(null, "Nothing in Cart", "Checkout", JOptionPane.PLAIN_MESSAGE);
        	}
        }
    }

    // Action to pay cart contents
    private class MenuItemCartPayAction extends AbstractAction {
        public MenuItemCartPayAction(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
    		CartInterface cart = context.user.getCart();
    		double toPay = cart.getTotal() - cart.getCredit();
    		
    		if (toPay <= 0) {
    			if (cart.getCartEntryCount() > 0) {
    				JOptionPane.showMessageDialog(null, "You have sufficient credit to checkout", "Pay", JOptionPane.PLAIN_MESSAGE);
    			}
    			else {
            		JOptionPane.showMessageDialog(null, "Nothing in Cart", "Pay", JOptionPane.PLAIN_MESSAGE);    				
    			}
    		} else {
        		JOptionPane.showMessageDialog(null, "<html>Paid &pound;" + toPay + "</html>", "Pay", JOptionPane.PLAIN_MESSAGE);
    			cart.pay(toPay);
        		updateInfo();
    		}
       }
    }
    
    // Action to refund cart balance
    private class MenuItemCartRefundBalanceAction extends AbstractAction {
        public MenuItemCartRefundBalanceAction(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
    		CartInterface cart = context.user.getCart();
    		double toRefund = cart.getCredit() - cart.getTotal();
    		
    		if (toRefund <= 0) {
            		JOptionPane.showMessageDialog(null, "No balance available for refund", "Refund", JOptionPane.PLAIN_MESSAGE);    				
    		} else {
        		JOptionPane.showMessageDialog(null, "<html>Refunded &pound;" + toRefund + "</html>", "Refund", JOptionPane.PLAIN_MESSAGE);
    			cart.refundCredit(toRefund);
        		updateInfo();
    		}
       }
    }

    // Action to refund cart credit
    private class MenuItemCartRefundCreditAction extends AbstractAction {
        public MenuItemCartRefundCreditAction(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
    		CartInterface cart = context.user.getCart();
    		double toRefund = cart.getCredit();
    		
    		if (toRefund <= 0) {
            		JOptionPane.showMessageDialog(null, "No credit availabe", "Refund", JOptionPane.PLAIN_MESSAGE);    				
    		} else {
        		JOptionPane.showMessageDialog(null, "<html>Refunded &pound;" + toRefund + "</html>", "Refund", JOptionPane.PLAIN_MESSAGE);
    			cart.refundCredit(toRefund);
        		updateInfo();
    		}
       }
    }
    
    // Action to checkout cart contents
    private class MenuItemCartCheckoutAction extends AbstractAction {
        public MenuItemCartCheckoutAction(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
    		CartInterface cart = context.user.getCart();
    		if (cart.getTotal() == 0) {
        		JOptionPane.showMessageDialog(null, "Nothing in Cart", "Checkout", JOptionPane.PLAIN_MESSAGE);
    		} else if (cart.getCredit() >= cart.getTotal()) {
    			cart.checkout();
        		JOptionPane.showMessageDialog(null, "Checked out ok", "Checkout", JOptionPane.PLAIN_MESSAGE);
        		updateInfo();
    		} else {
        		JOptionPane.showMessageDialog(null, "Checkout failed - have you paid?", "Checkout", JOptionPane.PLAIN_MESSAGE);
    		}
        }
    }

    private String selectFunItem(FantasySwing parent, String type) {
    	JPanel thePanel = new JPanel();

    	final FunThingInterface[] things;
    	final String[] names;

		things = parent.stockPossibility(type);
		if (things.length == 0) {
			return null;
		}
		
		names = new String[things.length];
		for (int i =0; i < things.length; i++) {
			names[i] = things[i].getName();
			debug("thing " + things[i].getName()+" Desc: " + things[i].getDescription());
		}

		final JPanel labelContainer = new JPanel();
    	final JLabel theLabel = new JLabel();
		final JComboBox<String> theComboBox = new JComboBox<String>(names);
		
		thePanel.setLayout((LayoutManager) new BoxLayout(thePanel, BoxLayout.PAGE_AXIS));
    	thePanel.add(theComboBox, BorderLayout.CENTER);
    	theLabel.setText(things[0].getDescription());
//    	labelContainer.setBorder(BorderFactory.createLineBorder(Color.black));   // Swap with line below for a border
    	labelContainer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    	labelContainer.add(theLabel,BorderLayout.CENTER);
    	thePanel.add(labelContainer,BorderLayout.CENTER);

        theComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
				String name = (String)theComboBox.getSelectedItem();
				for (int i=0; i < things.length; i++) {
					if (things[i].getName().equals(name)) {
						theLabel.setText(things[i].getDescription());						
					}
				}
            }
        });
    	
    	Object [] options = {"Select", "Cancel"};
    	JOptionPane _optionPane = new JOptionPane(thePanel, JOptionPane.PLAIN_MESSAGE , JOptionPane.YES_NO_OPTION, null, options, null);

    	JDialog dialog = _optionPane.createDialog(null, "Select Purchase");
    	dialog.setVisible(true);

    	Object result = _optionPane.getValue();
    	String choice = "";
    	if ((result != null) && (result.toString().equals("Select"))) {
    		choice = (String) theComboBox.getSelectedItem();
    	}
    	return choice;
    }

    // method to create the menubar
    private void createMenuBar() {

    	final FantasySwing swing = this;
    	
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit", null);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        // Add exit item
        file.add(eMenuItem);

        // Add to menubar
        menubar.add(file);

        // Create user menu
        JMenu user_menu = new JMenu("User");
        user_menu.setMnemonic(KeyEvent.VK_U);
        
        // Create selection of user menu item
        JMenuItem select_user = new JMenuItem("Choose User");
        select_user.setMnemonic(KeyEvent.VK_S);
        select_user.setToolTipText("Select a user");
        select_user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	Object[] possibilities = new String[fantasy.getUserCount()];
            	int index = 0;
            	Iterator<UserInterface> iterator = fantasy.getUserIterator();
            	while (iterator.hasNext()) {
            		possibilities[index++] = iterator.next().getName();
            	}
            	
            	String _string = (String)JOptionPane.showInputDialog(
            						null,
            	                    "Choose the user to login:\n",
            	                    "Login Dialog",
            	                    JOptionPane.PLAIN_MESSAGE,
            	                    null,
            	                    possibilities,
            	                    context.user.getName());

            	//If a string was returned
            	if ((_string != null) && (_string.length() > 0)) {
            		setUser(_string);
            	    return;
            	}
            }
        });
        
        // Add menu item
        user_menu.add(select_user);

        // Create watch menu item
        JMenuItem observe = new JMenuItem("Watch Current Zone");
        observe.setMnemonic(KeyEvent.VK_W);
        observe.setToolTipText("Get Item addition notifications for current zone");
        observe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	fantasy.registerObserver((Observer)context.user, context.zone);
        		JOptionPane.showMessageDialog(null, "Registered for FunThing addition notifications for " + context.zone.getName(), "Watch", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        // Add to user menu
        user_menu.add(observe);

        // Create unwatch menu item
        JMenuItem unobserve = new JMenuItem("Stop Watching Current Zone");
        unobserve.setMnemonic(KeyEvent.VK_S);
        unobserve.setToolTipText("Stop getting Item addition notifications for current zone");
        unobserve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	fantasy.unregisterObserver((Observer)context.user, context.zone);
        		JOptionPane.showMessageDialog(null, "Deregistered for addition notifcations from " + context.zone.getName(), "Watch", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        // Add to user menu
        user_menu.add(unobserve);
        
        // Create clear notifications menu item
        JMenuItem clearNotifications = new JMenuItem("Clear Notifications");
        clearNotifications.setMnemonic(KeyEvent.VK_I);
        clearNotifications.setToolTipText("Clear notifications");
        clearNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if (context.user.getNotificationCount() > 0) {
            		context.user.clearNotifications();
            		JOptionPane.showMessageDialog(null, "Cleared", "Notifications", JOptionPane.PLAIN_MESSAGE);
            	} else {
            		JOptionPane.showMessageDialog(null, "None found", "Notifications", JOptionPane.PLAIN_MESSAGE);
            	}
        		updateInfo();
            }
        });
        
        // Add clearNotifications to user_menu
        user_menu.add(clearNotifications);

        // Add user menu to menubar
        menubar.add(user_menu);

        // Create zone menu
        JMenu zonesMenu = new JMenu("Zones");
        zonesMenu.setMnemonic(KeyEvent.VK_Z);

        // Create selection of current zone menu item
        JMenuItem select_zone = new JMenuItem("Select Zone");
        select_zone.setMnemonic(KeyEvent.VK_Z);
        select_zone.setToolTipText("Select a Fun Zone");
        select_zone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	Object[] possibilities = new String[fantasy.getZoneCount()];
            	int index = 0;
            	Iterator<FunZoneInterface> iterator = fantasy.getZoneIterator();
            	while (iterator.hasNext()) {
            		possibilities[index++] = iterator.next().getName();
            	}
            	
            	String _string = (String)JOptionPane.showInputDialog(
            						null,
            	                    "Choose Zone:\n",
            	                    "Zone Dialog",
            	                    JOptionPane.PLAIN_MESSAGE,
            	                    null,
            	                    possibilities,
            	                    context.zone.getName());

            	// If a string was returned
            	if ((_string != null) && (_string.length() > 0)) {
            		setZone(_string);
            	    return;
            	}
            }
        });
        
        // Add to zone menu
        zonesMenu.add(select_zone);
        
        // Show current zone contents
        JMenuItem show_zone = new JMenuItem("Contents");
        show_zone.setMnemonic(KeyEvent.VK_S);
        show_zone.setToolTipText("Show Fun Zone Contents");
        show_zone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if (context.zone != null) {
            		String activities="";
            		String products="";
            		Iterator<FunZoneStock> iterator = context.zone.getIterator();
            		while (iterator.hasNext()) {
                		FunZoneStock stock = iterator.next();
                		String _string = "<ul>" + stock.getName() + " &pound;" + stock.getThing().getCost() + " (" + stock.getCount() + ")</ul>";
                		if (stock.getType() == "activity") {
                			activities += _string;
                		}
                		else {
                			products += _string;
                		}
                	}
                	String content = "<html><h3>Stock &pound; cost (quantity in stock)</h3><h3>Products:</h3>" + products + "<h3>Activities:</h3>" + activities + "</html>";
                	JTextPane textPane = new JTextPane();
                	textPane.setContentType("text/html");
                	textPane.setText(content);
                	textPane.setBackground(panel.getBackground());
                	textPane.setCaretPosition(0);
                	JScrollPane scroller = new JScrollPane(textPane);                	
                	scroller.setPreferredSize(new Dimension(300, 400));
                	scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                	scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
               		JOptionPane.showMessageDialog(null, scroller, "Zone: " + context.zone.getName(), JOptionPane.PLAIN_MESSAGE);
            	}
            	else {
            		JOptionPane.showMessageDialog(null, "(empty)", "No zones available", JOptionPane.PLAIN_MESSAGE);            		
            	}
            }
        });
        
        // Add to zone menu
        zonesMenu.add(show_zone);
        
        // Create purchasing of a thing to zone        
        JMenu purchase = new JMenu("Purchase");
        purchase.setMnemonic(KeyEvent.VK_P);
        purchase.setToolTipText("Purchase a Fun Zone activity or product");
        JMenuItem activity = new  JMenuItem("Activity");
        activity.setMnemonic(KeyEvent.VK_A);
        activity.setToolTipText("Show Activities");
        activity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	String _string = selectFunItem(swing, "activity");
            	if (_string == null) {
            		JOptionPane.showMessageDialog(null, "No activities available, watch the zone for updates!", "No activities!",  JOptionPane.PLAIN_MESSAGE);
            	} else if (_string.length() > 0) {
            		fantasy.buy(context.user, context.zone, _string);
            		updateInfo();
            		return;
            	}
            }
        });
        
        JMenuItem product = new  JMenuItem("Product");
        product.setMnemonic(KeyEvent.VK_P);
        product.setToolTipText("Show Products");
        product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
              	String _string = selectFunItem(swing, "item");
            	if (_string == null) {
            		JOptionPane.showMessageDialog(null, "No products available, watch the zone for updates!", "No products!",  JOptionPane.PLAIN_MESSAGE);
            	} else if (_string.length() > 0) {
            		fantasy.buy(context.user, context.zone, _string);
            		updateInfo();
            		return;
            	}	
            }
        });

        // Add to zone menu
        zonesMenu.add(purchase);
        purchase.add(activity);
        purchase.add(product);
        
        // Visual decoration
        zonesMenu.addSeparator();

        // Create adding of random item to zone
        JMenuItem addItem = new JMenuItem("Add New Item");
        addItem.setMnemonic(KeyEvent.VK_I);
        addItem.setToolTipText("Add Fun Zone Item");
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	Random random = new Random();
            	String itemString = "Item" + random.nextInt(99);
            	FunThingInterface item = fantasy.createFunThing("item",itemString, itemString, 5);
        		context.zone.addThing(item, 1);
        		JOptionPane.showMessageDialog(null, "Added zone item " + itemString, "Add Item ", JOptionPane.PLAIN_MESSAGE);
        		updateInfo();
            }
        });

        // Add to zone menu
        zonesMenu.add(addItem);

        // Create adding of random activity to zone
        JMenuItem addActivity = new JMenuItem("Add New Activity");
        addActivity.setMnemonic(KeyEvent.VK_A);
        addActivity.setToolTipText("Add Fun Zone Activity");
        addActivity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	Random random = new Random();
            	String itemString = "Activity" + random.nextInt(99);
            	FunThingInterface activity = fantasy.createFunThing("activity",itemString, itemString, 5);
        		context.zone.addThing(activity, 1);
        		JOptionPane.showMessageDialog(null, "Added zone item " + itemString, "Add Item ", JOptionPane.PLAIN_MESSAGE);
        		updateInfo();
            }
        });
        
        // Add to zone menu
        zonesMenu.add(addActivity);

        // Add zone menu
        menubar.add(zonesMenu);

        // Create cart menu
        JMenu cart = new JMenu("Cart");
        cart.setMnemonic(KeyEvent.VK_C);
        
        JMenuItem list = new JMenuItem(new MenuItemCartListAction("Cart Contents"));
        list.setMnemonic(KeyEvent.VK_A);
        list.setToolTipText("Lists Cart Items");
        cart.add(list);
        
        JMenuItem Remove_Item = new JMenuItem(new MenuItemCartDeleteAction("Remove Item"));
        Remove_Item.setMnemonic(KeyEvent.VK_R);
        Remove_Item.setToolTipText("Remove Item From Cart");
        cart.add(Remove_Item);
        
        JMenuItem pay = new JMenuItem(new MenuItemCartPayAction("Pay"));
        pay.setMnemonic(KeyEvent.VK_P);
        pay.setToolTipText("Pay Cart Total");
        cart.add(pay);
        
        JMenuItem checkout = new JMenuItem(new MenuItemCartCheckoutAction("Checkout Cart"));
        checkout.setMnemonic(KeyEvent.VK_C);
        checkout.setToolTipText("Checkout Cart");
        cart.add(checkout);
        
        JMenu refundMenu = new JMenu("Refund");
        refundMenu.setMnemonic(KeyEvent.VK_E);
        refundMenu.setToolTipText("Refund Menu");
        

        JMenuItem refundBalance = new JMenuItem(new MenuItemCartRefundBalanceAction("Refund Cart Balance"));
        refundBalance.setMnemonic(KeyEvent.VK_B);
        refundBalance.setToolTipText("Refund remaining balance in Cart");
        refundMenu.add(refundBalance);

        JMenuItem refundCredit = new JMenuItem(new MenuItemCartRefundCreditAction("Refund Cart Credit"));
        refundCredit.setMnemonic(KeyEvent.VK_F);
        refundCredit.setToolTipText("Refund all credit in Cart");
        refundMenu.add(refundCredit);

        cart.add(refundMenu);
        
        // Add cart menu
        menubar.add(cart);

        // Test menu
       	aboutMenu = new JMenu("Help");
       	aboutMenu.setMnemonic(KeyEvent.VK_H);
       	
       	JMenuItem help = new JMenuItem("Show Help");
       	help.setMnemonic(KeyEvent.VK_H);
       	help.setToolTipText("Display help");
       	help.addActionListener(new ActionListener() {
       		@Override
       		public void actionPerformed(ActionEvent event) {
           		showHelp();
       		}
       	});
       	
       	//Add to menu
       	aboutMenu.add(help);

       	JMenuItem about = new JMenuItem("About");
       	about.setMnemonic(KeyEvent.VK_A);
       	about.setToolTipText("About FantasyLand");
       	about.addActionListener(new ActionListener() {
       		@Override
       		public void actionPerformed(ActionEvent event) {
       			String txt = "FantasyZone GUI driving a FantasyCore single instance";
       			String title = "About";
       			// Internationalisation example
       			if (messages != null) {
       				String tmp_title;
       				String tmp_txt;
       				try { 
       					tmp_title = messages.getString("about");
       					tmp_txt = messages.getString("about_text");
       					title = tmp_title;
       					txt = tmp_txt;
       				}
       				catch (MissingResourceException e){
       					error("Missing resource " + e.getKey());
       				}
       			}
           		JOptionPane.showMessageDialog(null, txt, title, JOptionPane.PLAIN_MESSAGE);
       		}
       	});
       	
       	//Add to menu
       	aboutMenu.add(about);

        addTests = new JMenuItem("Add Tests");
       	addTests.setMnemonic(KeyEvent.VK_T);
       	addTests.setToolTipText("Add Tests Menu");
       	addTests.addActionListener(new ActionListener() {
       		@Override
       		public void actionPerformed(ActionEvent event) {
       			AddTests();
           		JOptionPane.showMessageDialog(null, "Test menus added", "Tests Added", JOptionPane.PLAIN_MESSAGE);
       		}
       	});

        // Visual decoration to isolate tests section
       	if (enable_tests){
           	aboutMenu.addSeparator();
       	}

       	// Set visibility depending on whether to enable tests
        addTests.setVisible(enable_tests);

        // Add to help menu
       	aboutMenu.add(addTests);

       	// Add to menubar
       	menubar.add(aboutMenu);
        
        debug("menubar created");
    }

    private void InitFantasy() {

    	// Requires at least one User
    	if (fantasy.getUserCount() == 0) {
    		fantasy.registerUser("DummyUser");
    	}
    	
		// Login the default user
        UserInterface default_user = fantasy.getUser(0);
        if (default_user != null){
        	setUser(default_user.getName());
        }
        
		// Get default zone 
		FunZoneInterface zone = fantasy.getZone(0);
		if (zone!= null) {
			setZone(zone.getName());
		} else {
			error("NO DEFAULT ZONE!!");
		}
    }

    public boolean setLocale(String language, String country) {
    	if (Runtime.internationalisation() && (language != null) && (country != null)) {
    		Locale currentLocale = new Locale(language, country);
    		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
    		return true;
    	}
    	return false;
    }
    
    // Fantasyswing constructor
	public FantasySwing() {
		
		// initialise the debugger
		setName("FantasySwing");
		setDebug(Runtime.isDevelopment());

		// get the FantasyCore object passing it whether we are debugging and a strategy string selector
		fantasy = FantasyCore.getFantasy(isDebugging(), Runtime.getUsersStrategyString());
		
		// The default action of <RETURN> is to confirm the default action from the buttins in the window.
		// This is often not what is wanted when tabbing between buttons and being surprised that <RETURN>
		// Selects the first button's action. Swing expects SPACE to select the currently focussed button.
		// The line below moves the default action to the currently focussed button so that <RETURN>
		// now has the more usual result.
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
		
		debug("initialising fantasy");

		// Initialise users
		InitFantasy();

		// Set the current locale
		setLocale(Runtime.getLanguage(),Runtime.getcountry());
		
		debug("initialising menubar");
		
		// Create menu
		createMenuBar();

		debug("creating panel");
		
        // Create panel with a grid layout
		panel = new JPanel(new GridLayout());
		
		debug("creating scroller");
		
		// Get a scroller view of the info object
		scroller = new JScrollPane(info);

		debug("setting null border");
		
		// Borders are unattractive
		scroller.setBorder(null);

		debug("adding scroller");
		
		// Put the scroller in the panel
		panel.add(scroller);
 
		debug("set info opaque");
		
		// Show info object
		info.setOpaque(true);

		debug("init done");
		        		        
	}
}
