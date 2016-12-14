import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO v4.2 is not passing 100% of tests (testRuntime()) and unable to run the benchmark script

/**
 * This class is a Search Engine project Done: Inverted Index and Partial Search, 
 * Web Crawler, Multithreading
 * 
 * @author Chezka Sino
 * 
 */

public class Driver {

	// TODO Do not throw exceptions in main
	/**
	 * Main method Initializes the program
	 * 
	 * @param argss
	 *            argument array
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, URISyntaxException {

		ArrayList<String> textFiles = new ArrayList<>();

		ArgumentParser parser = new ArgumentParser(args);

		if (parser.numFlags() == 0) {
			System.err.println("No arguments");
		}
		
		InvertedIndex indexx = null;
		QueryParserInterface query = null;
		@SuppressWarnings("rawtypes") // TODO Shouldn't do this
		WebCrawlerInterface crawler = null;
		WorkQueue queue = null;
		
		if (parser.hasFlag("-multi")) {
			
			ThreadSafeInvertedIndex threadSafe = new ThreadSafeInvertedIndex();
			indexx = threadSafe;
			
			int threads;

			try {
				threads = Integer.parseInt(parser.getValue("-multi"));
				if (threads == 0) {
					threads = 5;
				}
			} catch (NumberFormatException e) {
				threads = 5;
			}
			
			queue = new WorkQueue(threads);
			crawler = new MultithreadedWebCrawler(queue, threadSafe); // TODO Pass queue instead of # of threads
			query = new MultithreadedQueryParser(threadSafe, queue);
			
			if (parser.hasFlag("-dir")) {

				String dirPath = parser.getValue("-dir");

				try {

					Path dir = Paths.get(dirPath);
					textFiles.addAll(DirectoryTraverser.traverse(dir));
					MultithreadedInvertedIndexBuilder.readArray(textFiles, indexx, queue);

				}

				catch (NullPointerException e) {
					System.err.println("No directory provided.");
				}

			}
			
		}
		
		else {
			
			indexx = new InvertedIndex();
			query = new QueryParser(indexx); 
			crawler = new WebCrawler();
			
			if (parser.hasFlag("-dir")) {

				String dirPath = parser.getValue("-dir");

				try {

					Path dir = Paths.get(dirPath);
					textFiles.addAll(DirectoryTraverser.traverse(dir));
					InvertedIndexBuilder.readArray(textFiles, indexx);

				}

				catch (NullPointerException e) {
					System.err.println("No directory provided.");
				}

			}

		}
		
		if (parser.hasFlag("-url")) {
			String seed = parser.getValue("-url");
			crawler.addSeed(seed, indexx);

		}

		if (parser.hasFlag("-index")) {
			String indexPath = parser.getValue("-index", "index.json");
			indexx.toJSON(indexPath);
		}
		
		if (parser.hasFlag("-query") && parser.getValue("-query") != null) {
			query.parseFile(Paths.get(parser.getValue("-query")), false);
		}

		if (parser.hasFlag("-exact") && parser.getValue("-exact") != null) {
			query.parseFile(Paths.get(parser.getValue("-exact")), true);
		}
		
		if (parser.hasFlag("-results")) {
			String resultsPath = parser.getValue("-results", "results.json");
			query.toJSON(resultsPath);
		}
		
		if (queue != null) {
			queue.shutdown();
		}
		
	}
	
}
