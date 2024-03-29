import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class looks into the .txt files, calls the ThreadSafeInvertedIndex class
 * to add the words in a TreeMap then calls the JSONWriter to write the Inverted
 * Index to a "pretty" JSON format
 * 
 * @see JSONWriter
 * @see ThreadSafeInvertedIndex
 * 
 * @author Chezka Sino
 *
 */
public class MultithreadedInvertedIndexBuilder {

	private static final Logger LOGGER = LogManager.getLogger();

	// TODO pass in a thread-safe inverted index as a parameter
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
	public static void readArray(ArrayList<String> textFiles, InvertedIndex index, WorkQueue minions)
			throws IOException {

		class DirectoryMinion implements Runnable {

			private String file;

			public DirectoryMinion(String file) {
				LOGGER.debug("Minion created for {}", file);
				this.file = file;

			}

			@Override
			public void run() {
				Path inputFile = Paths.get(file);
				// TODO InvertedIndexBuilder.openFile(inputFile, index);

				InvertedIndex local = new InvertedIndex();
				InvertedIndexBuilder.openFile(inputFile, local);
				index.addAll(local);
				
				LOGGER.debug("Minion finished openFile on {}", inputFile);
			}

		}

		for (String name : textFiles) {
			minions.execute(new DirectoryMinion(name));
			LOGGER.debug("Minion finished {}", name);
		}

		minions.finish();

	}

}
