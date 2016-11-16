import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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

		if (parser.numFlags() == 0) {
			System.err.println("No arguments");
		}

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
			URLParser parseURL = new URLParser();

			String seed = parser.getValue("-url");
			parseURL.urlList(seed, index);

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
