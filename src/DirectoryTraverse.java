import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DirectoryTraverse {
	
	private final ArrayList<String> textFiles;
	public Path dir, ind;
	
	public DirectoryTraverse(String directory, String index) {

		dir = Paths.get(directory);
		ind = Paths.get(index);
		textFiles = new ArrayList<>();
		
	}
	
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
	
	public Path getIndex() {
		return ind;
	}
	
	public Path getDir() {
		return dir;
	}
	
	public ArrayList<String> getFileList () {
		return textFiles;
	}

}
