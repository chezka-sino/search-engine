import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class parses the query file that contains all of the query words for
 * searching
 * 
 * @author Chezka Sino
 *
 */
public class MultithreadedQueryParser implements QueryParserInterface {

	private final ThreadSafeInvertedIndex index;

	// TODO Protect all access to results (synchronized (results))
	private final TreeMap<String, List<SearchResult>> results;

	private static final Logger LOGGER = LogManager.getLogger();
	
	private final WorkQueue minions;

	/**
	 * QueryParser constructor
	 * 
	 * @param index
	 *            ThreadSafeInvertedIndex object
	 * 
	 */
	public MultithreadedQueryParser(ThreadSafeInvertedIndex index, WorkQueue minions) {
		this.index = index;
		this.results = new TreeMap<>();
		this.minions = minions;
	}

	/**
	 * Goes through the query file line by line for the search words
	 * 
	 * @param file
	 *            file path containing the query words
	 * @param exact
	 *            true if conducting an exact search, false otherwise
	 * @param threads
	 *            number of threads in the workqueue
	 * 
	 */
	public void parseFile(Path file, boolean exact) {

		class QueryMinion implements Runnable {

			private String line;

			public QueryMinion(String line) {
				LOGGER.debug("Minion created for {}", line);
				this.line = line;
			}

			@Override
			public void run() {
				
				line = line.toLowerCase().replaceAll("\\p{Punct}+\\s{0,1}", "");
				line = line.trim();
				String[] words = line.split("\\s+");
				Arrays.sort(words);
				line = String.join(" ", words);

				List<SearchResult> current;
				if (exact)
					current = index.exactSearch(words);
				else
					current = index.partialSearch(words);

				synchronized (results) {
					results.put(line, current);
				}

				LOGGER.debug("Minion finished search on {}", line);
			}

		}

		try (BufferedReader reader = Files.newBufferedReader(file)) {

			String line;

			while ((line = reader.readLine()) != null) {

				minions.execute(new QueryMinion(line));
				LOGGER.debug("Minion finished {}", line);

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + file.toString());
			LOGGER.warn("Unable to read query file {}", file.toString());
		}

		minions.finish();

	}

	/**
	 * Writes the search results to a "pretty" JSON format
	 * 
	 * @param output
	 *            the output file name
	 * @throws IOException
	 * 
	 */
	public void toJSON(String output) throws IOException {
		Path outputFile = Paths.get(output);
		synchronized (results) {
			JSONWriter.searchResultsWriter(outputFile, results); // TODO Protect
																	// results
		}

	}

}
