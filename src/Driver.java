import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO Configure Eclipse to re-indent on save 
// TODO Fill in your missing Javadoc comments
// TODO Remove old TODO comments

/** 
 * This class demonstrates traversing through a directory, looking for .txt files,
 * organizing words into an array and writing them in a JSON File
 * 
 * @author Chezka Sino
 * 
 */

public class Driver { 

	/**
	 * Checks where the value of the -index flag is empty then assigns the default 
	 * JSON file to be used
	 */
	private static void checkJSONPath(Map<String, String> argumentMap) {

		if (argumentMap.get("-index") == null && argumentMap.containsKey("-index")) {
			argumentMap.replace("-index", argumentMap.get("-index"), "index.json");
		}

	}	
	
	/**
	 * 
	 * @param argumentMap
	 * @return
	 */
	private static boolean checkDir(Map<String, String> argumentMap) {
		
		if (argumentMap.get("-dir") == null || !argumentMap.containsKey("-dir")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Main method
	 * 		Initializes the program
	 * 
	 * @param args
	 * 		argument array
	 */
	public static void main(String[] args) {

		ArrayList <String> textFiles = new ArrayList<>();
		Map <String, String> argumentMap = new HashMap<>();
		
		ArgumentParser argP = new ArgumentParser();
		
		argP.parseArguments(args);
		argumentMap = argP.getArgs();
		
		if (args.length == 0 || argumentMap.isEmpty()) {
			System.err.println("No arguments");
		}
		
		else if (!checkDir(argumentMap) || !argumentMap.containsKey("-index")) {
			System.err.println("Invalid directory. Check directory and index input");
		}
		
		else {
			checkJSONPath(argumentMap);
				
				try {
	
					DirectoryTraverse dir = new DirectoryTraverse(argumentMap.get("-dir"));
					textFiles.addAll(DirectoryTraverse.traverse(dir.getDir()));
					
					InvertedIndexBuilder indexing = new InvertedIndexBuilder(textFiles);
					indexing.readArray(argumentMap.get("-index"));
	
				}
	
				catch (IOException e) {
					System.err.println("IOException caught: " + e.getMessage());
				}
				
		}
	}
}
