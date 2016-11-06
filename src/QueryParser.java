import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

// TODO This is a hybrid class that stores and does stuff

/**
 * This class parses the query file that contains all of the query words for searching
 * 
 * @author Chezka Sino
 *
 */
public class QueryParser {
	
	// TODO No longer a bunch of static methods
	
	private final InvertedIndex index;
	private final TreeMap<String, List<SearchResult>> results;
	
//	
	public QueryParser(InvertedIndex index) {
		this.index = index;
		this.results = new TreeMap<>();
	}
	
	/*
	public void parseFile(Path file, boolean exact) {
		loop line by line
			if (exact)
				index.exactSearch()
			else
				index.partialSearch()
	}
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
//					results.put(line, index.partialSearch(words));
					//partial
				}

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + file.toString());
		}
			
	}
	
	public void toJSON (String output) throws IOException {
		Path outputFile = Paths.get(output);
		JSONWriter.toJSON(outputFile, results);
	}
	
	/**
	 * Parses the file for partial search query
	 * 
	 * @param inputFile
	 * 			the file that contains the query words
	 * @param index
	 * 			InvertedIndex object
	 * @see InvertedIndex
	 * @see InvertedIndex#partial(String)
	 * 
	 */
	public static void parseFilePartial(Path inputFile, InvertedIndex index) {

		try (BufferedReader reader = Files.newBufferedReader(inputFile)) {

			String line;

			while ((line = reader.readLine()) != null) {

				line = line.toLowerCase().replaceAll("\\p{Punct}+\\s{0,1}", "");
				
				// TODO Split and sort here
				// TODO Store the results: results.put(line, index.exactSearch(queryWords))
				
				index.partial(line);

			}
			
		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + inputFile.toString());
		}

	}

}
