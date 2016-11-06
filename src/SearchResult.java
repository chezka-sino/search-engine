/**
 * This class creates SearchResult objects
 * 
 * @author shinheera
 *
 */
public class SearchResult implements Comparable<SearchResult> {
	
	private final String location;
	private int frequency;
	private int position;
	
	/**
	 * Class constructor
	 * 
	 * @param location
	 * 		filename of the search result
	 * @param frequency
	 * 		frequency of the word in the file
	 * @param position
	 * 		first index of the word in the file
	 * 
	 */
	public SearchResult(String location, int frequency, int position) {
		this.location = location;
		this.frequency = frequency;
		this.position = position;
	}
	
	/**
	 * Updates the frequency of the SearchResult by adding to the previous frequency
	 * 
	 * @param frequency
	 * 		additional number of positions in the file
	 * 
	 */
	public void setFrequency(int frequency) {
		this.frequency += frequency;
	}

	/**
	 * Updates the first index of the word in the file
	 *  
	 * @param position
	 * 		new first index of the word
	 * 
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Returns the filename of the object
	 * 
	 * @return
	 * 		the filename of the object
	 * 
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Returns the frequency of occurrences of the word in the file
	 * 
	 * @return
	 * 		the frequency of occurrences of the word in the file
	 * 
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Returns the first index of the word in the file
	 * 
	 * @return
	 * 		the first index of the word in the file
	 * 
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * The custom compare method used to sort the SearchResults using the following heirarchy:
	 * 1. Frequency - higher frequency appears before lower frequency
	 * 2. Initial position - if frequency is equal, the earlier index position appears before the later index position
	 * 3. File name - if the above criteria are both equal, the file name is compared alphabetically
	 * 
	 */
	@Override
	public int compareTo(SearchResult o) {
		if (Integer.compare(this.frequency, o.frequency) != 0) {
			return -(Integer.compare(this.frequency, o.frequency));
		}
		
		if (Integer.compare(this.position, o.position) != 0) {
			return Integer.compare(this.position, o.position);
		}
		
		return this.location.compareTo(o.location);
		
	}
	
}