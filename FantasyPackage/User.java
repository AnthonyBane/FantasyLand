package FantasyPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class User extends Debugger implements UserInterface, Observer {
	// User identifier
	private String name;
	// Return identifier
	public String getName() {
		return name;
	}
	// name setter
	public void setName(String n) {
		super.setName(n);
		name = n;
	}
	// Cart belonging to user
	private CartInterface cart = new Cart();
	// Notification storage implementation
	private ArrayList<String> notifications = new ArrayList<String>();
	// Debugger instance
//	private DebuggerInterface debugger = new Debugger("User", true);
	// Return cart
	public CartInterface getCart() { return cart; }
	// Return notification count
	public int getNotificationCount() { return notifications.size(); }
	// Return iterator for notifications
	public Iterator<String> getNotificationIterator() { return notifications.iterator(); }
	// Remove notifications so far
	public void clearNotifications() {
		this.notifications.clear(); 
		debug("Notifications cleared");
	}
	// Notification from observed FunZone that a FunThing has been added 
	@Override
	public void update(Observable _observable, Object _object) {
		FunZone zone = (FunZone)_observable;						// FIXME May have to guard against error with a try/catch statement
		FunThing thing = (FunThing)_object;						// FIXME May have to guard against error with a try/catch statement
		notifications.add(zone.getName() + ": " + thing.getName());
		debug("Updated by " + zone.getName() + " fun thing " + thing.getName());
	}
	
	// User constructor initilised by name
	public User(String _user) {
		setName(_user);
		setType(getName());
	}
	
	// debugging setter
	@Override
	public void setDebug(boolean onOff){
		// Set the debugger as requested
		super.setDebug(onOff);
		// Chain to the user's cart depending on
		// the result of setting the debugger
		cart.setDebug(isDebugging());
	}
}
