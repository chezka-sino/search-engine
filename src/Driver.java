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

		if (argP.numFlags() == 0) {
			System.err.println("No arguments");
		}

		if (argP.getValue("-dir") == null || !argP.hasFlag("-dir")) {
			System.err.println("Invalid directory. Check directory input");
		}
		
		
		
		else {
			
			try {

				Path dir = Paths.get(dirPath);
				textFiles.addAll(DirectoryTraverser.traverse(dir));

				InvertedIndex index = new InvertedIndex();
				InvertedIndexBuilder.readArray(textFiles, index);
				
				if (!checkJSONPath(argP).equals("")) {
					indexPath = checkJSONPath(argP);
				}
//				index.toJSON(indexPath);
				
				if (resultsPath == null || resultsPath.equals("")) {
					resultsPath = checkResultsPath(argP);
					System.out.println("New results path: " + resultsPath);
				}
				
				if (argP.getValue("-results") == null && argP.hasFlag("-results")) {
					resultsPath = "results.json";
					System.out.println("New results path: " + resultsPath);
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
				System.err.println("Error in directory: " + dirPath);

			}	
			
		}	

	}

}
