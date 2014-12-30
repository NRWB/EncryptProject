package FrontEnd;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Holds a buffer of recent actions.
 * This includes valid actions and errors that occur.
 * As the buffer updates, the logs are stored to a [compressed] file.
 * 
 * @author Nick
 *
 */
public class Log implements WindowConsts {
	
	private static final int bufferSize = 100;
	private static final int bufferResize = 75;
	
	private static final List<String> buffer = new ArrayList<String>(bufferSize);
	private static int bufferLineCount = 0;
	
	private static Path logFile = null;
	
	public static void openLogFile(Path p) {
		logFile = p;
	}
	
	public static void openLogFile(String s) {
		logFile = Paths.get(s);
	}
	
	public static void openLogFile(File f) {
		logFile = f.toPath();
	}
	
	// maybe a to do? use delphi / free pascal to log natively? (platform independent implementation required)
	// public native static void Write(char[] characters);
	
	// the char[] accounts for a SINGLE entry to the buffer
	public static void WriteLn(char[] characters, Charset encoding) {
		System.out.println(new String(characters));
		/*
		String line = String.valueOf(characters);
		buffer.add(line);
		++bufferLineCount;
		if (bufferLineCount > bufferResize) {
			writeBuffContents();
			bufferLineCount = 0;
		}
		*/
	}
	
	public static void WriteLn(String s, Charset encoding) {
		System.out.println(s);
		// WriteLn(s.toCharArray(), encoding);
	}
	
	public static void WriteLn(String s) {
		System.out.println(s);
		// WriteLn(s.toCharArray(), Charset.defaultCharset());
	}
	
	private static void writeBuffContents() {
		try {
			File f = new File(logFile.toString());
			Writer pw = new PrintWriter(f);
			for (int i = 0; i < bufferSize; i++) {
				String data = buffer.get(i);
				if (!data.trim().equals(""))
					pw.append(data + NEWLINE);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void flush() {
		writeBuffContents();
		bufferLineCount = 0;
	}

}
