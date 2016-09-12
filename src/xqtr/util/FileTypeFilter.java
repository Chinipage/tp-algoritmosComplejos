package xqtr.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;

public class FileTypeFilter extends FileFilter {
	
	private List<String> validExtensions;
	
	public FileTypeFilter(List<String> extensions) {
		validExtensions = extensions;
	}
	
	public FileTypeFilter(String extensions) {
		validExtensions = Arrays.asList(extensions.split(" "));
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
	    }
		String extension = Support.getFileExtension(f);
		return validExtensions.contains(extension);
	}

	public String getDescription() {
		String desc = String.join(", ", Support.transform(validExtensions, String::toUpperCase));
		return Support.replaceLast(desc, ",", " and") + " Files";
	}
	
}