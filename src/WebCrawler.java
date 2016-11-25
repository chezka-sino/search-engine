import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO In general, its only okay to make things less general if they improve efficiency

public class WebCrawler {
	
	public static final String REGEX = "<a([^>]+)href\\s*=\\s*\"([^\"]*)\"";
	public static final int GROUP = 2;
	
	// Set of all the URLs parsed
	private final HashSet<String> links;
	// Queue of the URLs to be processed
	private final Queue<String> queue;

	/**
	 * Class constructor Initializes the set and queue
	 */
	public WebCrawler() {
		links = new HashSet<String>();
		queue = new LinkedList<>();
	}

	/**
	 * Parses the html of the seed to look for URLs Indexes the words found in
	 * the paage
	 * 
	 * @param seed
	 *            URL string
	 * @param index
	 *            Inverted Index object
	 * 
	 * @throws UnknownHostException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 */
	public void urlList(String seed, InvertedIndex index) // TODO refactor addSeed()
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException {
		
		links.add(seed);
		queue.add(seed);

		while (!queue.isEmpty()) {
			String url = queue.remove();
			
			String htmlFile = HTTPFetcher.fetchHTML(url);
			String[] cleanedHTML = HTMLCleaner.fetchWords(htmlFile);
			InvertedIndexBuilder.openHTML(url, cleanedHTML, index);
			getURLs(url, htmlFile);
		}

	}

	/**
	 * Returns the list of URLs found in the page
	 * 
	 * @param seed
	 *            String of the URLs
	 * @return Set of URLs
	 * 
	 * @throws UnknownHostException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 */
	public HashSet<String> getURLs(String seed, String text) // TODO private and void
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException {

		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(text);

		URL base = new URL(seed);

		while (m.find() && links.size() <= 49) { 
			
			URL absolute = new URL(base, m.group(GROUP));
			URI cleanURL = new URI(absolute.getProtocol(), absolute.getAuthority(), absolute.getPath(),
					absolute.getQuery(), null);
			String urlString = cleanURL.toString();
			
			if (!links.contains(urlString)) {
				links.add(urlString);
				queue.add(urlString);
			}

		}

		return links;

	}

}
