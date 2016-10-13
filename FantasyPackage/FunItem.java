package FantasyPackage;

// Implements an item and asserts it to be so
public class FunItem extends FunThing {
	// Name of FunItem
	private String name;
	// Its description
	private String description;
	// Its cost
	private double cost;
	// Accessors
	public String getName() { return name; }
	public String getDescription() { return description; }
	public double getCost() { return cost; }
	public String getType() { return "item"; }
	// Constructor
	public FunItem(String _name, String _description, double _cost) {
		name = _name;
		description = _description;
		cost = _cost;
	}
}
