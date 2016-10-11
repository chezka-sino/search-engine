import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 
 * @author shinheera
 *
 */
public class QueryParser {

	/**
	 * 
	 * @param inputFile
	 * @param searchType
	 * @param index
	 */
	public static void parseFile(Path inputFile, String searchType, InvertedIndex index) {

		Map<String, HashMap<String, TreeSet<Integer>>> searchResults = new TreeMap<>();

		try (BufferedReader reader = Files.newBufferedReader(inputFile)) {

			String line;

			while ((line = reader.readLine()) != null) {

				line = line.toLowerCase();
				line = line.replace("\\p{Punct}+", "");

				if (!line.isEmpty()) {

					searchResults.put(line, null);
					
					if (searchType.equals("partial")) {
						searchResults.put(line, partial(index, line));
					}
					
					else if (searchType.equals("exact")) {
						searchResults.put(line,exact(index, line));
					}
										
				}

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + inputFile.toString());
		}
	}

	// TODO Query file, storing and writing of results
	
	/**
	 * 
	 * @param index
	 * @param queryWord
	 * @return
	 */
	public static HashMap<String, TreeSet<Integer>> partial(InvertedIndex index, String queryWord) {
		
		String[] words = queryWord.toLowerCase().split("\\s+");
		HashMap<String, TreeSet<Integer>> searchResults = new HashMap<>(); 
		searchResults = (HashMap<String, TreeSet<Integer>>) index.partialSearch(words);

		return searchResults;
	}
	
	public static HashMap<String, TreeSet<Integer>> exact(InvertedIndex index, String queryWord) {
			
		String[] words = queryWord.toLowerCase().split("\\s+");
		HashMap<String, TreeSet<Integer>> searchResults = new HashMap<>();
		searchResults = (HashMap<String, TreeSet<Integer>>) index.exactSearch(words);
		return searchResults;
	
	}
	
//	public static void sortResults ()
	
}
