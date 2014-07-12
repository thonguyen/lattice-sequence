package sequence;

import java.io.BufferedWriter;
import java.io.IOException;

public class SequencesWriterText implements SequencesWriter {

	@Override
	public void write(ContextSequence sequences, BufferedWriter file)
			throws IOException {
        file.write(sequences.toString());
	}
}
