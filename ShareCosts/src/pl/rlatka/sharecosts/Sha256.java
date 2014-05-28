package pl.rlatka.sharecosts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256 {

	private static MessageDigest md;

	public static String encode(String username, String password, int iteration) {
		if(md == null)
			try {
				md = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) { }
		
		String toEncode = username + "xXx" + password;

		for (int i = 0; i < iteration; i++) {
			md.update(toEncode.getBytes());
			toEncode = bytArrayToHex(md.digest());
		}
		return toEncode;
	}

	private static String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

}
