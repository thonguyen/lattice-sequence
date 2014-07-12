package suffixtree;

public class MathHelpers {
    private static final int LOG_TABLE_256[] = 
        {
            0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
    };

    private static int MOD_37_BITPOSITION[] = // map a bit value mod 37 to its position
    {
    	32, 0, 1, 26, 2, 23, 27, 0, 3, 16, 24, 30, 28, 11, 0, 13, 4,
    	7, 17, 0, 25, 22, 31, 15, 29, 10, 12, 6, 0, 21, 14, 9, 5,
    	20, 8, 19, 18
    };

    /**
     * Find log base 2 of 32 bits integer (aka the position of the highest bit set)
     * @param n
     * @return
     */
    public static int log2(int n){
        int c = 0; // c will be lg(v)
        int t, tt; // temporaries

        if ((tt = n >> 16) !=0) {
            c = ((t = n >> 24)!=0) ? 24 + LOG_TABLE_256[t] : 16 + LOG_TABLE_256[tt & 0xFF];
        }
        else {
            c = ((t = n & 0xFF00)!=0) ? 8 + LOG_TABLE_256[t >> 8] : LOG_TABLE_256[n & 0xFF];
        }
        return c;	
    }

    /**
     * Find log base 2 of 32 bits integer (aka the position of the highest bit set)
     * @param n
     * @return
     */
    public static int log(int n){
        int c = 0; // c will be lg(v)
        int t, tt; // temporaries
    	if ((tt = n >> 16) != 0)
    	{
    	  c = ((t = tt >> 8) != 0) ? 24 + LOG_TABLE_256[t] : 16 + LOG_TABLE_256[tt];
    	}
    	else 
    	{
    	  c = (t = n >> 8) != 0 ? 8 + LOG_TABLE_256[t] : LOG_TABLE_256[n];
    	}
    	return c;
    }

	/**
	 * This method finds the number of zeros that are trailing on the right, so binary 0100 would produce 2. 
	 * It makes use of the fact that the first 32 bit position values are relatively prime with 37, 
	 * so performing a modulus division with 37 gives a unique number from 0 to 36 for each. 
	 * These numbers may then be mapped to the number of zeros using a small lookup table. 
	 * For more detail please see <a href="http://graphics.stanford.edu/~seander/bithacks.html#ZerosOnRightModLookup">Bit Twiddling Hacks</a>
	 * by Sean Eron Anderson.
	 */
	public static int countConsecutiveTrailingZeroBits(int n){
		return MOD_37_BITPOSITION[(-n & n) % 37];
	}
}
