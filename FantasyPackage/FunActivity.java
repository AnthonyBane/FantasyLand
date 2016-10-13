package FantasyPackage;

// Overrides FunItem to being an activity
// Hierarchy suggests FunActivity is always of type FunItem,
// this may not always be true but was assumed to be so for -
// this implementation
public class FunActivity extends FunItem {
	@Override
	public String getType() {
		return "activity";
	}
	public FunActivity(String name, String desciption, double cost) {
		super(name, desciption, cost);
	}
}
