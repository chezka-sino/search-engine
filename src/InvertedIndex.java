import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class indexes the words in the .txt files in the Treemap
 * 
 * @author Chezka Sino
 *
 */
public class InvertedIndex { 
	// TODO Should really only have data-structure like methods in here
	// TODO add(String word, String path, int position), toString(), size(), containsWord(), etc.
	
	// TODO The file parsing methods should be in a different class; InvertedIndexBuilder
	
	/**
	 * 
	 */
	// TODO final, not static
	private static TreeMap <String, TreeMap <String, TreeSet<Integer>>> fileMap;
	
	// TODO This can be a method parameter?
	private static Path outputFile;
	
	/**
	 * Constructor for the WordDataStructureClass
	 * 
	 * @param index
	 * 		the JSON file where the index would be written
	 * 
	 */
	public InvertedIndex(String index) {
		fileMap = new TreeMap<>();
		InvertedIndex.outputFile = Paths.get(index);
		
	}
	
	/**
	 * Reads the array of the text files 
	 * 
	 * @param files
	 * 		ArrayList of the .txt files
	 * @throws IOException
	 * 
	 */
	public void readArray(ArrayList<String> files) throws IOException {
		for (String name: files) {
			Path inputFile = Paths.get(name);
			openFile(inputFile);
		}
	}

	/**
	 * Reads through the files, puts the words in the treemap then calls the JSON
	 * class to write it into the file
	 * 
	 * @param inputFile
	 * 		the file to be checked
	 * @throws IOException
	 * 
	 */
	public void openFile(Path inputFile) throws IOException{
		
		int count = 1;
		
		try (BufferedReader reader = Files.newBufferedReader(inputFile, 
				Charset.forName("UTF-8"))) {
			
			String line;
			
			while ((line = reader.readLine()) != null) {

				String [] words = line.toLowerCase().split(" ");
				List<String> wordArray = new ArrayList<>();
				
				for (String i:words) {					
					i = stripWords(i);
					
					if (i != null && i.length()>0) {
						wordArray.add(i);
					}								
				}
				
				words = wordArray.toArray(new String[wordArray.size()]);
				String iFile = inputFile.toString();
				count = wordMap(words, iFile, count);
				
				
			}
			
			// TODO This ends up being called several times for each file
			// TODO Only want to do this if the flag is present
			// TODO Move this into a toJSON(Path output) method instead
			JSONWriter writer = new JSONWriter(outputFile, fileMap);
			writer.writeJSON();
		}
	}
	
	/**
	 * Removes characters other than letters and digits
	 * 
	 * @param word
	 * 		word to be stripped
	 * @return
	 * 		word without the other characters
	 * 
	 */
	public String stripWords(String word) {
		
		StringBuilder builder = new StringBuilder();
		
		for (char c: word.toCharArray()) {
			if(Character.isLetterOrDigit(c)) {
				builder.append(c);
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * Put the words mapped to their corresponding .txt files and position in
	 * the file
	 * 
	 * @param words
	 * 		the word
	 * @param inputFile
	 * 		txt file where the word is from
	 * @param count
	 * 		position of the word
	 * @return
	 * 		the position of the current word in the .txt file
	 */
	public int wordMap (String[] words, String inputFile, Integer count) {
		
		for (String w: words) {
			if (!fileMap.containsKey(w)) {
				fileMap.put(w, new TreeMap<String, TreeSet<Integer>>());
			}
			
			if (!fileMap.get(w).containsKey(inputFile.toString())) {
				fileMap.get(w).put(inputFile.toString(), new TreeSet<Integer>());
			}
			fileMap.get(w).get(inputFile.toString()).add(count);
			count++;
		}
		
		return count;

	}
	
	/**
	 * Prints the TreeMap of the wordindex
	 * 
	 */
	public void printMap() {
		System.out.println("Current Map"); 
			
		for (Map.Entry<String, TreeMap<String, TreeSet<Integer>>> wordEntry: fileMap.entrySet()) {
			String word = wordEntry.getKey();
			System.out.println("WORD: " + word);
			
			for (Map.Entry<String, TreeSet<Integer>> fileEntry: wordEntry.getValue().entrySet()) {
				String file = fileEntry.getKey();
				System.out.println(".TXT FILE: " + file);
				System.out.println("POSITIONS: " );
				for (Integer i: fileEntry.getValue()) {
					System.out.println(i);
				}
			}
			System.out.println();
		}
		
	}
	
	// TODO Remove this; breaking encapsulation
	/**
	 * 
	 * @return
	 * 		the TreeMap of the wordindex
	 * 
	 */
	public static Map<String, TreeMap<String, TreeSet<Integer>>> getFileMap() {
		return fileMap;
	}

}
