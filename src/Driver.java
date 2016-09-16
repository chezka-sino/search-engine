import java.util.Arrays;

public class Driver { 
	
    public static void main(String[] args) {
        // TODO Modify as necessary!
        System.out.println(Arrays.toString(args));
        
        String [] argsArray = new String[4];
        
        for (int i = 0; i<4; i++) {
        	argsArray[i] = args[i];
        }
        
        for (int j = 0; j < 4; j++) {
        	if (argsArray[j].equals("-dir") | argsArray[j].equals("-index")) {
        		// TODO if dir then the next one is the directory to be traversed
        		// no dir = error
        		// if index the it's the json file name.
        		// no filename then default index-simple.json
        		// no -index, still execute but no output file
        	}
        }
        
        IndexTest test = new IndexTest();
    }
}
