package anagraficaCentrale.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScriptUtils {
	final static Logger logger = LogManager.getRootLogger();
	/**
	 * get param from args. Params are case insensitive
	 * @param args
	 * @param param
	 * @return
	 */
	public static String getParam(String[] args, String param){
		param = param.toLowerCase();
		for (int i = 0; i < args.length; i++) {
			if(args[i].toLowerCase().startsWith(param))
				if(args[i].indexOf("=") > 0){
					String tmp=args[i].substring(param.length(), args[i].length());
					if(tmp.charAt(0)=='"') tmp = tmp.substring(1,tmp.length());
					if(tmp.charAt(tmp.length()-1)=='"') tmp = tmp.substring(0,tmp.length()-1);
					return tmp;
				}
				else
					return args[i];
		}
		return null;
	}
	/**
	 * set an env variable
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public static void setEnv(String key, String value) {
		try {
			Map<String, String> env = System.getenv();
			Class<?> cl = env.getClass();
			Field field = cl.getDeclaredField("m");
			field.setAccessible(true);
			Map<String, String> writableEnv = (Map<String, String>) field.get(env);
			writableEnv.put(key, value);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to set environment variable", e);
		}
	}

	public static String decrypt (String plainText) {
		//will be implemented in future releases
		return plainText;
	}

	public static String encrypt (String plainText) {
		//will be implemented in future releases
		return plainText;
	}

	public static boolean isValidTaxId(String codiceFiscale) {
		String CF_REGEX = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$";
		Pattern CF_PATTERN = Pattern.compile(CF_REGEX);
		Matcher matcher = CF_PATTERN.matcher(codiceFiscale);
		return matcher.matches();
	}
	
	public static Map<String, String> convertParamStringToMap(String mapToBeConverted, String separator) {
		String[] entries = mapToBeConverted.split(separator);
		HashMap<String, String> map = new HashMap<>();
		for (String entry : entries)
			if(entry.contains("=")) {
				String[] keyValue = entry.split("=", 2);
				map.put(keyValue[0], keyValue[1]);
			}
		return map;
	}
	
	public static String hash(String password) throws NoSuchAlgorithmException {
		return hash(password.toCharArray());
	}

	public static String hash(char[] password) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		Charset charset = StandardCharsets.UTF_8;
		byte[] encodedhash = digest.digest(charset.encode(CharBuffer.wrap(password)).array());
		logger.debug("hashed pw: " + String.valueOf(bytesToHex(encodedhash)));
		return String.valueOf(bytesToHex(encodedhash));
	}

	public static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	public static InputStream getResourceAsStream(Class<?> c, String resourceName) {
		InputStream in;
		//URL url = c.getClassLoader().getResource(resourceName);
		//System.out.println("resource path: "+url);
		in=c.getClassLoader().getResourceAsStream(resourceName);
		if(in==null) {
			//url = c.getClassLoader().getResource("resources/"+resourceName);
			//System.out.println("resource path2: "+url);
			in = c.getClassLoader().getResourceAsStream("resources/"+resourceName);
		}
		return in;
	}
	
	public static URL getResource(Class<?> c, String resourceName) {
		URL url;
		url=c.getClassLoader().getResource(resourceName);
		if(url==null) {
			url = c.getClassLoader().getResource("resources/"+resourceName);
		}
		return url;
	}

}
