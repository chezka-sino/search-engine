import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class traverses through the directory provided and looks for all the .txt
 * files in the directory.
 * 
 * @author Chezka Sino
 *
 */
public class DirectoryTraverse {
	
	/** TODO Add description */
	private static ArrayList<String> textFiles;
	
	// TODO These aren't good....
	private static Path dir;
	
	/**
	 * Constructor of the class
	 * 
	 * @param directory
	 * 			the directory to be traversed
	 * @param index
	 * 			the JSON file to where the index would be stored
	 */
	public DirectoryTraverse(String directory) {
		
		textFiles = new ArrayList<>();
		dir = Paths.get(directory);
		
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
	
	/* TODO
	public static List<String> traverse(Path path) {
		List<String> paths = new ArrayList<>();
		traverse(path, paths);
		return paths;
	}
	
	private static void traverse(Path path, List<String> paths) {
		loop through every path
			if subdir
				traverse(subdir, paths)
			else if text file
				add path to paths
	}
	*/
	
	public static List<String> traverse(Path path) throws IOException {
		traverse(path,textFiles);
		return textFiles;
	}
	
	private static void traverse(Path path, List<String> paths) throws IOException {
		DirectoryStream<Path> listing = Files.newDirectoryStream(path);
		
		for (Path file: listing) {
			if (Files.isDirectory(file)) {
				traverse(file, paths);
			}
			else {
				if (file.toString().toLowerCase().endsWith(".txt")) {
					paths.add(file.normalize().toString());
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @return
	 * 		the directory that was traversed
	 */
	public Path getDir() {
		return dir;
	}

}
