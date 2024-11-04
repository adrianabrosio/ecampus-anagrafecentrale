package anagraficaCentrale.testsuite;

import java.security.NoSuchAlgorithmException;

import anagraficaCentrale.client.core.ConnectionManager;

public class Hasher {
	
	public static void main(String[] args) {
		try {
			System.out.println(ConnectionManager.hash("mariaDP".toCharArray()));
			System.out.println(ConnectionManager.hash("marioDP".toCharArray()));
			System.out.println(ConnectionManager.hash("francescoDP".toCharArray()));
			System.out.println(ConnectionManager.hash("giuliaDP".toCharArray()));
			System.out.println(ConnectionManager.hash("ginevraDP".toCharArray()));
			System.out.println(ConnectionManager.hash("federicaDP".toCharArray()));
			System.out.println(ConnectionManager.hash("matteoDP".toCharArray()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
