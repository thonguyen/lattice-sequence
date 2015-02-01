package sequence;

public class Sequence implements Comparable<Sequence>{
	/**
	 * A string represented a sequence
	 */
	private String sequence;
	private static int id = 0;
	
	/**
	 * not used
	 * @param seq
	 */
	public Sequence(String seq) {
		this.sequence = seq;	
		id++;
	}
	/**
	 * 
	 * @param seq
	 * @return true if seq is subsequence(substring) of current sequence
	 */
	public boolean containSubsequence(Sequence seq){
		return toString().indexOf(seq.toString()) >= 0;
	}
		
//	@Override
	public int compareTo2(Sequence o) {
		if(id > o.getId()){
			return 1;
		}
		if(id < o.getId()){
			return -1;
		}
		return 0;
	}

	@Override
	public int compareTo(Sequence o) {
		//@TODO: use contains or indexOf?
		
		if(toString().equals(o.toString())){
			return 0;
		}
		if(containSubsequence(o)){
			return 1;
		}
		if(o.containSubsequence(this)){
			return -1;
		}
		//
		return toString().compareTo(o.toString());
	}
	
	@Override
	public String toString(){
		return sequence;
	}


	public static int getId() {
		return id;
	}


	public static void setId(int id) {
		Sequence.id = id;
	}
}
