import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
public class MultithreadedQueryParser {

	private final ThreadSafeInvertedIndex index;
	
	// TODO Protect all access to results (synchronized (results))
	private final TreeMap<String, List<SearchResult>> results;

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * QueryParser constructor
	 * 
	 * @param index
	 *            ThreadSafeInvertedIndex object
	 * 
	 */
	public MultithreadedQueryParser(ThreadSafeInvertedIndex index) {
		LOGGER.debug("New QueryParser ThreadsafeInvertedIndex");
		this.index = index;
		this.results = new TreeMap<>();
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
	public void parseFile(Path file, boolean exact, int threads) {

		WorkQueue minions = new WorkQueue(threads);

		class QueryMinion implements Runnable {

			private String line;

			public QueryMinion(String line) {
				LOGGER.debug("Minion created for {}", line);
				this.line = line;
			}

			@Override
			public void run() {
				List<String> queryList = new ArrayList<>();
				line = line.toLowerCase().replaceAll("\\p{Punct}+\\s{0,1}", "");
				line = line.trim();
				String[] words = line.split("\\s+");

				// TODO FIx (see QueryParser)
				for (String word : words) {
					queryList.add(word);
				}
				Collections.sort(queryList);
				line = String.join(" ", queryList);
				
				/*
				 * TODO
				 * 
				 * synchronized (results) {
				 * 		if (exact) {
							results.put(line, index.exactSearch(words));
						}
		
						else {
							results.put(line, index.partialSearch(words));
						}
				 * }
				 * 
				 * -versus-
				 * 
				 * List<SearchResult> current = (exact) ? index.exactSearch(words) : index.partialSearch(word);
				 * 
				 * synchronized (results) {
				 * 		results.put(line, current);
				 * }
				 */
				
				synchronized (results) {
					if (exact) {
						results.put(line, index.exactSearch(words));
					}
					else {
						results.put(line, index.partialSearch(words));
					}
					
				}

				LOGGER.debug("Minion finished search on {}", line);
			}

		}

		try (BufferedReader reader = Files.newBufferedReader(file)) {

			String line;

			while ((line = reader.readLine()) != null) {

				minions.execute(new QueryMinion(line));
				LOGGER.debug("Minion finished {}", line);
				minions.finish();

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + file.toString());
			LOGGER.warn("Unable to read query file {}", file.toString());
		}

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
			JSONWriter.searchResultsWriter(outputFile, results); // TODO Protect results
		}
		
	}

}
