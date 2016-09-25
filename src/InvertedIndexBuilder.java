import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndexBuilder {
	
	private final ArrayList<String> textFiles;
	private final TreeMap <String, TreeMap <String, TreeSet<Integer>>> fileMap;
//	private static Path outputFile;
//	private static InvertedIndexV2 toIndex;
	
	public InvertedIndexBuilder(ArrayList<String> files) {
		// TODO Auto-generated constructor stub
		textFiles = files;
//		outputFile = Paths.get(index);
		fileMap = new TreeMap<>();
//		toIndex = new InvertedIndexV2(index);
	}
	
	public void readArray(String index) throws IOException {
		InvertedIndex toIndexV2 = new InvertedIndex(index);
		
		for (String name: textFiles) {
			Path inputFile = Paths.get(name);
			openFile(inputFile, toIndexV2);
//			toIndexV2.toJSON();
		}
		toIndexV2.toJSON();
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
	
	public void openFile(Path inputFile, InvertedIndex toIndexV2) throws IOException {
//		InvertedIndexV2 toIndex = new InvertedIndexV2(outputFile.toString());
		
		int count = 1;
		
		BufferedReader reader = Files.newBufferedReader(inputFile, 
				Charset.forName("UTF-8"));
		
		String line;
		
		while ((line = reader.readLine()) != null) {
			
			String [] words = line.toLowerCase().split(" ");
//			List<String> wordArray = new ArrayList<>();
			
			for (String i:words) {
				i = stripWords(i);
				
				if (i != null && i.length()>0) {
//					wordArray.add(i);
					toIndexV2.add(i, inputFile.toString(), count);
					count++;
				}
				
//				toIndex.add(i, inputFile.toString(), count);
//				count++;
				
			}
		}

	}
	
//	public void toInvertedIndex() {
//		InvertedIndex invIndex = new InvertedIndex(index);
//		
//	}
	
	public void toJSON(Path output) throws IOException {
		JSONWriter writer = new JSONWriter(output,fileMap);
		writer.writeJSON();
	}
	
//	public void toTreeMap ()
	

}
