package backEnd.IO;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
/**
 * 
 * Class: IOLib
 * 
 * Description: Custom functions for specific IO-related functions.
 * 
 * TODO - Visit other todo marks, edit last method in this file + add comments
 * and finally, document the global variables in this class.
 * 
 * @author Nick
 *
 */
public class IOLib {
	
	public static final String NEWLINE = System.getProperty("line.separator");
	
	private static final int SEARCH_ALL_LEVELS = -1;
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * 
	 * Method: getFilesByteSum
	 * 
	 * Description: A method used to compute the total amount of bytes for
	 * a given List of Path Objects (referring to the individual files that
	 * each Path Object represents). *Useful for progress bar functionality.
	 * 
	 * @param filePaths - A list of Path objects.
	 * @return - A BigInteger object representing a summation of bytes.
	 * @throws IOException
	 */
	public static BigInteger getFilesByteSum(List<Path> filePaths) throws IOException {
		BigInteger result = new BigInteger(String.valueOf(0));
		for (Path p : filePaths) {
			String s = String.valueOf(Files.size(p));
			result.add(new BigInteger(s));
		}
		return result;
	}
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * 
	 * Method: listfiles
	 * 
	 * Description: A generalized version of a custom method used to gather
	 * files.
	 * 
	 * @param startDir - The initial directory level to start file searching.
	 * @param depth - The number of sub-directory levels to dig down from the
	 * startDir (each sub/child directory accounts for a depth level).
	 * @return - A List of Path Objects.
	 * @throws IOException
	 */
	public static List<Path> listFiles(Path startDir, int depth) throws IOException {
		List<Path> result = new ArrayList<Path>();
		Stack<Path> stack = new Stack<Path>();
		stack.push(startDir);
		int depthLevel = 0;
		DirectoryStream<Path> stream = null;
		while (!stack.isEmpty()) {
			stream = Files.newDirectoryStream(stack.pop());
			depthLevel++;
			for (Path p : stream) {
				if (Files.isDirectory(p))
					stack.push(p);
				if (p.toFile().isFile())
					result.add(p);
			}
			if (depthLevel == depth)
				break;
		}
		stream.close();
		return result;
	}
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * 
	 * Method: selectDirs
	 * 
	 * Description: Prompts user with a JFileChooser, then parses files
	 * (does not accept files that are also directories) and returns them
	 * in a List of Path Objects.
	 * 
	 * @param prev - A previous path to start from in the JFileChooser
	 * @param recursive - If folders are selected, setting this value to true
	 * will explore those folders, grabbing any sub/child file in any and all
	 * sub directories.
	 * @return - A List of Path Objects.
	 */
	public static List<Path> selectDirs(Path prev, boolean recursive) { 
		List<Path> masterRecord = new ArrayList<Path>();
		
		// TODO - perhaps move this to a single static instance?
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		
		// default path to start at:
		// TODO - global-ize if used more than once, or just do it
		File tempHome = new File(System.getProperty("user.home"));
		
		// Default to starting at a relative, previous file path
		String paf = prev.toString();
		File testPaf = new File(paf.trim());
		if (testPaf.exists()) {
			tempHome = testPaf;
		}
		
		chooser.setCurrentDirectory(tempHome);
		
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			Log.write("IOLib.selectDirs; JFileChooser, hard exit.");
			return masterRecord;
		}
		
		File[] files = chooser.getSelectedFiles();
		if (files == null) {
			Log.write("IOLib.selectDirs; File array null.");
			return masterRecord;
		} 
		
		for (File f : files) {
			boolean isDir = f.isDirectory();
			if (isDir && recursive) {
				List<Path> additionalFound = null;
				try { 
					additionalFound = listFiles(f.toPath(), SEARCH_ALL_LEVELS);
				} catch (IOException e) {
					String msg = e.getLocalizedMessage();
					Log.write("IOLib.selectDirs; IOException, " + msg);
				}
				for (Path p : additionalFound) {
					masterRecord.add(p);
				}
			}
			if (!isDir) {
				masterRecord.add(f.toPath());
			}
		}
		return masterRecord;
	}
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	public static Path selectDir(JTextField textUpdate, Component cParent) {
		Path masterRecord = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // dir only b/c its used for output folder selection
		chooser.setMultiSelectionEnabled(false);
		// default path to start at:
		File tempHome = new File(System.getProperty("user.home"));
		// test input text area to see if we have a path stored in that field to start from
		String paf = textUpdate.getText();
		File testPaf = new File(paf.trim());
		if (testPaf.exists()) {
			//Log.WriteLn("Using last starting point path that was left in the JTextArea: " + paf);
			tempHome = testPaf;
		}
		chooser.setCurrentDirectory(tempHome);
		int returnVal = chooser.showOpenDialog(cParent);
		if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
			//Log.WriteLn("Error: return value obtained from JFileChooser selected option");
			return masterRecord;
		}
		File file = chooser.getSelectedFile();
		if (file == null) {
			textUpdate.setText("");
			//Log.WriteLn("JFileChooser resulted in no selected file, File == null");
			return masterRecord;
		}
		String toDisplay = ""; // display all files in the File[], displayed in the text area
		toDisplay += "\"" + file.toString() + "\"" + NEWLINE;
		//Log.WriteLn("[+] Added file path to masterRecord, " + file.toString());
		masterRecord = file.toPath();
		textUpdate.setText(toDisplay);
		return masterRecord;
	}

}
