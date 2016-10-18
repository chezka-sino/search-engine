import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.print.attribute.standard.MediaPrintableArea;

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

	/**
	 * Class constructor
	 */
	public InvertedIndex() {
		map = new TreeMap<>();
		partialSearchMap = new TreeMap<>();
		exactSearchMap = new TreeMap<>();
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

	/*
	 * Project 2
	 * 
	 * public List<SearchResult> exactSearch(String[] queryWords) {
	 * 
	 * loop through the query words get the matching key and inner map loop
	 * through the files calculate search results
	 * 
	 * sort results return results }
	 */
	
	public void partial(String queryLine) {
		
		queryLine = queryLine.trim();
		String [] words = queryLine.split("\\s+");
		partialSearchMap.put(queryLine, partialSearch(words));
		
//		for(String line: exactSearchMap.keySet()) {
//			System.out.println("QUERY LINE PARTIAL: " + line);
//			for (String filename: partialSearchMap.get(line).keySet()) {
//				System.out.println("FILE: " + filename);
//				System.out.println("FREQUENCY: " + partialSearchMap.get(line).get(filename).size());
//			}
//		}
//		
//		System.out.println();
//		System.out.println("ENDED PARTIAL SEARCH");
		
	}

	//TODO ERROR WAS HERE!
	public HashMap<String, TreeSet<Integer>> partialSearch(String[] queryWords) {

		List<String> words = new ArrayList<>();
		words.addAll(map.keySet());
//		List<String> wordMatches = new ArrayList<>();
		List<String> fileResult = new ArrayList<>();
		HashMap<String, TreeSet<Integer>> fileMatches = new HashMap<>();
		
		for (String word: queryWords) {

			for (String match: map.tailMap(word).keySet()) {
				
//				System.out.println(map.get(match).keySet().toString());
				
			}		
		}
		
		return fileMatches;

	}
		//TODO Sort
	
	public void exact(String queryLine) {
		
		System.out.println("STARTED EXACT SEARCH");
		
		queryLine = queryLine.trim();
		String [] words = queryLine.split("\\s+");
		exactSearchMap.put(queryLine, exactSearch(words));
		
		for(String line: exactSearchMap.keySet()) {
			System.out.println("QUERY LINE EXACT: " + line);
			for (String filename: exactSearchMap.get(line).keySet()) {
				System.out.println("FILE: " + filename);
				System.out.println("FREQUENCY: " + exactSearchMap.get(line).get(filename).size());
			}
		}
		
		System.out.println();
		System.out.println("ENDED EXACT SEARCH");
		
		sortSearchResult(exactSearchMap);
		
	}
	
	public HashMap<String, TreeSet<Integer>> exactSearch(String[] queryWords) {

		List<String> words = new ArrayList<>();
		words.addAll(map.keySet());
		List<String> fileResult = new ArrayList<>();
		HashMap<String, TreeSet<Integer>> fileMatches = new HashMap<>();
		
		for (String word: queryWords) {

			for (String match: words) {
				
				if (match.equals(word)) {
					
					fileResult.addAll(map.get(match).keySet());
					for (String filename: fileResult) {
						fileMatches.put(filename, new TreeSet<>());
						fileMatches.get(filename).addAll(map.get(match).get(filename));
					}
										
				}
			}		
		}
		
		return fileMatches;

	}
	
	public void sortSearchResult (TreeMap<String, HashMap<String, TreeSet<Integer>>> unsortedMap) {
		
	}

}
