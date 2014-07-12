package sequence;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import utils.TextUtils;

public class SequencesReaderText implements SequencesReader{

	@Override
	public void read(ContextSequence sequences, BufferedReader file) throws IOException {
        StringTokenizer st =  new StringTokenizer(file.readLine());
        st.nextToken(); // first token corresponds to the string "Observations:"
        while (st.hasMoreTokens()) {
            String n = new String(st.nextToken());
            sequences.addObservation(n);
        }
        
        // second line : All attributes separated by a space
        // a StringTokenizer is used to divide the line into different token,
        // considering spaces as separator.
        st =  new StringTokenizer(file.readLine());
        st.nextToken(); // first token corresponds to the string "Attributes:"
        while (st.hasMoreTokens()) {
            String n = new String(st.nextToken());
            //sequences.addAttribute(new Sequence(n));
        }
        
		String line = file.readLine();
		while(!TextUtils.isNullOrEmpty(line)){
            st = new StringTokenizer(line);
            String o = st.nextToken();
            st.nextToken();
            String seq = st.nextToken();
            Sequence sequence = new Sequence(seq);
			sequences.addSequence(sequence);
			sequences.addExtentIntent(o, sequence);
			line = file.readLine();
		}
	}
}
