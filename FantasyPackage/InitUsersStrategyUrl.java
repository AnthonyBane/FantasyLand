package FantasyPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Strategy pattern implementation of a URL based fetching of a string to use
// to initialise users
public class InitUsersStrategyUrl extends InitUsersStrategyString {
	protected void initUrl(URI uri) {
		// Get the scheme from the URI
		String scheme = uri.getScheme();
		// http scheme requires use of an InputStreamReader
		if (scheme.equals("http")) {
			// the list of names that will be retrieved
			List<String> names = new ArrayList<String>();
			URL url = null;
			BufferedReader in = null;
			InputStreamReader input =null;
			// Create a url - it may fail
			try {
				url = new URL(uri.toString());
			} catch (MalformedURLException e) {
				// no action performed but exception caught
				//  e.printStackTrace();
				return;
			}
			// Create an InputStreamReader using the url
			try {
				input = new InputStreamReader(url.openStream());
				// Wrap the reader in a buffered formatter
				in = new BufferedReader(input);
				// Read in data 
				while (in.ready()) {
					// a line at a time
					String name = in.readLine();
					// And add it to the list of strings
					names.add(name);
				}
				// Apply this list to the names in the parent class
				setUserNames(names);
				return;
			} catch (IOException e) {
				// no action performed but exception caught
				// e.printStackTrace();
				return;
			}
		}
		else {
			// Try and open a local file
			try {
				Path path = Paths.get(uri);
				// Assume the file isn't large and read in all the lines
				List<String> names = Files.readAllLines(path);
				// Apply this list to the names in the parent class
				setUserNames(names);
			}
			catch (IOException e) {
				// no action performed but exception caught
				// e.printStackTrace();
			}
		}
	}
	public InitUsersStrategyUrl(URI uri) {
		initUrl(uri);
	}
	public InitUsersStrategyUrl() {
	}
}
