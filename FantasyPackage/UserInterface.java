package FantasyPackage;

import java.util.Iterator;

public interface UserInterface extends DebuggerInterface {
	String getName();
	// Return cart
	CartInterface getCart();
	// Return notification count
	int getNotificationCount();
	// Return iterator for notifications
	Iterator<String> getNotificationIterator();
	// Remove notifications so far
	void clearNotifications();
}
