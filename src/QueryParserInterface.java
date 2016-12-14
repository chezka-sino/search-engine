import java.io.IOException;
import java.nio.file.Path;

public interface QueryParserInterface {
	
	public void parseFile(Path file, boolean exact);
	public void toJSON(String output) throws IOException;

}
