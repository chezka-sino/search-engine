import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedWebCrawler implements WebCrawlerInterface<ThreadSafeInvertedIndex> {

	public static final String REGEX = "<a([^>]+)href\\s*=\\s*\"([^\"]*)\"";
	public static final int GROUP = 2;

	// Set of all the URLs parsed
	private final HashSet<String> links;
	// Queue of the URLs to be processed
	
	// TODO All access to links must be synchronized

	private static final Logger LOGGER = LogManager.getLogger();
	private final WorkQueue minions;
	private final ThreadSafeInvertedIndex index;

	/**
	 * Class constructor Initializes the set and queue
	 */
	public MultithreadedWebCrawler(WorkQueue queue, ThreadSafeInvertedIndex index) {
		links = new HashSet<String>();
		minions = queue; // TODO Pass in the queue not the number of threads/ new WorkQueue(threads);
		this.index = index;
	}

	/**
	 * Parses the html of the seed to look for URLs Indexes the words found in
	 * the page
	 * 
	 * @param seed
	 *            URL string
	 * @param index
	 *            ThreadSafeInverted Index object
	 * 
	 * @throws UnknownHostException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 */

	public void addSeed(String seed, ThreadSafeInvertedIndex index)
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException {

		links.add(seed);
		minions.execute(new urlMinion(seed));
		minions.finish();

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
	public void getURLs(String seed, String text)
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
				minions.execute(new urlMinion(urlString));
			}

		}

	}
	
	/**
	 * Runnable class that implements the multithreaded web crawler
	 *
	 */
	private class urlMinion implements Runnable {
		
		private String url;

		public urlMinion(String url) {
			LOGGER.debug("Minion created for {}", url);
			this.url = url;
		}

		@Override
		public void run() {
			String htmlFile;
			try {
				htmlFile = HTTPFetcher.fetchHTML(url);
				String[] cleanedHTML = HTMLCleaner.fetchWords(htmlFile);
				htmlToIndex(url, cleanedHTML, index); // TODO Use a local inverted index here too!
				getURLs(url, htmlFile);
			} catch (IOException e) {
				LOGGER.warn("IOException on {}", url);
			} catch (URISyntaxException e) {
				LOGGER.warn("URISyntaxException on {}", url);
			}

			LOGGER.debug("Minion indexed {}", url);
		}
		
		
		
	}

}
