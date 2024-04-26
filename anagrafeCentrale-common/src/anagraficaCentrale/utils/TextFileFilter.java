package anagraficaCentrale.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

public class TextFileFilter implements FilenameFilter {
	private Pattern p;
	public TextFileFilter(String filter) {
		if(filter==null || filter.trim().equals("")){
			p=null;
		}else{
			p = Pattern.compile(filter.toLowerCase());
		}
	}

	@Override
	public boolean accept(File dir, String name) {
		if(p==null) return true;
		String lowercaseName = name.toLowerCase();
		return p.matcher(lowercaseName).matches();
	}

}