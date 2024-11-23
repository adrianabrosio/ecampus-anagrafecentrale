package anagraficaCentrale.testsuite;

import java.security.NoSuchAlgorithmException;

import anagraficaCentrale.utils.ScriptUtils;

public class Hasher {
	
	public static void main(String[] args) {
		try {
			System.out.println(ScriptUtils.hash("federicaDP1"));
			System.out.println(ScriptUtils.hash("marioDP"));
			System.out.println(ScriptUtils.hash("francescoDP"));
			System.out.println(ScriptUtils.hash("giuliaDP"));
			System.out.println(ScriptUtils.hash("ginevraDP"));
			System.out.println(ScriptUtils.hash("federicaDP"));
			System.out.println(ScriptUtils.hash("matteoDP"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
