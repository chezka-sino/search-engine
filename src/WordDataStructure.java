import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordDataStructure {
	
	private static Map <String, HashMap <String, ArrayList <Integer>>> fileMap;
	private static Path inputFile;
	private static String txtFile;
	
	public WordDataStructure(String txtFile) {
		// TODO Auto-generated constructor stub
		fileMap = new HashMap<>();
		this.inputFile = Paths.get(txtFile);
		this.txtFile = txtFile;
	}

	public void openFile() throws IOException{
		try (BufferedReader reader = Files.newBufferedReader(inputFile)) {
			
			String line = null;
			
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
//				System.out.println("Word Array SIZE: " + words.length);
				wordMap(words);
				
			}
			
			printMap();
		}
	}
	
	public String stripWords(String word) {
		
		word = word.replaceAll("[^\\p{Alpha}\\p{Digit}]+","");
		return word;
	}
	
	public void wordMap (String[] words) {
//		System.out.println(words);
		
//		for (String i:words) {
//			System.out.println(i);
//		}
		
		System.out.println("Word Array SIZE: " + words.length);
		System.out.println("Initial Map EMPTY: " + fileMap.isEmpty());	
//		for (int i = 0; i>words.length;i++) {
//			System.out.println(words[i]);
//		}
		
		int i = 0;
		
		for (String w: words) {
			System.out.println(w);
			if (!fileMap.containsKey(w)) {
				System.out.println("doesn't contain " + w);
				fileMap.put(w, new HashMap<String, ArrayList<Integer>>());
			}
			
			if (!fileMap.get(w).containsKey(txtFile)) {
				fileMap.get(w).put(txtFile, new ArrayList<Integer>());
			}
			fileMap.get(w).get(txtFile).add(i);
			i++;
		}
		
		System.out.println("New Map EMPTY: " + fileMap.isEmpty());
		System.out.println();

	}
	
	public void printMap() {
		System.out.println("Current Map"); 
//		System.out.println(fileMap.isEmpty());
//				
		
		
//		for (Map.Entry<String, HashMap<String, ArrayList<Integer>>> entry: fileMap.entrySet()) {
//			System.out.println("WORD: " + entry.getKey());
//			for (Map.Entry<String, ArrayList<Integer>> entry2: fileMap.get(entry).entrySet()) {
//				System.out.println(".TXT FILE: " + entry2.getKey());
//				System.out.println("POSITIONS: ");
//				for (Integer c: fileMap.get(entry).get(entry2)) {
//					System.out.println(c);
//				}
//			}
//		}
	}

}
