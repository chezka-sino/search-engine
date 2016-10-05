import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// TODO Different design for classes that store stuff vs do stuff

/**
 * This class traverses through the directory provided and looks for all the .txt
 * files in the directory.
 * 
 * @author Chezka Sino
 *
 */
public class DirectoryTraverse {
	
	/**
	 * Calls the recursive traverse method to get the .txt files
	 * 
	 * @param path
	 * 			the directory to be traversed
	 * @return
	 * 			the list of .txt files
	 * @throws IOException
	 * 
	 */
	public static List<String> traverse(Path path) throws IOException {
		ArrayList<String> textFiles = new ArrayList<>();
		traverse(path,textFiles);
		return textFiles;
	}
	
	/**
	 * This method recursively traverses through the directory and it adds all
	 * the .txt files into an list
	 * 
	 * @param path
	 * 			the directory to be traversed
	 * @param paths
	 * 			list of .txt files found in the directory
	 * 
	 */
	private static void traverse(Path path, List<String> paths) {
		
		try {
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
		
		catch (IOException e) {
			System.err.println("IOException caught: " + e.getMessage());
		}
		
	}

}
