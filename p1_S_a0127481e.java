import java.math.BigInteger;

class p1_S_a0127481e {
	private static pad_oracle oracle = new pad_oracle();

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("There should be exactly two arguments!");
			System.exit(-1);
		}

		computePtxt(new BigInteger(args[0].substring(2), 16), args[1]);
	}

	public static void computePtxt(BigInteger c0, String c1) {
		String ptxt = "";
		BigInteger c0_guess = null;
		BigInteger oneByteGuess = null;
		String pad = null;
		int pad_size = determinePadSize(c0, c1);
		ptxt = padBuilder("0" + pad_size, pad_size);

		for (int j = pad_size + 1; j <= 8; j++) {
			pad = padBuilder("0" + j, j);
			for (int i = 1; i < 256; i++) {
				oneByteGuess = BigInteger.valueOf(i);
				oneByteGuess = oneByteGuess.shiftLeft(8 * (j-1));
				c0_guess = c0.xor(new BigInteger(pad, 16).xor(oneByteGuess));
				c0_guess = c0_guess.xor(new BigInteger(ptxt, 16));
				if (oracle.doOracle("0x" + c0_guess.toString(16), c1)) {
					//System.out.println("Suceess! The correct one byte guess at position " + (8 - j) + " is " + i + ".");
					ptxt = BigInteger.valueOf(i).toString(16) + ptxt;
					ptxt = (ptxt.length() % 2 != 0) ? ("0" + ptxt) : ptxt;
					//System.out.println("current plaintext is " + ptxt);
					break;
				}
			}
		}

		System.out.println(hexToASCII(ptxt).substring(0, 8 - pad_size));
	}

	private static String padBuilder(String pad, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(pad);
		}
		return sb.toString();
	}

	private static String hexToASCII(String hexString) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hexString.length(); i += 2) {
        	String str = hexString.substring(i, i + 2);
        	output.append((char)Integer.parseInt(str, 16));
    	}
    	return output.toString();
	}

	private static int determinePadSize(BigInteger c0, String c1) {
		int pad = 8;
		String temp = "0x" + c0.toString(16);
		BigInteger c0_copy = c0;
		while (oracle.doOracle(temp, c1)) {
			pad--;
			c0_copy = c0.flipBit(pad * 7);
			temp = "0x" + c0_copy.toString(16);
		}
		return pad;
	}

}