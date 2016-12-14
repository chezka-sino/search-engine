import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public interface WebCrawlerInterface {
	
	public void addSeed(String seed, ThreadSafeInvertedIndex index) 
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException;
	
	public void getURLs(String seed, String text) 
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException;
	
	/**
	 * Indexes the words in the HTML
	 * 
	 * @param url
	 *            URL where the word was found
	 * @param words
	 *            array of words to be indexed
	 * @param toIndex
	 *            InvertedIndex object
	 * 
	 */
	default void htmlToIndex(String url, String[] words, InvertedIndex toIndex) {
		int count = 1;

		for (String i : words) {
			i = i.replaceAll("\\p{Punct}+", "");
			if (!i.isEmpty()) {

				toIndex.add(i, url, count);
				count++;

			}

		}
	}

}
