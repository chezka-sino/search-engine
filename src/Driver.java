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

// TODO Rename argP to "parser"

public class Driver {

	/**
	 * Checks where the value of the -index flag is empty then assigns the
	 * default JSON file to be used
	 * 
	 * @param argP
	 * 		ArgumentParser object
	 * @return
	 * 		The default JSON file
	 *  
	 */
	private static String checkJSONPath(ArgumentParser argP) {

		if (argP.getValue("-index") == null && argP.hasFlag("-index")) {
			return "index.json";
		}

		return "";

	}

	/**
	 * Checks where the value of the -results flag is empty then assigns the
	 * default JSON file to be used
	 * 
	 * @param argP
	 * 		ArgumentParser object
	 * @return
	 * 		The default JSON file
	 *  
	 */
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

		/*
		if (-dir flag)
			get the -dir value
			try {
				immediately build
			}
			catch () {
				informative error message
			}
		}
		
		if (-index)
			String path = argParser.getValue(-index, index.json);
		
		*/
		
		// TODO No need to get if flag doesn't exist
		String dirPath = argP.getValue("-dir");
		String indexPath = argP.getValue("-index");
		String resultsPath = argP.getValue("-results");
		String queryPath = argP.getValue("-query");
		String exactPath = argP.getValue("-exact");

		if (argP.numFlags() == 0) {
			System.err.println("No arguments");
		}

		// TODO Do not need this check at all
		if (argP.getValue("-dir") == null || !argP.hasFlag("-dir")) {
			System.err.println("Invalid directory. Check directory input");
		}
		else {

			try {

				Path dir = Paths.get(dirPath);
				textFiles.addAll(DirectoryTraverser.traverse(dir));

				InvertedIndex index = new InvertedIndex();
				InvertedIndexBuilder.readArray(textFiles, index);

				// Checks if the output JSON file for the index exists then sets
				// it to the default output file path
				if (!checkJSONPath(argP).equals("")) {
					indexPath = checkJSONPath(argP);
				}

				// Checks if the output JSON file for the search results exists
				// then sets it to the default output file path
				if (resultsPath == null || resultsPath.equals("")) {
					resultsPath = checkResultsPath(argP);
				}

				if (!(queryPath == null)) {
					QueryParser.parseFilePartial(Paths.get(queryPath), index);
					index.searchToJSON(resultsPath);
				}

				if (!(exactPath == null)) {
					QueryParser.parseFileExact(Paths.get(exactPath), index);
					index.searchToJSON(resultsPath);
				}

				if (indexPath == null || indexPath.equals("")) {
					indexPath = checkJSONPath(argP);
				}

				index.toJSON(indexPath);

			}

			catch (IOException e) {
				// TODO Not that informative
				System.err.println("Error in directory: " + dirPath);

			}

		}

	}

}
