package BackEnd;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JPasswordField;

import FrontEnd.Log;

/**
 * 
 * A simple class to hold parameters that will be passed off to the CryptTomb object for future/further use.
 * 
 * @author Nick
 *
 */
public class CryptTombSpec {
	
	private boolean encrypt;
	private Path outputDir;
	private List<Path> processes;
	private boolean delOrigFiles;
	
	/**
	 * @param doEncryption - When set to true, this will use encrypting, and when set to false, this will use decrypting.
	 */
	public void setCrypt(boolean doEncryption) {
		encrypt = doEncryption;
	}
	
	/**
	 * Specify the output directory of the resulting files.
	 * @param p
	 */
	public void setOutputDir(Path p) {
		outputDir = p;
	}
	
	/**
	 * Allow this class to be able to go over the files to process to do encrypt/decrypt on
	 * @param procs
	 */
	public void setFilesToProcess(List<Path> procs) {
		processes = procs;
	}
	
	/**
	 * set to delete original files after being processed
	 * @param value
	 */
	public void setDelAfterwards(boolean value) {
		delOrigFiles = value;
	}
	
	/**
	 * Starts everything.
	 */
	public void go(JPasswordField pw) throws Exception { // TODO - MAY NEED TO PASS A PROG-BAR AS PARAM HERE
		char[] chars = pw.getPassword();
		byte[] bytes = new byte[chars.length * 2];
		for (int i = 0; i < chars.length; i++) {
			bytes[i * 2] = (byte) (chars[i] >> 8);
			bytes[i * 2 + 1] = (byte) chars[i];
		}
		/*
		 * http://stackoverflow.com/questions/4931854/converting-char-array-into-byte-array-and-back-again
		 * convert back:
		char[] chars2 = new char[bytes.length / 2];
		for (int i = 0; i < chars2.length; i++)
			chars2[i] = (char) ((bytes[i * 2] << 8) + (bytes[i*2+1] & 0xFF));
		String password = new String(chars2);
		*/
		
		// default settings
		final byte[] priv_iV = new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		Cipher c = Cipher.getInstance(CryptTombPassage.DEFAULT_CIPHER_ALGO_NAME);
		MessageDigest md = MessageDigest.getInstance(CryptTombPassage.DEFAULT_MSG_DIGEST_ALGO_NAME);
		Key sKeySpec = new SecretKeySpec(md.digest(bytes), CryptTombPassage.DEFAULT_KEY_SPEC_ALGO_NAME);
		IvParameterSpec ivps = new IvParameterSpec(priv_iV);

		CryptTomb ct = new CryptTomb(c, sKeySpec, ivps, bytes, priv_iV);
		
		//TODO Note that this is DEFAULT settings;
		for (Path entry : processes) {
			String str = entry.toString();
			String renamed = attemptRename(str, (encrypt ? "obfus" : "deob"));
			File checker = new File(renamed);
			if (!checker.exists())
				checker.createNewFile();
			if (outputDir.getParent().toString().compareTo(checker.getParent()) != 0) // == 0 means the file is already in the correct place
				checker.delete(); // the stub check-file isn't needed anymore. delete it, its size of 0 file size.
			String pathName = outputDir.toString() + Log.FILESEP + checker.getName();
			Path dest = Paths.get(pathName);
			if (encrypt)
				ct.encryptFile(entry, dest); // TODO - MAY NEED TO PASS A PROG-BAR AS PARAM HERE
			else
				ct.decryptFile(entry, dest); // TODO - MAY NEED TO PASS A PROG-BAR AS PARAM HERE
			if (delOrigFiles)
				entry.toFile().delete();
		}

		// now to zero out the users password.
		Arrays.fill(chars, '\0');
		Arrays.fill(bytes, (byte) 0);
	}
	
	private String attemptRename(String input, String tag) {
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
