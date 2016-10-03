import java.util.HashMap;
import java.util.Map;

// TODO Make this closer to the homework so you aren't breaking encapsulation

public class ArgumentParser {
	
	private final Map<String, String> argumentMap;

	
	public ArgumentParser() {
		argumentMap = new HashMap<>();
	}
	
	/**
	 * Creates the mapping of the flag arguments to their values
	 * 
	 * @param args
	 * 			array of arguments
	 * 
	 */
	public void parseArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {
	
			if (isFlag(args[i])) {
	
				if(!argumentMap.containsKey(args[i])) {
					argumentMap.put(args[i], null);
	
					if (i + 1 == args.length) {
						return;
					}
	
					if (!isFlag(args[i + 1])) {
						argumentMap.replace(args[i], null, args[i + 1]);
					}
				}
			}
	
		}

	}
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
	
	// TODO This breaks encapsulation, which is key for object-oriented programming
	public Map<String, String> getArgs() {
		return argumentMap;
	}

}
