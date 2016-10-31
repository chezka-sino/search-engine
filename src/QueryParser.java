import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class parses the query file that contains all of the query words for searching
 * 
 * @author Chezka Sino
 *
 */
public class QueryParser {

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
