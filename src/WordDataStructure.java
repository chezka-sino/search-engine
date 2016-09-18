import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WordDataStructure {
	
	private static TreeMap <String, TreeMap <String, HashSet<Integer>>> fileMap;
	private static String txtFile;
	private static Path outputFile;
	
//	public WordDataStructure(String txtFile) {
//		// TODO Auto-generated constructor stub
//		fileMap = new HashMap<>();
//		this.inputFile = Paths.get(txtFile);
//		this.txtFile = txtFile;
//	}
	
	public WordDataStructure(String index) {
		fileMap = new TreeMap<>();
//		this.inputFile = Paths.get(txtFile);
//		this.txtFiles = txtFiles;
		this.outputFile = Paths.get(index);
		
	}
	
	public void readArray(ArrayList<String> files) throws IOException {
		for (String name: files) {
			Path inputFile = Paths.get(name);
			openFile(inputFile);
		}
	}

	public void openFile(Path inputFile) throws IOException{
		
		
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
				String iFile = inputFile.toString();
				wordMap(words, iFile);
				
			}
			
//			printMap();
			JSONWriter writer = new JSONWriter(outputFile, fileMap);
			writer.writeJSON();
		}
	}
	
	public String stripWords(String word) {
		
		word = word.replaceAll("[^\\p{Alpha}\\p{Digit}]+","");
		return word;
	}
	
	public void wordMap (String[] words, String inputFile) {
		
//		System.out.println("Word Array SIZE: " + words.length);
//		System.out.println("Initial Map EMPTY: " + fileMap.isEmpty());	
//		
		int i = 1;
		
		for (String w: words) {
			System.out.println(w);
			if (!fileMap.containsKey(w)) {
				System.out.println("doesn't contain " + w);
				fileMap.put(w, new TreeMap<String, HashSet<Integer>>());
			}
			
			if (!fileMap.get(w).containsKey(inputFile.toString())) {
				fileMap.get(w).put(inputFile.toString(), new HashSet<Integer>());
			}
			fileMap.get(w).get(inputFile.toString()).add(i);
			System.out.println("COUNT: " + i);
			i++;
		}
		
//		System.out.println("New Map EMPTY: " + fileMap.isEmpty());
		System.out.println();

	}
	
	public void printMap() {
		System.out.println("Current Map"); 
			
		for (Map.Entry<String, TreeMap<String, HashSet<Integer>>> wordEntry: fileMap.entrySet()) {
			String word = wordEntry.getKey();
			System.out.println("WORD: " + word);
			
			for (Map.Entry<String, HashSet<Integer>> fileEntry: wordEntry.getValue().entrySet()) {
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
	
	public static Map<String, TreeMap<String, HashSet<Integer>>> getFileMap() {
		return fileMap;
	}

}
