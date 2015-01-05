package backEnd.IO;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private static Logger logger;
	private static FileHandler fileHandler;
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private Log() {
		
	}
	
	public static void init() throws SecurityException, IOException {
		logger = Logger.getLogger("backEnd.IO.Log");
		fileHandler = new FileHandler("CryptoTombLog.txt");
		
		// Send logger output to our FileHandler.
		logger.addHandler(fileHandler);
		
		// Request that every detail gets logged.
		logger.setLevel(Level.ALL);
	}
	
	public static void write(Object o) {
		logger.log(Level.ALL, o.toString());
	}
	
}
