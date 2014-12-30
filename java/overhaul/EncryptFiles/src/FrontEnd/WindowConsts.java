package FrontEnd;

import java.awt.Font;

public interface WindowConsts {

	public static final String APP_TITLE = "CryptTomb (Encryption Tool)";
	
	public static final int DEFAULT_WINDOW_W = 800;
	public static final int DEFAULT_WINDOW_H = 600;
	
	public static final String NEWLINE = System.getProperty("line.separator");
	public static final String FILESEP = System.getProperty("file.separator");
	
	/**
	 * - Title font constant used in the application
	 * @return
	 */
	public static Font createTitleFont() {
		return new Font("Times New Roman", Font.BOLD, 24);
	}
	
}
