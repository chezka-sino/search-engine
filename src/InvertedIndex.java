import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class indexes the words.
 * 
 * @author Chezka Sino
 *
 */
public class InvertedIndex {
	
	/**
	 * The TreeMap of the words
	 */
	private final TreeMap<String, TreeMap <String, TreeSet<Integer>>> fileMap; // TODO Refactor to map

	/**
	 * Class constructor
	 * 
	 * @param index
	 * 				the JSON file where the index would be written
	 */
	public InvertedIndex(String index) { // TODO Remove the index parameter
		fileMap = new TreeMap<>();
	}
	
	/**
	 * Put the words mapped to their corresponding .txt files and position in
	 * the TreeMap
	 * 
	 * @param word
	 * 				the word
	 * @param path
	 * 				txt file where the word was found
	 * @param position
	 * 				position of the word
	 * 
	 */
	public void add(String word, String path, int position) {
		if (!fileMap.containsKey(word)) {
			TreeMap<String, TreeSet<Integer>> textPosition = new TreeMap<>();
			fileMap.put(word, textPosition);
		}
		
		if (!fileMap.get(word).containsKey(path)) {
			TreeSet<Integer> posSet = new TreeSet<>();
			fileMap.get(word).put(path, posSet);
		}
		
		fileMap.get(word).get(path).add(position);
	}
	
	/**
	 * Writes the treeMap to JSON format
	 * 
	 * @see JSONWriter
	 * @param index
	 * 				the output file
	 * 				
	 * @throws IOException
	 */
	public void toJSON(String index) throws IOException {
		Path outputFile = Paths.get(index);
		
		// TODO JSONWriter.writeJSON(outputFile, fileMap)
		JSONWriter writer = new JSONWriter(outputFile, fileMap);
		writer.writeJSON();
	}
	
	@Override
	public String toString() {
		String wordMap = "";
		
		for (String word: fileMap.keySet()) {
			wordMap += "WORD: " + word + '\n';
			
			for (String fileName: fileMap.get(word).keySet()) {
				wordMap += '\t' + ".TXT FILE: " + fileName + '\n';
				
				for (Integer pos: fileMap.get(word).get(fileName)) {
					wordMap+= '\t' + '\t' + pos.toString() + 'n';
					
				}
			}
		}
		
		return wordMap;
		
		// TODO return fileMap.toString();
	}
	
	// TODO Adding a numWords() method, numLocations(String word), etc.
	// TODO Adding a containsWord(), containsLocation(String word), etc.
	
	/**
	 * TODO 
	 * @return
	 * 			The list of the words in the Map
	 */
	public List<String> getWords() {
		List <String> wordList = new ArrayList<>();
		wordList.addAll(fileMap.keySet());
		return wordList;
	}

}
