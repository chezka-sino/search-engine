import java.io.IOException;
import java.nio.file.Path;

// TODO Javadoc here, but not where you implement

public interface QueryParserInterface {
	
	public void parseFile(Path file, boolean exact);
	public void toJSON(String output) throws IOException;

}
