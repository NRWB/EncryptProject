package backEnd;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Map.Entry;

import javax.swing.JPasswordField;

import intermediaryControl.Controller;

public class CryptoEvent {
	
	private CryptoEvent() {
		
	}
	
	public static void run(JPasswordField passKey, Controller c) {
		try {
			if (CryptoMethod.Encrypt == c.getCryptoMethod()) {
				runEncryption(passKey, c);
			} else {
				runDecryption(passKey, c);
			}
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void runEncryption(JPasswordField passKey, Controller c) throws GeneralSecurityException, IOException {
		char[] passChars = passKey.getPassword();
		for (Entry<Path, FileModel> e : c.getData().entrySet()) {
			CryptoModel.encrypt(passChars, c, e.getValue());
		}
        for (int i = 0, len = passChars.length; i < len; i++) {
        	passChars[i] = '\0';
        }
	}

	private static void runDecryption(JPasswordField passKey, Controller c) throws GeneralSecurityException, IOException {
		char[] passChars = passKey.getPassword();
		for (Entry<Path, FileModel> e : c.getData().entrySet()) {
			CryptoModel.decrypt(passChars, c, e.getValue());
		}
        for (int i = 0, len = passChars.length; i < len; i++) {
        	passChars[i] = '\0';
        }
	}
}
