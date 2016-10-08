import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class demonstrates traversing through a directory, looking for .txt
 * files, organizing words into an array and writing them in a JSON File
 * 
 * @author Chezka Sino
 * 
 */

public class Driver {

	/**
	 * Checks where the value of the -index flag is empty then assigns the
	 * default JSON file to be used
	 */
	private static String checkJSONPath(ArgumentParser argP) {

		if (argP.getValue("-index") == null && argP.hasFlag("-index")) {
			return "index.json";
		}

		return "";

	}

	/**
	 * Main method Initializes the program
	 * 
	 * @param args
	 *            argument array
	 */
	public static void main(String[] args) {

		ArrayList<String> textFiles = new ArrayList<>();

		ArgumentParser argP = new ArgumentParser(args);

		String dirPath = argP.getValue("-dir");
		String indexPath = argP.getValue("-index");

		if (argP.numFlags() == 0) {
			System.err.println("No arguments");
		}

		else if (argP.getValue("-dir") == null || !argP.hasFlag("-index")) {
			System.err.println("Invalid directory. Check directory and index input");
		}

		else {

			if (!checkJSONPath(argP).equals("")) {
				indexPath = checkJSONPath(argP);
			}

			try {

				Path dir = Paths.get(dirPath);
				textFiles.addAll(DirectoryTraverser.traverse(dir));

				InvertedIndex index = new InvertedIndex();
				InvertedIndexBuilder.readArray(textFiles, index);
				index.toJSON(indexPath);

			}

			catch (IOException e) {
				System.err.println("Error in directory: " + dirPath);
			}

		}

		//
		// if (-dir) {
		// build
		// }
		//
		// if (-index) {
		// index.toJSON(index);
		// }

		/*
		 * Project 2
		 * 
		 * Create a query parser class to handle the query file and storing and
		 * writing of results.
		 * 
		 * Create a single search result class that implements Comparable
		 * 
		 * Create some search methods in inverted index
		 * 
		 */

	}

}
