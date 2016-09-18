import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DirectoryTraverse {
	
	private final ArrayList<String> textFiles;
//	private Map<String, String> argMap;
	public Path dir, ind;
	
	public DirectoryTraverse(String directory, String index) {
		// TODO Auto-generated constructor stub

		dir = Paths.get(directory);
		ind = Paths.get(index);
		textFiles = new ArrayList<>();
//		Path path = Paths.get(directory);
		
	}
	
	public void traverse(Path path) throws IOException {
		
		try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
			
			for (Path file: listing) {
				
				if (Files.isDirectory(file)) {
					traverse(file);
				}
				
				else {
					if (file.toString().endsWith(".txt")) {
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
