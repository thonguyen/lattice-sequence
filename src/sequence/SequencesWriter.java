package sequence;

import java.io.BufferedWriter;
import java.io.IOException;

public interface SequencesWriter {
    void write(ContextSequence sequences, BufferedWriter file) throws IOException;
}
