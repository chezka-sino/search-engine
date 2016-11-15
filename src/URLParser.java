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

public class URLParser {

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

	public static HashSet<String> links;
	public static Queue<String> queue;

	public URLParser() {
		links = new HashSet<String>();
		queue = new LinkedList<>();
	}

	// TODO
	// if not 50 links add to queue and set
	// while q is not empty

	public HashSet<String> urlList(String seed)
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException {

		links.addAll(getURLs(seed));
		queue.addAll(getURLs(seed));

		while (!queue.isEmpty()) {
			String url = queue.remove();
			getURLs(url);
		}

		return links;
	}

	public HashSet<String> getURLs(String seed)
			throws UnknownHostException, MalformedURLException, IOException, URISyntaxException {

		String text = fetchHTML(seed);
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(text);

		links.add(seed);
		URL base = new URL(seed);
		int count = links.size();

		while (m.find() && count <= 49) {

			URL absolute = new URL(base, m.group(GROUP).replaceAll("\"", ""));
			URI cleanURL = new URI(absolute.getProtocol(), absolute.getAuthority(), absolute.getPath(),
					absolute.getQuery(), null);
			if (!links.contains(cleanURL.toString()) && !cleanURL.equals(seed)) {
				links.add(cleanURL.toString());
				queue.add(cleanURL.toString());
				count++;
			}

		}

		return links;

	}

	public static String craftHTTPRequest(URL url, HTTP type) {
		String host = url.getHost();
		String resource = url.getFile().isEmpty() ? "/" : url.getFile();

		return String.format("%s %s %s\n" + "Host: %s\n" + "Connection: close\n" + "\r\n", type.name(), resource,
				version, host);
	}

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
