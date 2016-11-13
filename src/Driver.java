import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class is a Search Engine project
 * Progress: Inverted Index and Partial Search
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
		
//		if (parser.hasFlag("-index")) {
//			String indexPath = parser.getValue("-index", "index.json");
//			index.toJSON(indexPath);		
//		}
		
		if (parser.hasFlag("-url")) {
			// TODO stuff for project 3
			
			String seed = parser.getValue("-url");
			String html = URLParser.fetchHTML(seed);
			ArrayList<String> URLList = URLParser.URLList(html, seed);
			
			for (String link: URLList) {
//				System.out.println("LINK: " + link);
				String htmlFile = URLParser.fetchHTML(link);
				String [] cleanedHTML = HTMLCleaner.fetchWords(htmlFile);
//				System.out.println(cleanedHTML);
				
				InvertedIndexBuilder.openHTML(link, cleanedHTML, index);
//				System.out.println();
				
			}
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
