import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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

	/**
	 * Class constructor
	 */
	public InvertedIndex() {
		map = new TreeMap<>();
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
		JSONWriter.indexWriter(outputFile, map);

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

	/**
	 * Returns the first position of the word in a file
	 * 
	 * @param word
	 *            the word to be checked for the first location
	 * @param fileName
	 *            the filename where the word is checked
	 * @return the first position of the word in the filename; returns 0 if the
	 *         map doesn't contain the word or filename
	 */
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

	/**
	 * Searches for words that starts with the query words
	 * 
	 * @param queryWords
	 *            array of the query words
	 * @return the list of the sorted search results of the query words
	 * 
	 */
	public List<SearchResult> partialSearch(String[] queryWords) {

		ArrayList<SearchResult> results = new ArrayList<>();
		HashMap<String, SearchResult> resultMap = new HashMap<>();

		for (String word : queryWords) {

			for (String match : map.tailMap(word).keySet()) {

				if (match.startsWith(word)) {
					addSearchResults(match, results, resultMap);
				}

				else if (match.compareTo(word) > 0) {
					break;
				}
			}

		}

		Collections.sort(results);
		return results;

	}

	/**
	 * Searches for the exact matches of the query words
	 * 
	 * @param queryWords
	 *            array of the query words
	 * @return the list of the sorted search results of the query words
	 * 
	 */
	public List<SearchResult> exactSearch(String[] queryWords) {

		ArrayList<SearchResult> results = new ArrayList<>();
		HashMap<String, SearchResult> resultMap = new HashMap<>();

		for (String word : queryWords) {

			if (map.containsKey(word)) {
				addSearchResults(word, results, resultMap);
			}

		}

		Collections.sort(results);
		return results;

	}

	/**
	 * Adds the search results as SearchResult objects in a map
	 * 
	 * @param word
	 *            The word match
	 * 
	 */
	private void addSearchResults(String word, ArrayList<SearchResult> results,
			HashMap<String, SearchResult> resultMap) {

		TreeMap<String, TreeSet<Integer>> innerMap = map.get(word);

		for (String fileName : innerMap.keySet()) {

			if (resultMap.containsKey(fileName)) {

				int freqAdd = innerMap.get(fileName).size();
				resultMap.get(fileName).setFrequency(freqAdd);
				int first = innerMap.get(fileName).first();

				if (Integer.compare(first, resultMap.get(fileName).getPosition()) < 0) {
					resultMap.get(fileName).setPosition(first);
				}

			}

			else {
				SearchResult newResult = new SearchResult(fileName, innerMap.get(fileName).size(),
						innerMap.get(fileName).first());
				resultMap.put(fileName, newResult);
				results.add(newResult);
			}
		}

	}

	/**
	 * Indexes the additional contents of the Inverted Index to the map
	 * 
	 * @param other
	 * 		InvertedIndex object
	 */
	public void addAll(InvertedIndex other) {
		for (String word : other.map.keySet()) {
			if (this.map.containsKey(word) == false) {
				this.map.put(word, other.map.get(word));
			} else {
				for (String file : other.map.get(word).keySet()) {
					if (!this.map.get(word).containsKey(file)) {
						this.map.get(word).put(file, other.map.get(word).get(file));
					} else {
						this.map.get(word).get(file).addAll(other.map.get(word).get(file));
					}
				}
			}
		}
	}

}
