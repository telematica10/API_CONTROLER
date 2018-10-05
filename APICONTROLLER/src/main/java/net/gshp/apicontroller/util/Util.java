package net.gshp.apicontroller.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	
	public static String toStringJustified(String s) {
		return "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"
				+ "<html><body>"
				+ "<p align=\"justify\">"
				+ s
				+ "</p> "
				+ "</body></html>";
	}
	
	public static boolean evalua(Object a, Object b, String operador) {

		Class objectClass = a.getClass();
		boolean aMenorB = false;
		boolean aEqualB = false;
		
		if(operador.equals("CONTAINS")){
			String aValue=""+a;
			String bValue=""+b;
			return aValue.contains("@"+bValue+"@");
		}
		
		if (objectClass == String.class) {
			String aS = (String) a;
			String bS = (String) b;
			aMenorB = aS.compareTo(bS) < 0;
			aEqualB = aS.equals(bS);
//			System.out.println("a"+aS);
//			System.out.println("b"+bS);
//			System.out.println("aMenorB"+aMenorB);
			
		} else if (objectClass == double.class || objectClass == int.class
				|| objectClass == long.class || objectClass == float.class) {
			double aD = ((Double) a).doubleValue();
			double bD = ((Double) b).doubleValue();
			aMenorB = aD < bD;
			aEqualB = aD == bD;
		}
		if (operador.equals(">")) {
			return !aMenorB && !aEqualB;
		}
		if (operador.equals("<")) {
			return aMenorB;
		}
		if (operador.equals("=")) {
			return aEqualB;
		}
		if (operador.equals("<=")) {
			return aMenorB || aEqualB;
		}
		if (operador.equals(">=")) {
			return !aMenorB;
		}
		if (operador.equals("<>")) {
			return !aEqualB;
		}
		return false;
	}
	
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
