package Utils;

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
import javax.swing.JTextPane;

import FrontEnd.Log;

public class IOLib {
	
	// progress bar related (helper)
	// get the total number of bytes to be used in a progress bars' update status
	public static BigInteger getFilesByteSum(List<Path> filePaths) throws IOException {
		// Start at 0 bytes
		BigInteger result = new BigInteger(String.valueOf(0));
		// Sum up all the file's sizes via the path list
		for (Path p : filePaths) {
			String s = String.valueOf(Files.size(p)); // http://stackoverflow.com/questions/7255592/get-file-directory-size-using-java-7-new-io
			result.add(new BigInteger(s));
		}
		// return the resulting summation
		return result;
	}
	
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
	
	// need to return the list of paths so that we can iterate over them too, not only just put them into the text pane.
	public static List<Path> selectDirs(JTextPane textUpdate, Component cParent, boolean recursive, int depthLvl) { 
		List<Path> masterRecord = new ArrayList<Path>();
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		// default path to start at:
		File tempHome = new File(System.getProperty("user.home"));
		// test input text area to see if we have a path stored in that field to start from
		String paf = textUpdate.getText();
		File testPaf = new File(paf.trim());
		if (testPaf.exists()) {
			Log.WriteLn("Using last starting point path that was left in the JTextArea: " + paf);
			tempHome = testPaf;
		}
		chooser.setCurrentDirectory(tempHome);
		int returnVal = chooser.showOpenDialog(cParent);
		if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
			Log.WriteLn("Error: return value obtained from JFileChooser selected option");
			return masterRecord;
		}
		File[] files = chooser.getSelectedFiles();
		if (files == null) {
			textUpdate.setText("");
			Log.WriteLn("JFileChooser resulted in no selected files, File[] == null");
			return masterRecord;
		}
		Log.WriteLn("Start of listing selected file path(s), given user selected options"); 
		String toDisplay = ""; // display all files in the File[], displayed in the text area
		for (File f : files) {
			boolean isDir = f.isDirectory();
			if (isDir && recursive) { // if recursive, then dig deeper
				List<Path> additionalFound = null;
				try { 
					additionalFound = listFiles(f.toPath(), depthLvl); // use "recursiveSubDirLvl" and call "listfiles()" - a method I made, used in other java src files
				} catch (IOException e) {
					Log.WriteLn("Error: adding recursively found files to masterRecord");
					Log.WriteLn(e.getMessage());
					Log.WriteLn("Error: IOException message above");
				}
				for (Path p : additionalFound) {
					toDisplay += "\"" + p.toString() + "\"" + Log.NEWLINE; // and add those files too
					masterRecord.add(p);
					Log.WriteLn("[+] Added file path to masterRecord, " + p.toString());
				}
			}
			if (!isDir) { // append the file path to the TEXT AREA
				toDisplay += "\"" + f.toString() + "\"" + Log.NEWLINE; // and add those files too
				masterRecord.add(f.toPath());
				Log.WriteLn("[+] Added file path to masterRecord, " + f.toString());
			}
		}
		textUpdate.setText(toDisplay); // display the text to screen
		return masterRecord;
	}
	
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
			Log.WriteLn("Using last starting point path that was left in the JTextArea: " + paf);
			tempHome = testPaf;
		}
		chooser.setCurrentDirectory(tempHome);
		int returnVal = chooser.showOpenDialog(cParent);
		if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
			Log.WriteLn("Error: return value obtained from JFileChooser selected option");
			return masterRecord;
		}
		File file = chooser.getSelectedFile();
		if (file == null) {
			textUpdate.setText("");
			Log.WriteLn("JFileChooser resulted in no selected file, File == null");
			return masterRecord;
		}
		String toDisplay = ""; // display all files in the File[], displayed in the text area
		toDisplay += "\"" + file.toString() + "\"" + Log.NEWLINE;
		Log.WriteLn("[+] Added file path to masterRecord, " + file.toString());
		masterRecord = file.toPath();
		textUpdate.setText(toDisplay);
		return masterRecord;
	}

}
