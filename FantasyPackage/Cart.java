package FantasyPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Used to store cart entries, report cost, and checkout
 * It is intended but not limited to being owned by a user
 * Thread safety has been added as an illustration showing -
 * synchronisation of methods (leading to inter-thread variable -
 * visibility as well as mutual exclusion of code)
 * Synchronisation statements to offer improved liveliness (application -
 * responsiveness) over method synchronisation
*/
public class Cart extends Debugger implements CartInterface {

	// Retains value of credit paid into the cart
	private double credit = 0;
	
	// Storage of cart entries. Implementation hidden
	private List<CartEntry> entries = new ArrayList<CartEntry>();
	
	/*
	 *  Private as used internally only.
	 *  Removes the first instance of the "thing"
	 *  Returns a boolean value to indicate success/failure of removal
	 */
	private synchronized boolean removeItem(FunThingInterface thing) {
		/*
		 *  Private method should not need to validate its input arguments -
		 *  unless being overly cautious
		*/
		/*
		 * Example of an iterator pattern for iterating over the cart entries -
		 * for the first entry that contains "thing" 
		 */
		for (CartEntry entry:entries) {
			// Check that the entry is not null
			// This should never happen
			if (entry == null) {
				error("Null entry detected");
			}
			else {
				// Test whether this entry contains a thing
				if (entry.thing == thing) {
					// There should never be an entry with no contents
					if (entry.count <= 0) {
						// This could be changed throw an exception as it is a logic error
						error("entry count zero or less");
						return false;
					}
					// Decrement entry count - already checked to be greater than zero
					entry.count--;
					// If the entry is empty
					if (entry.count == 0) {
						// Remove the entry itself
			            entries.remove(entry);
					}
					// Successful removal
					return true;
				}
			}
		}
		// No entry containing "thing" found
		return false;
	}

	// Add single fun thing qualified by vendor id
	// This could be extended to add multiple things rather than single -
	// e.g. selecting 5 pizzas from foodcourt and passed as an argument
	public boolean addItem(FunThingInterface thing, String vendorId) { 
		// Check arguments for expected values
		if (thing == null || vendorId == null) {
			// Indicate failure to calling object
			return false;
		}
		// Create a new cart entry
		CartEntry entry = new CartEntry(thing, vendorId, 1);
		// Synchronised statement using lock owned by this to avoid race condition when adding entries
		synchronized(this) {
			// Iterator pattern used to iterate over entries list
			for (CartEntry e:entries) {
				// If an identical entry exists
				if ((e.thing == thing) && (vendorId == e.vendorName)) {
					// Increment the count
					e.count++;
					// Return success
					// The new entry is garbage collected after it goes out of scope
					return true;
				}
			}
			// No existing entries found so add the new one
			entries.add(entry);
		}
		// Return success
		return true;
	}
	
	// Fun thing remove by name - NOTE: removes first matching name only.
	public boolean removeItem(String name) {
		// Sanity checking arguments as public/protected method
		if (name == null) {
			return false;
		}
		// Protect entry removal by synchronised statement using lock from this object
		synchronized(this) {
			// Iterator pattern to iterate over entries
			for (CartEntry entry:entries) {
				// If entry found
				if (entry.thing.getName() == name) {
					// Try to remove one instance via private method
					return removeItem(entry.thing);
				}
			}
		}
		// Failed to remove item
		return false;
	}

	// Return count of cart entries (may contain multiple things per entry)
	public synchronized int getCartEntryCount() {
		return entries.size();
	}
	
	// Get cart entry by index
	public synchronized CartEntry getCartEntry(int index) {
		// Sanity check input arguments
		if ((index >= 0) && (index < getCartEntryCount())) {
			// Return the entry
			return entries.get(index);
		}
		// Return null as no entry is within the specified index
		return null;
	}
	
	// Get cart entry by name
	public CartEntry getCartEntry(String name) {
		// Check input parameters
		if (name == null) {
			return null;
		}
		// Protect getting an entry race condition 
		synchronized(this) {
			// Iterator pattern to iterate over entries list
			for (CartEntry entry:entries) {
				// If the name matches
				if (entry.thing.getName() == name)
					// Return the entry
					return entry;
			}
		}
		// No entry found
		return null;
	}

	// Return iterator for fun things so
	// external objects can use an iterator pattern
	// on the entries thus encapsulating the implementation
	public Iterator<CartEntry> getCartEntryIterator() {	
		return entries.iterator();
	}

	// Return total cost of fun things
	// A strategy pattern could be implemented to take account of discounts or anything else
	// applicable to the individual account e.g. promotion, quantity, total-cost discount
	// these options could form part of a decorator pattern implementation
	public double getTotal() {
		// Accumulate the total in a variable
		double total = 0;
		// Protect iteration from race condition e.g. another thread adding or removing an entry
		synchronized(this) {
			// Iterator pattern iterating over the entries
			for (CartEntry entry:entries)	{
				// Accumulate total for non-null entries
				if (entry != null) {
					total += entry.thing.getCost() * entry.count;
				}
			}
		}
		return total;
	}
	
	// Reading of type double in Java is not guaranteed to be atomic
	public synchronized double getCredit() { return credit; }
	
	// Refund credit - limited to amount of credit available
	public synchronized double refundCredit(double amount) {
		// Variable to store how much refund is available
		double refund = 0;
		// If we want a refund of less than the amount of credit
		if (amount <= credit) {
			// Grant it
			refund = amount;
		}
		// Otherwise cap the refund amount at the credit available - no overdrafts allowed.
		else {
			refund = credit;
		}
		// Reduce the credit by the refunded amount
		credit -= refund;
		// Return the amount actually refunded
		return refund;
	}

	// Refund all credit
	public synchronized double refundCredit() {
		// Refund all available credit 
		return refundCredit(credit);
	}

	// Add credit to pay with
	public synchronized double pay(double money) {
		// Cannot add negative credit
		if (money > 0){
			credit += money;
			// Return amount credited
			return money;
		}
		// Nothing credited
		return 0;
	}

	// Could write test to demonstrate removing items decreases total which in -
	// turn allows checkout to pass
	// Note that this deletes all fun things
	// This could be extended to flag items as having been paid for if -
	// a returns policy was to be implemented
	public synchronized boolean checkout() {
		// If we have enough credit to pay for the full amount
		if (credit >= getTotal()) {
			// Reduce credit by what we owe
			credit -= getTotal();
			// Create new empty array list
			entries = new ArrayList<CartEntry>();
			// Checkout success
			return true;
		}
		// Checkout failed
		return false;
	}

	public Cart() {
		setName("Cart");
	}
	
}
