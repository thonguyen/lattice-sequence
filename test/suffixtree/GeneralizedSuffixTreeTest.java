package suffixtree;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class GeneralizedSuffixTreeTest {

	@Test
	public void testLCS() {
		String[] seqs = new String[] {"drdrgaargf", "drgadrgdrf"};
		GeneralizedSuffixTree gst = new GeneralizedSuffixTree(seqs);
		List<String> lcs = gst.getLCS();
		assertTrue(lcs.contains("drga"));
		assertTrue(!lcs.contains("ga"));
		assertTrue(lcs.contains("f"));
	}

}
