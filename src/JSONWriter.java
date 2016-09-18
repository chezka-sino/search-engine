import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class JSONWriter {
	
	public static final char TAB = '\t';
	public static final char END = '\n';
	private static TreeMap <String, TreeMap <String, TreeSet<Integer>>> fileMap;
	private static Path outputFile;

	
	public JSONWriter(Path outputFile, TreeMap <String, TreeMap <String, TreeSet<Integer>
		>> fileMap) {
		JSONWriter.outputFile = outputFile;
		JSONWriter.fileMap = fileMap;
	}
	
	public static String quote(String text) {
        return String.format("\"%s\"", text);
    }
	
	public static String tab(int n) {
        char[] tabs = new char[n];
        Arrays.fill(tabs, TAB);
        return String.valueOf(tabs);
    }
	
	public static String writeStrings (String s, int n) {
    	
    	String tabStr = tab(n) + quote(s);
    	return tabStr;
    	
    }
	
	public static String writeIntegers(Integer i, int n) {
    	
    	String tabInt = tab(n) + i.toString();
    	return tabInt;
    	
    }
	
	public void writeJSON() throws IOException {
		
		try (BufferedWriter writer = Files.newBufferedWriter(outputFile,
				Charset.forName("UTF-8"))) {
			
			Set<String> words = fileMap.keySet();
			Iterator<String> itr = words.iterator();
			writer.write("{" + END);
			
			while (itr.hasNext()) {
				
				String current = itr.next();
				writer.write(writeStrings(current, 1) + ": {" + END);
				
				Set<String> fileName = fileMap.get(current).keySet();
				Iterator<String> itr2 = fileName.iterator();
				
				while (itr2.hasNext()) {
					
					String current2 = itr2.next();
					writer.write(writeStrings(current2, 2) + ": [" + END);
					
					Set<Integer> positions = fileMap.get(current).get(current2);
					Iterator<Integer> itr3 = positions.iterator();
					
					while (itr3.hasNext()) {
						
						Integer current3 = itr3.next();
						writer.write(writeIntegers(current3, 3));
						
						if (itr3.hasNext()) {
							writer.write(",");
						}
						
						writer.write(END);
						
					}
					
					writer.write(tab(2) + "]");
					
					if (itr2.hasNext()) {
						writer.write(",");
					
					}
				
					writer.write(END);
					
				}
				
				writer.write(TAB + "}");
				
				if (itr.hasNext()) {
					writer.write(",");
				}
			
				writer.write(END);
				
			}
			
			writer.write("}" +END);
			
			
		}
		
	}

}
