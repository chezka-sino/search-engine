import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> map;
	private final TreeMap<String, HashMap<String, TreeSet<Integer>>> partialSearchMap;
	private final TreeMap<String, HashMap<String, TreeSet<Integer>>> exactSearchMap;
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> sortedSearchResult;

	/**
	 * Class constructor
	 */
	public InvertedIndex() {
		map = new TreeMap<>();
		partialSearchMap = new TreeMap<>();
		exactSearchMap = new TreeMap<>();
		sortedSearchResult = new TreeMap<>();
	}

	/**
	 * Put the words mapped to their corresponding .txt files and position in
	 * the TreeMap
	 * 
	 * @param word
	 *            the word
	 * @param path
	 *            txt file where the word was found
	 * @param position
	 *            position of the word
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
	 *            the output file
	 * 
	 * @throws IOException
	 */
	public void toJSON(String index) throws IOException {
		Path outputFile = Paths.get(index);
		JSONWriter.writeJSON(outputFile, map);
		
	}

	@Override
	public String toString() {
		return map.toString();
	}

	/**
	 * Returns the number of words in the map
	 * 
	 * @return the number of words in the map
	 * 
	 */
	public int numWords() {
		return map.size();
	}

	/**
	 * Returns the number of files where word can be found
	 * 
	 * @param word
	 *            the word to be checked for the number of locations
	 * @return the number of files where word can be found
	 */
	public int numLocations(String word) {
		if (!map.containsKey(word)) {
			return 0;
		}
		return map.get(word).size();
	}

	public int firstIndex(String word, String fileName) {

		if (map.containsKey(word)) {
			return map.get(word).get(fileName).first();
		}

		return 0;

	}

	/**
	 * Checks if the map contains the word
	 * 
	 * @param word
	 *            the word to be searched
	 * @return true if the word is in the map, false otherwise
	 */
	public boolean containsWord(String word) {
		return map.containsKey(word);
	}

	/**
	 * Checks if the word can be found in the text file
	 * 
	 * @param word
	 *            the word to be searched
	 * @param textFile
	 *            the .txt file to be checked
	 * @return true if the word can be found in the text file, false otherwise
	 */
	public boolean containsLocation(String word, String textFile) {
		if (map.containsKey(word)) {
			return map.get(word).containsKey(textFile);
		}
		return false;
	}

	/**
	 * Returns the list of the words in the map
	 * 
	 * @return The list of the words in the Map
	 */
	public List<String> getWords() {
		List<String> wordList = new ArrayList<>();
		wordList.addAll(map.keySet());
		return wordList;
	}

	public List<String> getFileLocations(String word) {
		List<String> locations = new ArrayList<>();
		locations.addAll(map.get(word).keySet());
		return locations;
	}

	public void partial(String queryLine) {
		
		List<String> queryList = new ArrayList<>();
		queryLine = queryLine.trim();
		String[] words = queryLine.split("\\s+");
		
		for (String indWords: words) {
			queryList.add(indWords);
		}
		Collections.sort(queryList);
		queryLine = String.join(" ", queryList);
		
		if (!partialSearchMap.containsKey(queryLine)) {
			partialSearchMap.put(queryLine, new HashMap<>());
		}
		
		if (partialSearch(words) != null || !partialSearch(words).isEmpty()) {
			partialSearchMap.put(queryLine, partialSearch(words));
		}
		
		else {
			partialSearchMap.put(queryLine, null);
		}
		
		for (String queryWord: partialSearchMap.keySet()) {
			sortSearchResult(queryWord, partialSearchMap);
		}

	}

	public HashMap<String, TreeSet<Integer>> partialSearch(String[] queryWords) {

		List<String> words = new ArrayList<>();
		words.addAll(map.keySet());

		HashMap<String, TreeSet<Integer>> fileMatches = new HashMap<>();

		for (String word : queryWords) {

			for (String match : words) {
				
				if (match.startsWith(word)) {
					for (String filename: map.get(match).keySet()) {
						if (!fileMatches.containsKey(filename)) {
							fileMatches.put(filename, new TreeSet<>());
						}

						fileMatches.get(filename).addAll(map.get(match).get(filename));
					}
					
				}
			}
		}

		return fileMatches;

	}

	public void exact(String queryLine) {
		
		List<String> queryList = new ArrayList<>();
		queryLine = queryLine.trim();
		String[] words = queryLine.split("\\s+");

		for (String indWords: words) {
			queryList.add(indWords);
		}
		Collections.sort(queryList);
		queryLine = String.join(" ", queryList);
		
		if (!exactSearchMap.containsKey(queryLine)) {
			exactSearchMap.put(queryLine, new HashMap<>());
		}
		
		if (exactSearch(words) != null || !exactSearch(words).isEmpty()) {
			exactSearchMap.put(queryLine, exactSearch(words));
		}
		
		else {
			exactSearchMap.put(queryLine, null);
		}

		for (String queryWord : exactSearchMap.keySet()) {
			sortSearchResult(queryWord, exactSearchMap);
		}
		
	}

	public HashMap<String, TreeSet<Integer>> exactSearch(String[] queryWords) {

		List<String> words = new ArrayList<>();
		words.addAll(map.keySet());

		HashMap<String, TreeSet<Integer>> fileMatches = new HashMap<>();

		for (String word : queryWords) {

			for (String match : words) {

				if (match.equals(word)) {

					for (String filename : map.get(match).keySet()) {
						if (!fileMatches.containsKey(filename)) {
							fileMatches.put(filename, new TreeSet<>());
						}

						fileMatches.get(filename).addAll(map.get(match).get(filename));

					}

				}
			}
		}
		
		return fileMatches;

	}

	public void sortSearchResult(String queryWord, TreeMap<String, HashMap<String, TreeSet<Integer>>> unsorted) {

		TreeMap<String, TreeSet<Integer>> sortedMap = new TreeMap<>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				
				if (unsorted.get(queryWord).get(o1) == null ||
						unsorted.get(queryWord).get(o2) == null) {
					return 0;
				}

				int size1 = unsorted.get(queryWord).get(o1).size();
				int size2 = unsorted.get(queryWord).get(o2).size();

				if (Integer.compare(size1, size2) != 0) {

					return -(Integer.compare(size1, size2));
				}

				else {

					int first1 = unsorted.get(queryWord).get(o1).first();
					int first2 = unsorted.get(queryWord).get(o2).first();

					if (first1 != first2) {
						return Integer.compare(first1, first2);
					}

					else {
						return o1.compareTo(o2);
					}
				}

			}

		});

		sortedMap.putAll(unsorted.get(queryWord));
		sortedSearchResult.put(queryWord, sortedMap);

	}
	
	public void searchToJSON(String output) throws IOException {
		Path outputFile = Paths.get(output);
		JSONWriter.writeJSONSearch(outputFile, sortedSearchResult);		
		
	}

}
