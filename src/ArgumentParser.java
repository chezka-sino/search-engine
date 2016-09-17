import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
	
	private final Map<String, String> argumentMap;
	
	public ArgumentParser(String[] args) {
		argumentMap = new HashMap<>();	
		parseArguments(args);
	}
	
	public void parseArguments(String[] args) {
		
		for (int i = 0; i<args.length; i++) {
          	
        	if (isFlag(args[i])) {
        		
        		if(!argumentMap.containsKey(args[i])) {
        			argumentMap.put(args[i], null);
        			
        			if (i+1 == args.length) {
        				return;
        			}
        			
        			if (isValue(args[i+1])) {
        				argumentMap.replace(args[i], null, args[i+1]);
        			}
        			
        		}
        	}
    		
        }
		
	}
	
	public static boolean isFlag(String arg) {
		if (arg.startsWith("-")) {
			return true;
		}
		return false;
	}	

	public static boolean isValue(String arg) {
		if (!arg.startsWith("-")) {
			return true;
		}
		return false;
	}
	
	public void checkJSONPath() {
		if (argumentMap.get("-index") == null) {
			argumentMap.replace("-index", "index.json");
		}
	}
}
