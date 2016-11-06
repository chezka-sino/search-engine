import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class demonstrates traversing through a directory, looking for .txt
 * files, organizing words into an array and writing them in a JSON File
 * 
 * @author Chezka Sino
 * 
 */

// TODO Rename argP to "parser"

public class Driver {

	/**
	 * Main method Initializes the program
	 * 
	 * @param argss
	 *            argument array
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		ArrayList<String> textFiles = new ArrayList<>();

		ArgumentParser argP = new ArgumentParser(args);
		InvertedIndex index = new InvertedIndex();
		
		if (argP.numFlags() == 0) {
			System.err.println("No arguments");
		}
		
		if (argP.hasFlag("-dir")) {
			
			String dirPath = argP.getValue("-dir");
			
			try {
				
				Path dir = Paths.get(dirPath);
				textFiles.addAll(DirectoryTraverser.traverse(dir));
				InvertedIndexBuilder.readArray(textFiles, index);
				
			}
			
			catch (NullPointerException e) {
				System.err.println("No directory provided.");
			}
			
		}
		
		if (argP.hasFlag("-index")) {
			String indexPath = argP.getValue("-index", "index.json");
			index.toJSON(indexPath);		
		}
		
		if (argP.hasFlag("-results")) {
			String resultsPath = argP.getValue("-results", "results.json");
			
			if (argP.hasFlag("-query")) {
				QueryParser partialSearch = new QueryParser(index);
				partialSearch.parseFile(Paths.get(argP.getValue("-query")), false);
				partialSearch.toJSON(resultsPath);
			}

			if (argP.hasFlag("-exact")) {
				QueryParser exactSearch = new QueryParser(index);
				exactSearch.parseFile(Paths.get(argP.getValue("-exact")), true);
				exactSearch.toJSON(resultsPath);
			}
			
		}

		

		// TODO Do not need this check at all
//		if (argP.getValue("-dir") == null || !argP.hasFlag("-dir")) {
//			System.err.println("Invalid directory. Check directory input");
//		}
//		else {
//
//			try {
//
//				Path dir = Paths.get(dirPath);
//				textFiles.addAll(DirectoryTraverser.traverse(dir));
//
//				InvertedIndex index = new InvertedIndex();
//				InvertedIndexBuilder.readArray(textFiles, index);
//
//				if (!checkJSONPath(argP).equals("")) {
//					indexPath = checkJSONPath(argP);
//				}
//
//				if (resultsPath == null || resultsPath.equals("")) {
//					resultsPath = checkResultsPath(argP);
//				}
//
//				if (!(queryPath == null)) {
//					QueryParser partialSearch = new QueryParser(index);
//					partialSearch.parseFile(Paths.get(queryPath), false);
//					partialSearch.toJSON(resultsPath);
//				}
//
//				if (!(exactPath == null)) {
//					QueryParser exactSearch = new QueryParser(index);
//					exactSearch.parseFile(Paths.get(exactPath), true);
//					exactSearch.toJSON(resultsPath);
//				}
//
//				if (indexPath == null || indexPath.equals("")) {
//					indexPath = checkJSONPath(argP);
//				}
//
//				index.toJSON(indexPath);
//
//			}
//
//			catch (IOException e) {
//
//			}
//
//		}

	}

}
