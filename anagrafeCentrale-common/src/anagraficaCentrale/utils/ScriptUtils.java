package anagraficaCentrale.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptUtils {
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
		return plainText;
	}
	
	public static String encrypt (String plainText) {
		return plainText;
	}
	
	

	public static boolean isValidTaxId(String codiceFiscale) {
		String CF_REGEX = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$";
		Pattern CF_PATTERN = Pattern.compile(CF_REGEX);
		Matcher matcher = CF_PATTERN.matcher(codiceFiscale);
	    return matcher.matches();
	}


}
