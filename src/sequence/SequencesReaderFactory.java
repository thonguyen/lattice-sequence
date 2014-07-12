package sequence;

import java.util.HashMap;

public class SequencesReaderFactory {
	private static final HashMap<String, Integer> EXTENSION = new HashMap<String, Integer>();
	static{
		EXTENSION.put("txt", 1);
	}
	
	public static SequencesReader get(String extension){
		if(EXTENSION.containsKey(extension)){
			switch (EXTENSION.get(extension)){
			case 1:
				return new SequencesReaderText();
			}
		}
		return null;
	}
}
