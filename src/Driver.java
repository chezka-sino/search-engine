import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** 
 * This class demonstrates traversing through a directory, looking for .txt files,
 * organizing words into an array and writing them in a JSON File
 * 
 * @author Chezka Sino
 */

public class Driver { 
	
	/**  
	 * 
	 */
	private static Map<String, String> argumentMap;
	private static ArrayList<String> textFiles;
	
	/**
	 * Checks if the string in the argument array is a flag
	 * 
	 * @param arg
	 * 			string to be checked
	 * @return boolean value
	 */
	public static boolean isFlag(String arg) {
		if (arg.startsWith("-")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Creates the mapping of the flag arguments to their values
	 * 
	 * @param args
	 * 			array of arguments
	 * 
	 */
	private static void parseArguments(String[] args) {
    	for (int i = 0; i<args.length; i++) {
          	
        	if (isFlag(args[i])) {
        		
        		if(!argumentMap.containsKey(args[i])) {
        			argumentMap.put(args[i], null);
        			
        			if (i+1 == args.length) {
        				return;
        			}
        			
        			if (!isFlag(args[i+1])) {
        				argumentMap.replace(args[i], null, args[i+1]);
        			}
        		}
        	}
    		
        }
    	
	}
	
	/**
	 * Checks where the value of the -index flag is empty then assigns the default 
	 * JSON file to be used
	 */
	private static void checkJSONPath() {

		if (argumentMap.get("-index") == null && argumentMap.containsKey("-index")) {
			argumentMap.put("-index", "index.json");
		}

	}	
	
	/**
	 * Main method
	 * 		Initializes the program
	 * 
	 * @param args
	 * 		argument array
	 */
    public static void main(String[] args) {

    	textFiles = new ArrayList<>();
    	argumentMap = new HashMap<>();
    	
    	parseArguments(args);
    	checkJSONPath();
    	
    	try {
    		
    		DirectoryTraverse dir = new DirectoryTraverse(argumentMap.get("-dir"),
    				argumentMap.get("-index"));
    		dir.traverse(dir.getDir());
    		textFiles.addAll(dir.getFileList());
    		
    		WordDataStructure indexing = new WordDataStructure(argumentMap.get("-index"));
    		indexing.readArray(textFiles);
    		
    	}
    	
    	catch (NullPointerException | IOException e) {
    		System.err.println("System Error");
    	} 
    	  	
    }
}
