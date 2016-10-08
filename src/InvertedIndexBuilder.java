import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class looks into the .txt files, calls the InvertedIndex class to add
 * the words in a TreeMap then calls the JSONWriter to write the Inverted Index
 * to a "pretty" JSON format
 * 
 * @see JSONWriter
 * @see InvertedIndex
 * 
 * @author Chezka Sino
 *
 */
public class InvertedIndexBuilder {

	/**
	 * Goes through the list of .txt files
	 * 
	 * @param index
	 *            The output file for the JSONfile
	 * 
	 * @see InvertedIndex
	 * 
	 * @throws IOException
	 * 
	 */
	public static void readArray(ArrayList<String> textFiles, InvertedIndex index) throws IOException {

		for (String name : textFiles) {
			Path inputFile = Paths.get(name);
			openFile(inputFile, index);
		}

	}

	/**
	 * Reads through the files, puts the words in the map then calls the JSON
	 * class to write it into the file
	 * 
	 * @param inputFile
	 *            the file to be checked
	 * @param toIndex
	 *            InvertedIndex object
	 * @throws IOException
	 * 
	 */
	public static void openFile(Path inputFile, InvertedIndex toIndex) {

		int count = 1;

		try (BufferedReader reader = Files.newBufferedReader(inputFile)) {

			String line;

			while ((line = reader.readLine()) != null) {

				String[] words = line.toLowerCase().split("\\s+");

				for (String i : words) {

					i = i.replaceAll("\\p{Punct}+", "");
					if (!i.isEmpty()) {

						toIndex.add(i, inputFile.toString(), count);
						count++;
					}

				}

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read file: " + inputFile.toString());
		}

	}

}
