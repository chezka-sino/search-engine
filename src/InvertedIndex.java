import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.OverlayLayout;

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
	private final TreeMap<String, TreeMap <String, TreeSet<Integer>>> map; // TODO Refactor to map

	/**
	 * Class constructor
	 * 
	 * @param index
	 * 				the JSON file where the index would be written
	 */
	public InvertedIndex() { // TODO Remove the index parameter
		map = new TreeMap<>();
	}
//	
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
		if (!map.containsKey(word)) {
			TreeMap<String, TreeSet<Integer>> textPosition = new TreeMap<>();
			map.put(word, textPosition);
		}
		
		if (!map.get(word).containsKey(path)) {
			TreeSet<Integer> posSet = new TreeSet<>();
			map.get(word).put(path, posSet);
		}
		
		map.get(word).get(path).add(position);
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
//		JSONWriter writer = new JSONWriter(outputFile, map);
//		JSONWriter.writeJSON(outputFile, fileMap);
		JSONWriter.writeJSON(outputFile, map);
	}
	
	@Override
	public String toString() {
//		String wordMap = "";
//		
//		for (String word: map.keySet()) {
//			wordMap += "WORD: " + word + '\n';
//			
//			for (String fileName: map.get(word).keySet()) {
//				wordMap += '\t' + ".TXT FILE: " + fileName + '\n';
//				
//				for (Integer pos: map.get(word).get(fileName)) {
//					wordMap+= '\t' + '\t' + pos.toString() + 'n';
//					
//				}
//			}
//		}
//		
		return map.toString();
		
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
		wordList.addAll(map.keySet());
		return wordList;
	}

}
