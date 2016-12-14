import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO Check your TODOs

/**
 * This class is a Search Engine project Done: Inverted Index and Partial Search
 * In Progress: Web Crawler
 * 
 * @author Chezka Sino
 * 
 */

public class Driver {

	/**
	 * Main method Initializes the program
	 * 
	 * @param argss
	 *            argument array
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {

		ArrayList<String> textFiles = new ArrayList<>();

		ArgumentParser parser = new ArgumentParser(args);
		ThreadSafeInvertedIndex index = new ThreadSafeInvertedIndex();

		if (parser.numFlags() == 0) {
			System.err.println("No arguments");
		}

		if (parser.hasFlag("-multi")) {
			int threads;

			try {
				threads = Integer.parseInt(parser.getValue("-multi"));
				if (threads == 0) {
					threads = 5;
				}
			} catch (NumberFormatException e) {
				threads = 5;
			}

			WorkQueue workQueue = new WorkQueue(threads);

			if (parser.hasFlag("-dir")) {

				String dirPath = parser.getValue("-dir");

				try {

					Path dir = Paths.get(dirPath);
					textFiles.addAll(DirectoryTraverser.traverse(dir));
					MultithreadedInvertedIndexBuilder.readArray(textFiles, index, workQueue);

				}

				catch (NullPointerException e) {
					System.err.println("No directory provided.");
				}

			}

			if (parser.hasFlag("-url")) {
				MultithreadedWebCrawler parseURL = new MultithreadedWebCrawler(threads, index);

				String seed = parser.getValue("-url");
				parseURL.addSeed(seed);

			}

			if (parser.hasFlag("-index")) {
				String indexPath = parser.getValue("-index", "index.json");
				index.toJSON(indexPath);
			}

			if (parser.hasFlag("-results")) {
				String resultsPath = parser.getValue("-results", "results.json");

				if (parser.hasFlag("-query")) {
					MultithreadedQueryParser partialSearch = new MultithreadedQueryParser(index, workQueue);
					partialSearch.parseFile(Paths.get(parser.getValue("-query")), false);
					partialSearch.toJSON(resultsPath);
				}

				if (parser.hasFlag("-exact")) {
					MultithreadedQueryParser exactSearch = new MultithreadedQueryParser(index, workQueue);
					exactSearch.parseFile(Paths.get(parser.getValue("-exact")), true);
					exactSearch.toJSON(resultsPath);
				}

			}

		}

		else {
			if (parser.hasFlag("-dir")) {

				String dirPath = parser.getValue("-dir");

				try {

					Path dir = Paths.get(dirPath);
					textFiles.addAll(DirectoryTraverser.traverse(dir));
					InvertedIndexBuilder.readArray(textFiles, index);

				}

				catch (NullPointerException e) {
					System.err.println("No directory provided.");
				}

			}

			if (parser.hasFlag("-url")) {
				WebCrawler parseURL = new WebCrawler();

				String seed = parser.getValue("-url");
				parseURL.addSeed(seed, index);

			}

			if (parser.hasFlag("-index")) {
				String indexPath = parser.getValue("-index", "index.json");
				index.toJSON(indexPath);
			}

			if (parser.hasFlag("-results")) {
				String resultsPath = parser.getValue("-results", "results.json");

				if (parser.hasFlag("-query")) {
					QueryParser partialSearch = new QueryParser(index);
					partialSearch.parseFile(Paths.get(parser.getValue("-query")), false);
					partialSearch.toJSON(resultsPath);
				}

				if (parser.hasFlag("-exact")) {
					QueryParser exactSearch = new QueryParser(index);
					exactSearch.parseFile(Paths.get(parser.getValue("-exact")), true);
					exactSearch.toJSON(resultsPath);
				}

			}

		}
		
		InvertedIndex index2 = null;
		QueryParserInterface query = null;
		WorkQueue queue = null;
		
		if (parser.hasFlag("-multi")) {
			ThreadSafeInvertedIndex threadSafe = new ThreadSafeInvertedIndex();
			index = threadSafe;
			queue = new WorkQueue(parser.getValue("-multi", 5));
			
			query = new MultithreadedQueryParser(threadSafe,queue);
		}
		
		else {
			index2 = new InvertedIndex();
			query = new QueryParser(index2);
		}
		
		if (parser.hasFlag("-exact")) {
			query.parseFile(Paths.get(parser.getValue("-exact")), true);
		}
		
		if (parser.hasFlag("-partial")) {
			query.parseFile(Paths.get(parser.getValue("-partial")), false);
		}
		
		if (queue != null) {
			queue.shutdown();
		}
		
	}

	/*
	 * TODO
	 * 
	 * InvertedIndex index = null; QueryParserInterface query = null;
	 * 
	 * WorkQueue queue = null;
	 * 
	 * if (-multi) {
	 * 
	 * ThreadSafeInvertedIndex threadSafe = new THreadSafeInvertedIndex(); index
	 * = threadSafe;
	 * 
	 * queue = new WorkQueue(threads);
	 * 
	 * query = new MultithreadedQueryParser(threadSafe, queue); } else { index =
	 * new InvertedIndex(); query = new QueryParser(index); }
	 * 
	 * 
	 * 
	 * if (-exact) { query.parseFile(path, true); }
	 * 
	 * 
	 * 
	 * if (queue != null) {
	 * 
	 * queue.shutdown(); }
	 * 
	 * 
	 */
	
	
	
}
