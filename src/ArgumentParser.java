import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

	private final Map<String, String> argumentMap;

	public ArgumentParser() {
		argumentMap = new HashMap<>();
	}

	/**
	 * Creates a new argument parser and immediately parses the passed
	 * arguments.
	 * 
	 * @param args
	 *            arguments to parse into flag, value pairs
	 * @see #parseArguments(String[])
	 */
	public ArgumentParser(String[] args) {
		this();
		parseArguments(args);
	}

	/**
	 * Creates the mapping of the flag arguments to their values
	 * 
	 * @param args
	 *            array of arguments
	 * 
	 */
	public void parseArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {

			if (isFlag(args[i])) {

				if (!argumentMap.containsKey(args[i])) { // TODO Could remove this block
					argumentMap.put(args[i], null);

					if (i + 1 == args.length) {
						return;
					}

					if (!isFlag(args[i + 1])) {
						// TODO Could jsut do put again to replace or update
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
	 *            string to be checked
	 * @return boolean value
	 */
	public static boolean isFlag(String arg) {
		arg = arg.trim();

		if (arg.startsWith("-")) {
			return true;
		}
		return false;
	}

	/**
	 * Tests whether the argument is a valid value, i.e. it is not null, does
	 * not start with a dash "-" character, and is not empty after trimming.
	 * 
	 * @param arg
	 *            argument to test
	 * @return {@code true} if the argument is a valid value
	 */
	public static boolean isValue(String arg) {
		arg = arg.trim();

		if (!(arg == null) && !arg.startsWith("-") && !(arg.length() == 0)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the number of flags stored.
	 * 
	 * @return number of flags
	 */
	public int numFlags() {
		return argumentMap.size();
	}

	/**
	 * Checks if the flag exists.
	 * 
	 * @param flag
	 *            flag to check for
	 * @return {@code true} if flag exists
	 */
	public boolean hasFlag(String flag) {
		return argumentMap.containsKey(flag);
	}

	/**
	 * Checks if the flag exists and has a non-null value.
	 * 
	 * @param flag
	 *            flag whose associated value is to be checked
	 * @return {@code true} if the flag exists and has a non-null value
	 */
	public boolean hasValue(String flag) {
		// return argumentMap.get(flag) != null;
		if (argumentMap.get(flag) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the value associated with the specified flag. May be {@code null}
	 * if a {@code null} value was stored or if the flag does not exist.
	 * 
	 * @param flag
	 *            flag whose associated value is to be returned
	 * @return value associated with the flag or {@code null} if the flag does
	 *         not exist
	 */
	public String getValue(String flag) {
		return argumentMap.get(flag);
	}

	/**
	 * Returns the value for a flag. If the flag is missing or the value is
	 * {@code null}, returns the default value instead.
	 * 
	 * @param flag
	 *            flag whose associated value is to be returned
	 * @param defaultValue
	 *            the default mapping of the flag
	 * @return value of flag or {@code defaultValue} if the flag is missing or
	 *         the value is {@code null}
	 */
	public String getValue(String flag, String defaultValue) {
		if (getValue(flag) == null) {
			return defaultValue;
		}
		return getValue(flag);
	}

	/**
	 * Returns the value for a flag as an integer. If the flag or value is
	 * missing or if the value cannot be converted to an integer, returns the
	 * default value instead.
	 * 
	 * @param flag
	 *            flag whose associated value is to be returned
	 * @param defaultValue
	 *            the default mapping of the flag
	 * @return value of flag as an integer or {@code defaultValue} if the value
	 *         cannot be returned as an integer
	 */
	public int getValue(String flag, int defaultValue) {
		try {
			int val = Integer.parseInt(argumentMap.get(flag));
			return val;
		}

		catch (NumberFormatException e) {
			return defaultValue;
		}

	}

	@Override
	public String toString() {
		// You do not need to modify this method.
		return argumentMap.toString();
	}

}
