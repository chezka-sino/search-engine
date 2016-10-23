import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 * @author shinheera
 *
 */
public class QueryParser {

	/**
	 * 
	 * @param inputFile
	 * @param searchType
	 * @param index
	 */
	public static void parseFilePartial(Path inputFile, InvertedIndex index) {

		// Map<String, List> searchResults = new HashMap<>();
		// ArrayList<String []> allQuery = new ArrayList<>();
		int count = 0;

		try (BufferedReader reader = Files.newBufferedReader(inputFile)) {

			String line;

			while ((line = reader.readLine()) != null) {

				line = line.toLowerCase().replaceAll("\\p{Punct}+\\s{0,1}", "");
				index.partial(line);

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read query file: " + inputFile.toString());
		}

	}

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

	public static void toJSON(Path outputFile) {
		
		

	}

	// TODO Query file, storing and writing of results

}
