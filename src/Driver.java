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
	
	private static String checkResultsPath(ArgumentParser argP) {
		
		if (argP.getValue("-results") == null && argP.hasFlag("-results")) {
			return "results.json";
		}
		
		return "";
	}

	/**
	 * Main method Initializes the program
	 * 
	 * @param argss
	 *            argument array
	 */
	public static void main(String[] args) {

		ArrayList<String> textFiles = new ArrayList<>();

		ArgumentParser argP = new ArgumentParser(args);

		String dirPath = argP.getValue("-dir");
		String indexPath = argP.getValue("-index");
		String resultsPath = argP.getValue("-results");
		String queryPath = argP.getValue("-query");
		String exactPath = argP.getValue("-exact");
		
//		System.out.println("-dir: " + dirPath + " " + argP.hasFlag("-dir"));
//		System.out.println("-index: " + indexPath + " " + argP.hasFlag("-index"));
//		System.out.println("-results: " + resultsPath + " " + argP.hasFlag("-results"));
//		System.out.println("-query: " + queryPath + " " + argP.hasFlag("-query"));
//		System.out.println("-exact: " + exactPath + " " + argP.hasFlag("-exact"));

		if (argP.numFlags() == 0) {
			System.err.println("No arguments");
		}

		if (argP.getValue("-dir") == null) {
			System.out.println(argP.getValue("-dir"));
			System.out.println(argP.hasFlag("-index"));
			System.err.println("Invalid directory. Check directory input");
		}
		
		else {
			
//			if (!checkJSONPath(argP).equals("")) {
//				indexPath = checkJSONPath(argP);
//			}
			
			

			try {

				Path dir = Paths.get(dirPath);
				textFiles.addAll(DirectoryTraverser.traverse(dir));

				InvertedIndex index = new InvertedIndex();
				InvertedIndexBuilder.readArray(textFiles, index);
				
//				if (indexPath == null || indexPath.equals("")) {
//					indexPath = checkJSONPath(argP);
//					
//				}
//				index.toJSON(indexPath);
				
//				System.out.println("Query path: " + queryPath);
//				System.out.println("Exact path: " + exactPath);
				
				if (resultsPath == null || resultsPath.equals("")) {
					resultsPath = checkResultsPath(argP);
					System.out.println("New results path: " + resultsPath);
				}
				
				if (!(queryPath == null)) {	
//					System.out.println("parseFile partial");
					QueryParser.parseFilePartial(Paths.get(queryPath), index);
				}
				
				if (!(exactPath == null)) {
					QueryParser.parseFileExact(Paths.get(exactPath), index);
					System.out.println("CALLING WRITEJSONSEARCH");
					index.searchToJSON(resultsPath);
				}
				
				if (indexPath == null || indexPath.equals("")) {
					indexPath = checkJSONPath(argP);
				}
				
				index.toJSON(indexPath);
				
//				if (resultsPath == null || resultsPath.equals("")) {
//					resultsPath = checkResultsPath(argP);
//					System.out.println("New results path: " + resultsPath);
//				}
//				
				
				

			}

			catch (IOException e) {
				System.err.println("Error in directory: " + dirPath);
//				e.printStackTrace();
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
