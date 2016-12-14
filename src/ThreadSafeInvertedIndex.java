import java.io.IOException;
import java.util.List;

/**
 * Thread-safe version of the InvertedIndex
 * 
 * @see InvertedIndex
 *
 */
public class ThreadSafeInvertedIndex extends InvertedIndex {

	private ReadWriteLock lock;

	public ThreadSafeInvertedIndex() {
		super();
		lock = new ReadWriteLock();
	}

	// TODO When you switch to a lock, need to remove the synchronized keyword
	
	@Override
	public void add(String word, String path, int position) {
		lock.lockReadWrite();

		try {
			super.add(word, path, position);
		} finally {
			lock.unlockReadWrite();
		}

	}

	@Override
	public void toJSON(String index) throws IOException {
		lock.lockReadOnly();
		try {
			super.toJSON(index);
		} finally {
			lock.unlockReadWrite();
		}
	}

	@Override
	public int numWords() {
		lock.lockReadOnly();
		try {
			return super.numWords();
		} finally {
			lock.lockReadOnly();
		}
	}

	@Override
	public int numLocations(String word) {
		lock.lockReadOnly();
		try {
			return super.numLocations(word);
		} finally {
			lock.lockReadOnly();
		}
	}

	@Override
	public int firstIndex(String word, String fileName) {
		lock.lockReadOnly();
		try {
			return super.firstIndex(word, fileName);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public boolean containsWord(String word) {
		lock.lockReadOnly();
		try {
			return super.containsWord(word);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public boolean containsLocation(String word, String textFile) {
		lock.lockReadOnly();
		try {
			return super.containsLocation(word, textFile);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public List<String> getWords() {
		lock.lockReadOnly();
		try {
			return super.getWords();
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public List<SearchResult> partialSearch(String[] queryWords) {
		lock.lockReadOnly();
		try {
			return super.partialSearch(queryWords);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public List<SearchResult> exactSearch(String[] queryWords) {
		lock.lockReadOnly();
		try {
			return super.exactSearch(queryWords);
		} finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public void addAll(InvertedIndex other) {
		lock.lockReadWrite();
		try {
			super.addAll(other);
		} finally {
			lock.unlockReadWrite();
		}
	}

}
