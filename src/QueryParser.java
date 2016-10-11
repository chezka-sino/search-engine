import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class QueryParser {

	public static void parseFile(Path inputFile, String searchType, InvertedIndex index) {

		ArrayList<String> queryWords = new ArrayList<>();
		// InvertedIndex toIndex = new InvertedIndex();

		try (BufferedReader reader = Files.newBufferedReader(inputFile)) {

			String line;

			while ((line = reader.readLine()) != null) {

				line = line.toLowerCase();
				line = line.replace("\\p{Punct}+", "");

				if (!line.isEmpty()) {
					queryWords.add(line);
				}

			}

			if (searchType.equals("partial")) {
				partial(index, queryWords);
			}

			// List<SearchResult> searchResults = new Li

			// if (searchType.equals("partial")) {
			// List<String> partialResult =
			// InvertedIndex.partialSearch(queryWords);

			// }

		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + inputFile.toString());
		}
	}

	// TODO Query file, storing and writing of results
	public static void partial(InvertedIndex index, ArrayList<String> queryWords) {

		// InvertedIndex.partialSearch(queryWords);
		// Map<String, Map<String, Map<Integer, Integer>>> search = new
		// HashMap();
		List<String> searchResults = index.partialSearch(queryWords);

		// search = index.partialSearch(queryWords);
	}
}
