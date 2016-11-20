import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO WebCrawler
public class URLParser {

	// TODO Reduce the total number of groups, don't include the quote in the capturing group
	public static final String REGEX = "\\s*(?i)<a([^>]+)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	public static final int GROUP = 2;

	/** Port used by socket. For web servers, should be port 80. */
	public static final int DEFAULT_PORT = 80;

	/** Version of HTTP used and supported. */
	public static final String version = "HTTP/1.1";

	// See: http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1.1
	/** Valid HTTP method types. */
	public static enum HTTP {
		OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
	};

	// TODO Make private
	// Set of all the URLs parsed
	public final HashSet<String> links;
	// Queue of the URLs to be processed
	public final Queue<String> queue;

	/**
	 * Class constructor Initializes the set and queue
	 */
	public URLParser() {
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
	public void urlList(String seed, InvertedIndex index)
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException {

		links.addAll(getURLs(seed));
		queue.addAll(getURLs(seed));
		
		// TODO instead...
		// links.add(seed), queue.add(seed)

		while (!queue.isEmpty()) {
			String url = queue.remove();
			
			// TODO fetch every page once, pass around the HTML as necessary
			String htmlFile = fetchHTML(url);
			String[] cleanedHTML = HTMLCleaner.fetchWords(htmlFile);
			InvertedIndexBuilder.openHTML(url, cleanedHTML, index);
			getURLs(url);
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
	public HashSet<String> getURLs(String seed) // TODO Take in the HTML instead?
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException {

		String text = fetchHTML(seed);
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(text);

		links.add(seed); // TODO Not here
		URL base = new URL(seed);
		int count = links.size();

		while (m.find() && count <= 49) { // TODO instead of count you call links.size() in the while

			// TODO Shouldn't need a replaceAll, if you do we need to fix the REGEX
			URL absolute = new URL(base, m.group(GROUP).replaceAll("\"", ""));
			URI cleanURL = new URI(absolute.getProtocol(), absolute.getAuthority(), absolute.getPath(),
					absolute.getQuery(), null);
			
			// TODO Save the toString() representation of the cleanURL so you don't need to keep recalling it
			
			if (!links.contains(cleanURL.toString()) && !cleanURL.equals(seed)) { // TODO can remove the cleanURL.equals(seed) part
				links.add(cleanURL.toString());
				queue.add(cleanURL.toString());
				count++;
			}

		}

		return links;

	}

	
	
	// TODO Might still make sense to have a seperate HTTPFetcher class
	/**
	 * Crafts a minimal HTTP/1.1 request for the provided method.
	 *
	 * @param url
	 *            url to fetch
	 * @param type
	 *            HTTP method to use
	 *
	 * @return HTTP/1.1 request
	 * 
	 */
	public static String craftHTTPRequest(URL url, HTTP type) {
		String host = url.getHost();
		String resource = url.getFile().isEmpty() ? "/" : url.getFile();

		return String.format("%s %s %s\n" + "Host: %s\n" + "Connection: close\n" + "\r\n", type.name(), resource,
				version, host);
	}

	/**
	 * Will connect to the web server and fetch the URL using the HTTP request
	 * provided. It would be more efficient to operate on each line as returned
	 * instead of storing the entire result as a list.
	 *
	 * @param url
	 *            - url to fetch
	 * @param request
	 *            - full HTTP request
	 *
	 * @return the lines read from the web server
	 *
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static List<String> fetchLines(URL url, String request)
			throws UnknownHostException, IOException, MalformedURLException {

		ArrayList<String> lines = new ArrayList<>();
		int port = url.getPort() < 0 ? DEFAULT_PORT : url.getPort();

		try (Socket socket = new Socket(url.getHost(), port);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				PrintWriter writer = new PrintWriter(socket.getOutputStream());) {
			writer.println(request);
			writer.flush();

			String line = null;

			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		}

		return lines;

	}

	/**
	 * Fetches the HTML for the specified URL (without headers).
	 *
	 * @param url
	 *            - url to fetch
	 * @return HTML as a single {@link String}, or null if not HTML
	 *
	 * @throws UnknownHostException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String fetchHTML(String url) throws UnknownHostException, IOException, MalformedURLException {

		URL seed = new URL(url);
		String request = craftHTTPRequest(seed, HTTP.GET);
		List<String> lines = fetchLines(seed, request);

		int start = 0;
		int end = lines.size();

		// Determines start of HTML versus headers.
		while (!lines.get(start).trim().isEmpty() && start < end) {
			start++;
		}

		// Double-check this is an HTML file.
		Map<String, String> fields = parseHeaders(lines.subList(0, start + 1));
		String type = fields.get("Content-Type");

		if (type != null && type.toLowerCase().contains("html")) {

			return String.join(System.lineSeparator(), lines.subList(start + 1, end));
		}

		return null;

	}

	/**
	 * Helper method that parses HTTP headers into a map where the key is the
	 * field name and the value is the field value. The status code will be
	 * stored under the key "Status".
	 *
	 * @param headers
	 *            - HTTP/1.1 header lines
	 * @return field names mapped to values if the headers are properly
	 *         formatted
	 */
	public static Map<String, String> parseHeaders(List<String> headers) {
		Map<String, String> fields = new HashMap<>();

		if (headers.size() > 0 && headers.get(0).startsWith(version)) {
			fields.put("Status", headers.get(0).substring(version.length()).trim());

			for (String line : headers.subList(1, headers.size())) {
				String[] pair = line.split(":", 2);

				if (pair.length == 2) {
					fields.put(pair[0].trim(), pair[1].trim());
				}
			}
		}

		return fields;
	}

}
