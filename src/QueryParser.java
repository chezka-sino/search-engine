import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
	
//	private final InvertedIndex index;
//	private final TreeMap<String, List<SearchResult>> results;
	
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
		if (exact) {
			
		}
		else {
			//partialSearch()
		}
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

	/**
	 * Parses the file for exact search query
	 * 
	 * @param inputFile
	 * 			the file that contains the query words
	 * @param index
	 * 			InvertedIndex object
	 * @see InvertedIndex
	 * @see InvertedIndex#exact(String)
	 * 
	 */
	public static void parseFileExact(Path inputFile, InvertedIndex index) {

		try (BufferedReader reader = Files.newBufferedReader(inputFile)) {

			String line;

			while ((line = reader.readLine()) != null) {

				line = line.toLowerCase().replaceAll("\\p{Punct}+\\s{0,1}", "");
				index.exact(line);

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + inputFile.toString());
		}


	}

}
