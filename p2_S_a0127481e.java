class p2_S_a0127481e {

	private static dec_oracle oracle = new dec_oracle();

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("There should be exactly 1 argument!");
			System.exit(-1);
		}
		String ptxt = args[0];
		String paddedPtxt = padPtxt(ptxt);
		String IV = "0x0000000000000000";

		//do encryption rounds
		int num_blks = paddedPtxt.length() / 16;
		String blk1 = null;
		String blk2 = IV;
		String enc_result = "";

		for (int i = 0; i < num_blks; i++) {
			blk1 = oracle.doOracle("0x" + paddedPtxt.substring(i * 16, 16 * (i + 1)), blk2);
			blk2 = blk1;
			enc_result += blk1 + " ";
		}

		System.out.println(enc_result);


		//test code for correctness
		//do decryption rounds on ciphertext created to recover the original message
		//uncomment to use
		// String recovered_ptxt = "";
		// blk2 = IV;
		// for (int i = 0; i < num_blks; i++) {
		// 	blk1 = oracle.doOracle(result.substring(i * 18, 18 * (i + 1)), blk2);
		// 	blk2 = result.substring(i * 18, 18 * (i + 1));
		// 	recovered_ptxt += blk1.substring(2);
		// }
		// recovered_ptxt = hexToASCII(recovered_ptxt);
		// recovered_ptxt = recovered_ptxt.substring(0, recovered_ptxt.length() - (8 - ptxt.length() % 8));
		// System.out.println(recovered_ptxt.equals(ptxt));

	}

	private static String padPtxt(String ptxt) {
		int len = ptxt.length();
		String formattedPtxt = "";
		for (int i = 0; i < len; i++) {
			formattedPtxt += Integer.toHexString((int) ptxt.charAt(i));
		}  

		if (len % 8 != 0) {
			int pad_size = 8 - len % 8;
			String pad = padBuilder("0" + pad_size, pad_size);
			formattedPtxt += pad;
		} 

		return formattedPtxt;
	}

	private static String padBuilder(String pad, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(pad);
		}
		return sb.toString();
	}


	//for testing only
	//uncomment to use
	// private static String hexToASCII(String hexString) {
	// 	StringBuilder output = new StringBuilder();
	// 	for (int i = 0; i < hexString.length(); i += 2) {
 //        	String str = hexString.substring(i, i + 2);
 //        	output.append((char)Integer.parseInt(str, 16));
 //    	}
 //    	return output.toString();
	// }

}