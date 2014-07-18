package sequence;

import static org.junit.Assert.*;

import org.junit.Test;

public class SequenceTest {
		
	@Test
	public void testCompareTo() {
		Sequence seq1 = new Sequence("");
		Sequence seq2 = new Sequence("rrrardrag");
		assertTrue(seq1.compareTo(seq2) < 0);
		
		seq1 = new Sequence("rr");
		assertTrue(seq1.compareTo(seq2) < 0);
		
		seq1 = new Sequence("arrardrag");
		
		assertTrue(seq1.compareTo(seq2) < 0);
	}

}
