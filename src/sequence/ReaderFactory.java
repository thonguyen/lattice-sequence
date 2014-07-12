package sequence;

import java.util.HashMap;


public class ReaderFactory {
	public enum type{SEQUENCE};
	public enum extension{TXT};
	
	public static Reader get(type t, extension e){
		switch (t){
		case SEQUENCE:
			switch (e){
			case TXT:
				//return new SequencesReaderText();
			}
			break;
		}
		return null;
	}
}
