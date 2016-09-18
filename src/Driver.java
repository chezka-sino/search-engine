import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver { 
	
	private static Map<String, String> argumentMap;
	private static ArrayList<String> textFiles;
	
	public static boolean isFlag(String arg) {
		if (arg.startsWith("-")) {
			return true;
		}
		return false;
	}
	
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
	
	private static void checkJSONPath() {

		if (argumentMap.get("-index") == null && argumentMap.containsKey("-index")) {
			argumentMap.put("-index", "index.json");
		}

	}	
	
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
