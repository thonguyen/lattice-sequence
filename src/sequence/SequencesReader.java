package sequence;

import java.io.BufferedReader;
import java.io.IOException;

public interface SequencesReader {
	
	public void read(ContextSequence sequences, BufferedReader file) throws IOException;
}
