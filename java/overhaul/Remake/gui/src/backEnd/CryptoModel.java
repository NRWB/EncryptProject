package backEnd;

import intermediaryControl.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * The password should not be stored in this actual class.
 * (Security measure)
 * 
 * Should rename data members?
 * (methods and global variables)
 */

public class CryptoModel {

	// -----------------------------------------------------------------------
	/**
	 * Denotes that the first 16 bytes of a FileModel's input file or output
	 * file is where the 16 initialization vector bytes are kept.
	 * 
	 * Ex. When taking a raw file in, and outputting an encrypted file, the
	 * first 16 bytes of the output file are the 16 pre-appended bytes used for
	 * the initialization vector (saved for needing to decrypt the file).
	 * 
	 * Ex. Vice-Versa for taking an encrypted file and decrypting it.
	 */
	// TODO Implement better scheme, also store salt for additive security
	private static final int DEFAULT_IV_SIZE = 16;

	// -----------------------------------------------------------------------
	/**
	 * Specifies the size of the byte array used when generating a salt.
	 */
	private static final int DEFAULT_SALT_SIZE = 256;

	// -----------------------------------------------------------------------
	/**
	 * The name of the algorithm used when defaulting the creation of a
	 * SecretKeyFactory objects' getInstance().
	 */
	private static final String DEFAULT_KEY_ALGO = "PBKDF2WithHmacSHA256";
	/*
	private static final char[] DEFAULT_KEY_ALGO = { 'P', 'B', 'K', 'D', 'F',
			'2', 'W', 'i', 't', 'h', 'H', 'm', 'a', 'c', 'S', 'H', 'A', '2',
			'5', '6' };// PBKDF2WithHmacSHA256
	*/
	
	// -----------------------------------------------------------------------
	/**
	 * The default number of iterations to execute upon the creation of a
	 * PBEKeySpec object.
	 */
	private static final int DEFAULT_ITERATIONS = 65536;
	
	// -----------------------------------------------------------------------
	/**
	 * The default crypto algorithm to use
	 */
	private static final String DEFAULT_CRYPT_ALGO = "AES";
	
	/**
	 * The default cipher transformation
	 */
	private static final String DEFAULT_CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
	
	/**
	 * Default buffer size used in I/O operations
	 */
	private static final int DEFAULT_BUFFER_SIZE = 4096;

	/**
	 * The default constructor.
	 * 
	 * This constructor uses the given input path, output path, and whether the
	 * input path should be deleted after use. In addition, the default
	 * methodology of encryption is applied the the encryption process.
	 * 
	 * @param input
	 *            - The input file path
	 * @param output
	 *            - The output file path
	 * @param delAfter
	 *            - True to delete the input file after process
	 */
	private CryptoModel() {
		
	}
	
	public static void encrypt(final char[] password, final Controller c, final FileModel fm) throws GeneralSecurityException, IOException {
		KeySpec s = new PBEKeySpec(password, getSalt(), DEFAULT_ITERATIONS, DEFAULT_SALT_SIZE);
		
		SecretKeyFactory skf = SecretKeyFactory.getInstance(DEFAULT_KEY_ALGO);
		SecretKey sk = skf.generateSecret(s);
		SecretKey sks = new SecretKeySpec(sk.getEncoded(), DEFAULT_CRYPT_ALGO);

		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, sks);
		
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

		writeIvToOutFile(iv, fm.getOutFile());
		write(cipher, fm);
	}
	
	public static void decrypt(final char[] password, final Controller c, final FileModel fm) throws GeneralSecurityException, IOException {
		SecretKeyFactory f = SecretKeyFactory.getInstance(DEFAULT_KEY_ALGO);
		KeySpec s = new PBEKeySpec(password, getSalt(), DEFAULT_ITERATIONS, DEFAULT_SALT_SIZE);
		SecretKey tmp = f.generateSecret(s);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), DEFAULT_CRYPT_ALGO);
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_TRANSFORMATION);
		
		IvParameterSpec ivps = new IvParameterSpec(getIvFromInFile(fm.getInFile()));
		
		cipher.init(Cipher.DECRYPT_MODE, secret, ivps);
		write(cipher, fm);
	}
	
	private static void writeIvToOutFile(byte[] arr, Path p) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(p.toString());
			out.write(arr);
		} finally {
			out.close();
		}
	}
	
	private static byte[] getIvFromInFile(Path p) throws IOException {
		byte[] result = new byte[DEFAULT_IV_SIZE];
		for (int i = 0; i < DEFAULT_IV_SIZE; i++) {
			result[i] = 0x00;
		}
		String name = p.toString();
		InputStream fis = null;
		try {
			fis = new FileInputStream(name);
			fis.read(result); // int status = fis.read(result);
		} finally {
			fis.close();
		}
		return result;
	}
	
	private static byte[] getSalt() {
		byte[] salt = new byte[DEFAULT_SALT_SIZE];
		Random r = new SecureRandom();
		r.nextBytes(salt);
		return salt;
	}
	
	// if the iv is not of len. 16, then either;
	// 1. the first 16 of 16 + n bytes are used, or,
	// 2. the n to 16 bytes are used, and the remaining bytes to reach 16 are padded with zero's
	private static void write(final Cipher c, final FileModel fileModel) throws BadPaddingException, IllegalBlockSizeException, IOException {
		if (c == null)
			throw new NullPointerException("null");
		
		// initialize streams
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(fileModel.getInFile().toString());
			out = new FileOutputStream(fileModel.getOutFile().toString()); // don't forget to make sure these paths exist
			
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			
			int n = in.read(buffer);
			
			while (n >= 0) {
				byte[] toWrite = c.update(buffer, 0, n); // in take
				out.write(toWrite); // write contents
				n = in.read(buffer); // move through buffer
			}
			
			byte[] finBuffer;
			finBuffer = c.doFinal(); // the last block requires doFinal(), even if the block isn't completely filled
			out.write(finBuffer);
		} finally {
			in.close();
			out.close();
		}
	}

}
