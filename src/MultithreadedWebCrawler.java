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

public class MultithreadedWebCrawler {

	public static final String REGEX = "<a([^>]+)href\\s*=\\s*\"([^\"]*)\"";
	public static final int GROUP = 2;

	// Set of all the URLs parsed
	private final HashSet<String> links;
	// Queue of the URLs to be processed

	private static final Logger LOGGER = LogManager.getLogger();
	
	private final WorkQueue minions;
	private static ThreadSafeInvertedIndex index;

	/**
	 * Class constructor Initializes the set and queue
	 */
	public MultithreadedWebCrawler(int threads, ThreadSafeInvertedIndex index) {
		links = new HashSet<String>();
		minions = new WorkQueue(threads);
		MultithreadedWebCrawler.index = index;
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
	public void addSeed(String seed)
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException {

		// WorkQueue minions = new WorkQueue(threads);
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
	private void getURLs(String seed, String text)
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
				WebCrawler.htmlToIndex(url, cleanedHTML, index);
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
