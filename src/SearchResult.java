import java.util.Comparator;

public class SearchResult {
	
	public static final Comparator<Integer> FREQUENCY_ORDER = new Comparator<Integer>() {

		@Override
		public int compare(Integer o1, Integer o2) {
			return Integer.compare(o1, o2);
		}
		
	};
	
	public static final Comparator<Integer> POSITION_ORDER = new Comparator<Integer>() {

		@Override
		public int compare(Integer o1, Integer o2) {
			return Integer.compare(o1, o2);
		}
	};
	
//	public static final Comparator<String> LOCATION_ORDER = new Comparator<String>() {
//	};

}
