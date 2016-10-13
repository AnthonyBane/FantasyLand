package FantasyPackage;

import java.util.Iterator;

public interface CartInterface extends DebuggerInterface {

		// Add single fun thing qualified by vendor id
		boolean addItem(FunThingInterface thing, String vendorId);

		// Fun thing remove by name - NOTE: removes first matching name only.
		boolean removeItem(String name);

		// Return count of cart entries (may contain multiple things per entry)
		int getCartEntryCount();
		
		// Get cart entry by index
		CartEntry getCartEntry(int index);
		
		// Get cart entry by name
		CartEntry getCartEntry(String name);

		// Return iterator for fun things so
		// external objects can use an iterator pattern
		// on the entries thus encapsulating the implementation
		Iterator<CartEntry> getCartEntryIterator();

		// Return total cost of fun things in cart
		double getTotal();
		
		// Reading of type double in Java is not guaranteed to be atomic
		double getCredit();
		
		// Refund credit - limited to amount of credit available
		double refundCredit(double amount);

		// Refund all credit
		double refundCredit();

		// Add credit to pay with
		double pay(double money);

		// Checkout paying for all added entries if sufficient credit is available
		boolean checkout();

}
