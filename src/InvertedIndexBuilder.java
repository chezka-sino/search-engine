import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO This should be a class that "does" stuff and doesn't have members and only static methods

/**
 * This class looks into the .txt files, calls the InvertedIndex class to add the words
 * in a TreeMap then calls the JSONWriter to write the Inverted Index to a "pretty"
 * JSON format
 * 
 * @see JSONWriter
 * @see InvertedIndex
 * 
 * @author Chezka Sino
 *
 */
public class InvertedIndexBuilder {
	
	//The ArrayList of the textFiles
//	private final ArrayList<String> textFiles;
	
	/**
	 * Class constructor
	 * 
	 * @param files
	 * 				Array list of .txt files
	 * 
	 */
//	public InvertedIndexBuilder(ArrayList<String> files) {
//		textFiles = files;
//	}
	
	/**
	 * Goes through the list of .txt files
	 * 
	 * @param index
	 * 				The output file for the JSONfile
	 * 
	 * @see InvertedIndex
	 * 
	 * @throws IOException
	 * 
	 */
	public static void readArray(ArrayList<String> textFiles, String index) throws IOException {
		InvertedIndex toIndex = new InvertedIndex();
		
		for (String name: textFiles) {
			Path inputFile = Paths.get(name);
			openFile(inputFile);
		}
		InvertedIndex.toJSON(index);
	}
	
	/**
	 * Removes characters other than letters and digits
	 * 
	 * @param word
	 * 				word to be stripped
	 * @return
	 * 				word without the other characters
	 * 
	 */
	public static String stripWords(String word) {
		
		StringBuilder builder = new StringBuilder();
		
		for (char c: word.toCharArray()) {
			if(Character.isLetterOrDigit(c)) {
				builder.append(c);
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * Reads through the files, puts the words in the treemap then calls the JSON
	 * class to write it into the file
	 * 
	 * @param inputFile
	 * 				the file to be checked
	 * @param toIndex
	 * 				InvertedIndex object
	 * @throws IOException
	 * 
	 */
	public static void openFile(Path inputFile) throws IOException {
		
		InvertedIndex toIndex = new InvertedIndex();
		
		int count = 1;
		
		BufferedReader reader = Files.newBufferedReader(inputFile, 
				Charset.forName("UTF-8"));
		
		String line;
		
		while ((line = reader.readLine()) != null) {
			
			String [] words = line.toLowerCase().split(" ");
			
			for (String i:words) {
				i = stripWords(i);
				
				if (i != null && i.length()>0) {

					toIndex.add(i, inputFile.toString(), count);
					count++;
				}
			}
		}
	}
	

}
