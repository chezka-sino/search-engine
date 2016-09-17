import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Driver { 
	
	private static Map<String, String> argMap;
	
	public static boolean isFlag(String arg) {
		if (arg.startsWith("-")) {
			return true;
		}
		return false;
	}
	
	public void parseArguments(String[] args) {
		for (String i: args) {
			if (isFlag(i)) {
				if(!argMap.containsKey(i)) {
					
				}
			}
		}
	}
	
    public static void main(String[] args) {
        // TODO Modify as necessary!
//        System.out.println(Arrays.toString(args))

    	Map <String,String> argumentMap = new HashMap<>();
    	
    	for (int i = 0; i<4; i++) {
        	if (isFlag(args[i])) {
        		
        	}
        	
        }

        		// TODO if dir then the next one is the directory to be traversed
        		// no dir = error
        		// if index the it's the json file name.
        		// no filename then default index-simple.json
        		// no -index, still execute but no output file
        		
//        	}
//        }
        
        IndexTest test = new IndexTest();
    }
}
