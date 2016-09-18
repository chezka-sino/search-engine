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

public class WordDataStructure {
	
	private static TreeMap <String, TreeMap <String, TreeSet<Integer>>> fileMap;
	private static Path outputFile;
	
	public WordDataStructure(String index) {
		fileMap = new TreeMap<>();
		WordDataStructure.outputFile = Paths.get(index);
		
	}
	
	public void readArray(ArrayList<String> files) throws IOException {
		for (String name: files) {
			Path inputFile = Paths.get(name);
			openFile(inputFile);
		}
	}

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
			
			JSONWriter writer = new JSONWriter(outputFile, fileMap);
			writer.writeJSON();
		}
	}
	
	public String stripWords(String word) {
		
		StringBuilder builder = new StringBuilder();
		
		for (char c: word.toCharArray()) {
			if(Character.isLetterOrDigit(c)) {
				builder.append(c);
			}
		}
		
		return builder.toString();
	}
	
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
	
	public static Map<String, TreeMap<String, TreeSet<Integer>>> getFileMap() {
		return fileMap;
	}

}
