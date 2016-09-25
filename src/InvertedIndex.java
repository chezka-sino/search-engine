import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex {
	
	// TODO final, not static
	private final TreeMap <String, TreeMap <String, TreeSet<Integer>>> fileMap;
	
	// TODO This can be a method parameter?
	private final Path outputFile;
	
	public InvertedIndex(String index) {
		fileMap = new TreeMap<>();
		this.outputFile = Paths.get(index);
		
	}
	
	public void add(String word, String path, int position) {
		
		TreeMap<String, TreeSet<Integer>> textPosition = new TreeMap<>();
		
		if (!fileMap.containsKey(word)) {
			fileMap.put(word, textPosition);
		}
		
		if (!fileMap.get(word).containsKey(path)) {
			TreeSet<Integer> posSet = new TreeSet<>();
			fileMap.get(word).put(path, posSet);
		}
		
		fileMap.get(word).get(path).add(position);
		
	}
	
	public void toJSON() throws IOException {
		JSONWriter writer = new JSONWriter(outputFile,fileMap);
		writer.writeJSON();
	}

}
