import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class traverses through the directory provided and looks for all the .txt
 * files in the directory.
 * 
 * @author Chezka Sino
 *
 */
public class DirectoryTraverse {
	
	/**
	 * 
	 */
	private final ArrayList<String> textFiles;
	public Path dir, ind;
	
	/**
	 * Constructor of the class
	 * 
	 * @param directory
	 * 			the directory to be traversed
	 * @param index
	 * 			the JSON file to where the index would be stored
	 */
	public DirectoryTraverse(String directory, String index) {

		dir = Paths.get(directory);
		ind = Paths.get(index);
		textFiles = new ArrayList<>();
		
	}
	
	/**
	 * This method recursively traverses through the directory and it adds all
	 * the .txt files into an ArrayList
	 * 
	 * @param path
	 * 			the directory to be traversed
	 * @throws IOException
	 * 
	 */
	public void traverse(Path path) throws IOException {
		
		try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
			
			for (Path file: listing) {
				
				if (Files.isDirectory(file)) {
					traverse(file);
				}
				
				else {
					if (file.toString().toLowerCase().endsWith(".txt")) {
						textFiles.add(file.normalize().toString());
					}
				}
			}
			
		}
		
	}
	
	/**
	 * 
	 * @return
	 * 		the path for the index
	 * 
	 */
	public Path getIndex() {
		return ind;
	}
	
	/**
	 * 
	 * @return
	 * 		the directory that was traversed
	 */
	public Path getDir() {
		return dir;
	}
	
	/**
	 * 
	 * @return
	 * 		the ArrayList of the .txt files
	 * 
	 */
	public ArrayList<String> getFileList () {
		return textFiles;
	}

}
