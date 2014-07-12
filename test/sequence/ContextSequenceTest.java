package sequence;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class ContextSequenceTest {
	private ContextSequence cs;
    @Before
    public void setUp() throws Exception
    {
    	String workingDir = System.getProperty("user.dir");
		String filename = workingDir + "/examples/ExampleSequences.txt";
		cs = new ContextSequence(filename);
    }	
    @Test
    public void testInit(){
    	assertTrue(cs != null);
    }
	@Test
	public void testClosure() {
		TreeSet<Comparable> testSet = new TreeSet<Comparable>();
		//drdrgaarg
		Comparable first = cs.getSet().first();
		testSet.add(first);
		TreeSet<Comparable> closureOfFirst = cs.closure(testSet);
		Iterator<Comparable> itr = closureOfFirst.iterator();
		while(itr.hasNext()){
			Sequence s = (Sequence)itr.next();
			System.out.println(s.toString());
		}		
		assertTrue(closureOfFirst.contains(first));
		
		
	}

}
