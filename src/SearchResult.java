

//public class SearchResult {} 
/*implements Comparable<SearchResult> {

	private final String location;
	private int frequency;
	private int position;
	
	public void addFrequency(int frequency)
	public void updatePosition(int position)
}*/

public class SearchResult implements Comparable<SearchResult> {
	
	private final String location;
	private int frequency;
	private int position;
	
	public SearchResult(String location, int frequency, int position) {
		this.location = location;
		this.frequency = frequency;
		this.position = position;
	}
	
	public void setFrequency(int frequency) {
		this.frequency += frequency;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getLocation() {
		return location;
	}

	public int getFrequency() {
		return frequency;
	}

	public int getPosition() {
		return position;
	}

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