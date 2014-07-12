package sequence;

public class Sequence implements Comparable<Sequence>{
	/**
	 * A string represented a sequence
	 */
	private String sequence;
	private static int id = 0;

	public Sequence(String seq) {
		this.sequence = seq;	
		id++;
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
		
		//sequence o is included in this string 
//		if(toString().contains(o.toString())){
//			
//		}
		if(toString().indexOf(o.toString()) >= 0){
			return 1;
		}
		
		if(o.toString().indexOf(toString()) >= 0){
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
