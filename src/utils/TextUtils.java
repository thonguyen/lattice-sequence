package utils;

public class TextUtils {
	public static boolean isNullOrEmpty(String str){
		if(str == null){
			return true;
		}
		//strim string before test
		return str.trim().isEmpty();
	}
}
