import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class QueryParser {

	public static void parseFile(Path inputFile, String searchType, InvertedIndex index) {

		Map<String, List<String>> searchResults = new TreeMap<>();

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
	public static List<String> partial(InvertedIndex index, String queryWord) {
		
		String[] words = queryWord.toLowerCase().split("\\s+");
		List<String> searchResults = index.partialSearch(words);

		return searchResults;
	}
	
	public static List<String> exact(InvertedIndex index, String queryWord) {
			
		String[] words = queryWord.toLowerCase().split("\\s+");
		List<String> searchResults = index.exactSearch(words);
		return searchResults;
	
	}
	
}
