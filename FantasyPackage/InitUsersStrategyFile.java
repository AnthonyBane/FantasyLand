package FantasyPackage;

import java.net.URI;
import java.net.URISyntaxException;

// Strategy pattern implementation 
public class InitUsersStrategyFile extends InitUsersStrategyUrl {
	// Init strategy based on passing a local filename
	InitUsersStrategyFile(String filename) {
		try {
			// Create Url using file:// scheme
			initUrl(new URI("file://"+filename));
		} catch (URISyntaxException e) {
			// It may fail. We catch the exception 
			// e.printStackTrace();
		}
	}
}
