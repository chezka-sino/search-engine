import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Thread-safe version of the InvertedIndex
 * 
 * @see InvertedIndex
 *
 */
public class ThreadSafeInvertedIndex extends InvertedIndex {
	
	// TODO Need to use the ReadWriteLock in this class

	public ThreadSafeInvertedIndex() {
		super();
	}

	@Override
	public synchronized void add(String word, String path, int position) {
		super.add(word, path, position);

	}

	@Override
	public synchronized void toJSON(String index) throws IOException {
		super.toJSON(index);
	}

	//
	@Override
	public synchronized int numWords() {
		return super.numWords();
	}

	//
	@Override
	public synchronized int numLocations(String word) {
		return super.numLocations(word);
	}

	//
	@Override
	public synchronized int firstIndex(String word, String fileName) {
		return super.firstIndex(word, fileName);
	}

	//
	@Override
	public synchronized boolean containsWord(String word) {
		return super.containsWord(word);
	}

	//
	@Override
	public synchronized boolean containsLocation(String word, String textFile) {
		return super.containsLocation(word, textFile);
	}

	//
	@Override
	public synchronized List<String> getWords() {
		return super.getWords();
	}

	//
	@Override
	public synchronized List<SearchResult> partialSearch(String[] queryWords) {
		return super.partialSearch(queryWords);
	}

	//
	@Override
	public synchronized List<SearchResult> exactSearch(String[] queryWords) {
		return super.exactSearch(queryWords);
	}

	//
	@Override
	public synchronized void addSearchResults(String word, ArrayList<SearchResult> results,
			HashMap<String, SearchResult> resultMap) {
		super.addSearchResults(word, results, resultMap);
	}

}
