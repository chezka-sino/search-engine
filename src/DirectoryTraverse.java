import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DirectoryTraverse {
	
	private final ArrayList<String> textFiles;
//	private Map<String, String> argMap;
	public Path dir;
	
	public DirectoryTraverse(String directory) {
		// TODO Auto-generated constructor stub

		dir = Paths.get(directory);
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
						textFiles.add(file.toAbsolutePath().normalize().toString());
					}
				}
			}
			
		}
		
	}
	
	
	
	public Path getDir() {
		return dir;
	}
	
	public ArrayList<String> getFileList () {
		return textFiles;
	}

}
