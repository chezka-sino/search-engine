import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class writes the word index of the files in a directory in a "pretty"
 * JSON format
 * 
 * @author Chezka Sino
 *
 */
public class JSONWriter {

	private static final char TAB = '\t';
	private static final char END = '\n';

	/**
	 * Formats String text to "text"
	 * 
	 * @param text
	 *            String to be formatted
	 * 
	 * @return String in "text" format
	 */
	public static String quote(String text) {
		return String.format("\"%s\"", text);
	}

	/**
	 * 
	 * @param n
	 *            the number of \t
	 * @return String of the number of tabs
	 */
	public static String tab(int n) {
		char[] tabs = new char[n];
		Arrays.fill(tabs, TAB);
		return String.valueOf(tabs);
	}

	/**
	 * Formats the String with the number of tabs
	 * 
	 * @param s
	 *            String to be formatted
	 * @param n
	 *            number of tabs
	 * @return String in \t"txt" format
	 * 
	 */
	public static String writeStrings(String s, int n) {

		String tabStr = tab(n) + quote(s);
		return tabStr;

	}

	/**
	 * Formats the integer with the number of tabs
	 * 
	 * @param i
	 *            integer to be formatted
	 * @param n
	 *            number of tabs
	 * @return String in \t integer format
	 * 
	 */
	public static String writeIntegers(Integer i, int n) {

		String tabInt = tab(n) + i.toString();
		return tabInt;

	}

	/**
	 * Writes the wordindex into a JSON file
	 * 
	 * @throws IOException
	 * 
	 */
	public static void writeJSON(Path outputFile, TreeMap<String, TreeMap<String, TreeSet<Integer>>> fileMap)
			throws IOException {

		try (BufferedWriter writer = Files.newBufferedWriter(outputFile, Charset.forName("UTF-8"))) {

			Set<String> words = fileMap.keySet();
			Iterator<String> itr = words.iterator();
			writer.write("{" + END);

			while (itr.hasNext()) {

				String current = itr.next();
				writer.write(writeStrings(current, 1) + ": {" + END);

				Set<String> fileName = fileMap.get(current).keySet();
				Iterator<String> itr2 = fileName.iterator();

				while (itr2.hasNext()) {

					String current2 = itr2.next();
					writer.write(writeStrings(current2, 2) + ": [" + END);

					Set<Integer> positions = fileMap.get(current).get(current2);
					Iterator<Integer> itr3 = positions.iterator();

					while (itr3.hasNext()) {

						Integer current3 = itr3.next();
						writer.write(writeIntegers(current3, 3));

						if (itr3.hasNext()) {
							writer.write(",");
						}

						writer.write(END);

					}

					writer.write(tab(2) + "]");

					if (itr2.hasNext()) {
						writer.write(",");

					}

					writer.write(END);

				}

				writer.write(TAB + "}");

				if (itr.hasNext()) {
					writer.write(",");
				}

				writer.write(END);

			}

			writer.write("}" + END);

		}

	}
	
	public static void toJSON (Path outputFile, TreeMap<String, List<SearchResult>> results) throws IOException {
		
		try (BufferedWriter writer = Files.newBufferedWriter(outputFile, Charset.forName("UTF-8"))) {
			
			Set<String> words = results.keySet();
			Iterator<String> itr = words.iterator();
			writer.write("{" + END);
			
			while (itr.hasNext()) {
				
				String current = itr.next();
				writer.write(writeStrings(current, 1) + ": [" + END);
				
				List<SearchResult> search = results.get(current);
				Iterator<SearchResult> itr2 = search.iterator();
				
				while (itr2.hasNext()) {
					
					SearchResult current2 = itr2.next();
					writer.write(tab(2) + "{" + END);
					writer.write(writeStrings("where", 3) + ": " + writeStrings(current2.getLocation(), 0) + "," + END);
					writer.write(writeStrings("count", 3) + ": " + current2.getFrequency() + "," + END);
					writer.write(writeStrings("index", 3) + ": " + current2.getPosition() + END);
					writer.write(tab(2) + "}");
					
					if (itr2.hasNext()) {
						writer.write(",");
					}
					
					writer.write(END);
					
				}
				
				writer.write(TAB + "]");
				
				if (itr.hasNext()) {
					writer.write(",");	
				}
				
				writer.write(END);
				
			}
			
			writer.write("}" + END);
		
		}
		
	}

}
