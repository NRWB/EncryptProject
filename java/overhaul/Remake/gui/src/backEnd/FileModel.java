package backEnd;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileModel {

	private final Path inFile;
	private final Path outFile;
	private final boolean deleteInputAfter;
	
	public FileModel(Path input, Path output, boolean delAfter) {
		inFile = input;
		outFile = output;
		deleteInputAfter = delAfter;
	}
	
	// final - it's simple, and stays true for any and all derived classes.
	public final Path getInFile() {
		return inFile;
	}
	
	public final Path getOutFile() {
		return outFile;
	}
	
	public final boolean isDeletable() {
		return deleteInputAfter;
	}
	
	// creates an entry
	public static FileModel create(Path defaultPath, boolean delete) {
		// here would be a good spot to implement a naming convention for output files
		Path redone = null;
		String s = defaultPath.toString();
		s = attemptRename(s, "-out");
		redone = Paths.get(s);
		return new FileModel(defaultPath, redone, delete);
	}
	
	private static String attemptRename(String input, String tag) {
		String result = "";
		
		int idx = input.lastIndexOf('.');
		if (idx == -1)
			return input + tag; // no period indicating an extension, return nothing.
		
		String prefix = input.substring(0, idx);
		String post = input.substring(idx, input.length());
		
	    result = prefix + tag + post;
		
		return result;
	}

}
