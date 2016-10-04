import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private static String checkJSONPath(ArgumentParser argP) {
		
		if (argP.getValue("-index") == null && argP.hasFlag("-index")) {
			return "index.json";
		}
		
		return "";

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
		
		ArgumentParser argP = new ArgumentParser(args);
		
//		argP.parseArguments(args);
//		argumentMap = argP.getArgs();
		System.out.println(argP.hasValue("-dir"));
		String dirPath = argP.getValue("-dir");
		String indexPath = argP.getValue("-index");
		
		if (argP.numFlags() == 0) {
			System.err.println("No arguments");
		}
//		!argumentMap.containsKey("-index")
		else if (argP.getValue("-dir") == null || !argP.hasFlag("-index"))
		{
			System.err.println("Invalid directory. Check directory and index input");
		}
		
		else {
			if (!checkJSONPath(argP).equals("")) {
				indexPath = checkJSONPath(argP);
			}
				
				try {
	
//					DirectoryTraverse dir = new DirectoryTraverse(argumentMap.get("-dir"));
					
//					Path dir = Paths.get(argumentMap.get("-dir"));
//					DirectoryTraverse.traverse(dir);
					
					Path dir = Paths.get(dirPath);
					textFiles.addAll(DirectoryTraverse.traverse(dir));
					
					InvertedIndexBuilder.readArray(textFiles, indexPath);
					
					
//					InvertedIndexBuilder indexing = new InvertedIndexBuilder(textFiles);
//					indexing.readArray(argumentMap.get("-index"));
	
				}
	
				catch (IOException e) {
					System.err.println("IOException caught: " + e.getMessage());
				}
				
		}
	}
}
