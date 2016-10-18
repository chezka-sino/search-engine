import java.nio.file.Path;
import java.util.Comparator;

public class SearchResult implements Comparable {

//	public static final Comparator<Integer> FREQUENCY_ORDER = new Comparator<Integer>() {
//
//		@Override
//		public int compare(Integer o1, Integer o2) {
//			return Integer.compare(o1, o2);
//		}
//
//	};
//
//	public static final Comparator<Integer> POSITION_ORDER = new Comparator<Integer>() {
//
//		@Override
//		public int compare(Integer o1, Integer o2) {
//			return Integer.compare(o1, o2);
//		}
//	};
//
//	public static final Comparator<Path> LOCATION_ORDER = new Comparator<Path>() {
//
//		@Override
//		public int compare(Path o1, Path o2) {
//			// TODO Auto-generated method stub
//			Path name1;
//			Path name2;
//
//			name1 = o1.toAbsolutePath().normalize().getFileName();
//			name2 = o2.toAbsolutePath().normalize().getFileName();
//
//			if (name1.equals(name2)) {
//				return o1.compareTo(o2);
//			}
//
//			return name1.compareTo(name2);
//
//		}
//
//	};

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		
		return 0;
	}
	
}
