import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Driver { 
	
	private static Map<String, String> argumentMap;
	private static final String DEFAULT_INDEX = "index.json";
	private static String directory;
	
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
		if (argumentMap.get("-index") == null) {
			argumentMap.put("-index", DEFAULT_INDEX);
		}
	}
	
	// TODO what if no directory
	
	
    public static void main(String[] args) throws IOException {
        // TODO Modify as necessary!
//        System.out.println(Arrays.toString(args))
    	
    	

    	argumentMap = new HashMap<>();
    	parseArguments(args);
    	checkJSONPath();
    	
//    	if (!argumentMap.containsKey("-dir")) {
//    		System.err.println("No directory provided");
//    	}
//    	
//    	else {
//    		DirectoryTraverse dir = new DirectoryTraverse(argumentMap.get("-dir"));
//    	}
    	
    	try {
//    		File file = new File(argumentMap.get("-dir"));
    		
    		DirectoryTraverse dir = new DirectoryTraverse(argumentMap.get("-dir"));
    		dir.traverse(dir.getDir());
    	}
    	
    	catch (NullPointerException | NoSuchFileException e) {
    		System.err.println("Directory error");
    	}
    	
//    	ArgumentParser parse = new ArgumentParser(args);
//    	parse.parseArguments(args);
//    	parse.checkJSONPath();
//    	
//    	if (!parse.hasDirectory()) {
//    		System.err.println("No directory path was found");
//    		System.exit(0);
//    	}
    	
//    	DirectoryTraverse dir = new DirectoryTraverse(parse.getMap());
//    	dir.traverse(dir.getDir());
//    	
//    	ArrayList<String> txt = dir.getFileList();
//    	System.out.println(txt.toString());
//    	parseArguments(args);
//    	checkJSONPath();
    	

//    	System.out.println(argumentMap.get("-index"));
    	
    }
}
