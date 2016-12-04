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
		InvertedIndex index = new InvertedIndex();
//		ThreadSafeInvertedIndex index = new ThreadSafeInvertedIndex();

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
			}
			catch (NumberFormatException e) {
				threads = 5;
			}
			
			WorkQueue workQueue = new WorkQueue(threads);
			
			if (parser.hasFlag("-dir")) {

				String dirPath = parser.getValue("-dir");

				try {

					Path dir = Paths.get(dirPath);
					textFiles.addAll(DirectoryTraverser.traverse(dir));
					InvertedIndexBuilder.readArray(textFiles, index, workQueue);

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
	}
	/*
	 * Project 4 Hints:
	 * 
	 * Create a thread-safe inverted index that extends your inverted index and uses the synchronized keyword
	 * Use the work queue from the prime finder homework
	 * 
	 * Create NEW classes (do not extend) that are multithreaded for your inverted index builder, query parser, web crawler
	 * - building, one "task" or Runnable object per file
	 * - searching, one "task" per query line
	 * - crawling, one "task" per url
	 * 
	 * Change 1 class at a time, then test.
	 * Do not worry about efficiency, try to get into code review for help speeding things up
	 *  
	 * Have an ugly driver to start... (ok)
	 * if (-multi) {
	 * 		everything you have now
	 * 		change classes one-by-one to multithreaded versions
	 * 
	 * }
	 * else {
	 * 		everything you have now
	 * }
	 */
	
}
