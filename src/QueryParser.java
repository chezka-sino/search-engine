import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;


/**
 * This class parses the query file that contains all of the query words for searching
 * 
 * @author Chezka Sino
 *
 */
public class QueryParser {
	
	private final InvertedIndex index;
	private final TreeMap<String, List<SearchResult>> results;
	

	/**
	 * QueryParser constructor
	 * 
	 * @param index
	 * 		InvertedIndex object
	 * 
	 */
	public QueryParser(InvertedIndex index) {
		this.index = index;
		this.results = new TreeMap<>();
	}
	
	/**
	 * Goes through the query file line by line for the search words
	 * 
	 * @param file
	 * 		file path containing the query words
	 * @param exact
	 * 		true if conducting an exact search, false otherwise
	 * 
	 */
	public void parseFile(Path file, boolean exact) {
			
		try (BufferedReader reader = Files.newBufferedReader(file)) {

			String line;
	
			while ((line = reader.readLine()) != null) {
	
				List<String> queryList = new ArrayList<>();
				line = line.toLowerCase().replaceAll("\\p{Punct}+\\s{0,1}", "");
				line = line.trim();
				String[] words = line.split("\\s+");
				
				for (String word: words) {
					queryList.add(word);
				}
				Collections.sort(queryList);
				line = String.join(" ", queryList);
				
				if (exact) {
					results.put(line, index.exactSearch(words));
				}
				
				else {
					results.put(line, index.partialSearch(words));
				}

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + file.toString());
		}
			
	}
	
	/**
	 * Writes the search results to a "pretty" JSON format
	 * 
	 * @param output
	 * 		the output file name
	 * @throws IOException
	 * 
	 */
	public void toJSON (String output) throws IOException {
		Path outputFile = Paths.get(output);
		JSONWriter.toJSON(outputFile, results);
	}
	

}
