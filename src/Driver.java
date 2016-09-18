import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Driver { 
	
	private static Map<String, String> argumentMap;
	private static ArrayList<String> textFiles;
	private static Map <String, HashMap <String, ArrayList <Integer>>> allWords;
	
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
		
		System.out.println(argumentMap);
		if (argumentMap.get("-index") == null && argumentMap.containsKey("-index")) {

//			argumentMap.remove("-index",null);
			argumentMap.put("-index", "index.json");
			System.out.println(argumentMap);
		}
	}
	
	// TODO what if no directory
	
	
    public static void main(String[] args) throws IOException {
        // TODO Modify as necessary!
//        System.out.println(Arrays.toString(args))
    	textFiles = new ArrayList<>();
    	argumentMap = new HashMap<>();
    	allWords = new HashMap<>();
    	
    	parseArguments(args);
    	checkJSONPath();
    	
    	try {
    		
    		DirectoryTraverse dir = new DirectoryTraverse(argumentMap.get("-dir"));
    		dir.traverse(dir.getDir());
    		textFiles.addAll(dir.getFileList());
    		
    	}
    	
    	catch (NullPointerException | NoSuchFileException e) {
    		System.err.println("ERROR: Directory error");
    	}
    	
    	for (String item:textFiles) {
//    		System.out.println(item);
    		WordDataStructure indexing = new WordDataStructure(item);
    		indexing.openFile();
    	}
    	
    }
}
