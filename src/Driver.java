import java.util.HashMap;
import java.util.Map;



public class Driver { 
	
	private static Map<String, String> argumentMap;
	private static final String DEFAULT_INDEX = "index.json";
	
	public static boolean isFlag(String arg) {
		if (arg.startsWith("-")) {
			return true;
		}
		return false;
	}
	
	public static void parseArguments(String[] args) {
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
	
	public static void checkJSONPath() {
		if (argumentMap.get("-index") == null) {
			argumentMap.put("-index", DEFAULT_INDEX);
		}
	}
	
    public static void main(String[] args) {
        // TODO Modify as necessary!
//        System.out.println(Arrays.toString(args))

    	argumentMap = new HashMap<>();
    	parseArguments(args);
    	if (argumentMap.containsKey("-index")) {
    		checkJSONPath();
    	}
    	    	
//    	System.out.println(argumentMap.get("-index"));
    	
    }
}
