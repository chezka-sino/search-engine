import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
	
	private static final Logger LOGGER = LogManager.getLogger();

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
	
	public static void readArray(ArrayList<String> textFiles, InvertedIndex index, int threads) throws IOException {

		WorkQueue minions = new WorkQueue(threads);
		
		class DirectoryMinion implements Runnable {
			
			private String file;
			
			public DirectoryMinion(String file) {
				LOGGER.debug("Minion created for {}", file);
				this.file = file;
				minions.incrementPending();
			}

			@Override
			public void run() {
				Path inputFile = Paths.get(file);
				openFile(inputFile, index);
				LOGGER.debug("Minion finished openFile on {}", inputFile);
				minions.decrementPending();				
			}
			
		}
		
		for (String name : textFiles) {
//			minions.incrementPending();
			minions.execute(new DirectoryMinion(name));
			minions.finish();
			LOGGER.debug("Minion finished {}", name);
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

				line = line.replaceAll("\\p{Punct}+", "");
				String[] words = line.toLowerCase().split("\\s+");

				for (String i : words) {

					if (!i.isEmpty()) {
						toIndex.add(i, inputFile.toString(), count);
						count++;
					}

				}

			}

		}

		catch (IOException e) {
			System.err.println("Unable to read file: " + inputFile.toString());
			LOGGER.catching(Level.DEBUG, e);
		}

	}

}
