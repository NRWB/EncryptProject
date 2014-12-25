import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptTomb {

	// http://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#Provider

	// http://stackoverflow.com/questions/27641981/repetitive-usage-of-javas-securerandom

	private static final int DEFAULT_ALLOCATED_BYTES = 16;
	private static final int DEFAULT_KEY_LENGTH = 32;
	private static final String DEFAULT_CHARSET_NAME = "UTF-8";
	private static final String DEFAULT_CIPHER_TRANSFORMATION_NAME = "AES/CBC/PKCS5Padding";
	private static final String DEFAULT_MESSAGE_DIGEST_ALGORITHM = "SHA-256";
	private static final String DEFAULT_SECRET_KEY_ALGORITHM = "AES";

	private int initializationVectorAllocatableBytes;
	private String specifiedCharsetName;
	private String cipherTransformationName;
	private String messageDigestAlgo;
	private String secretKeyAlgo;

	private static Random SECURE_RANDOM_KEY = new SecureRandom();

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * <b> Default Constructor </b> <br>
	 * <i> Initializes the CryptTomb object with the default data members </i>
	 * 
	 */
	public CryptTomb() {
		this(DEFAULT_ALLOCATED_BYTES, DEFAULT_KEY_LENGTH, DEFAULT_CHARSET_NAME,
				DEFAULT_CIPHER_TRANSFORMATION_NAME,
				DEFAULT_MESSAGE_DIGEST_ALGORITHM, DEFAULT_SECRET_KEY_ALGORITHM);
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * <b> Constructor (with initialization options) </b> <br>
	 * <i> Initializes the CryptTomb object with specified data members, parsed
	 * from the String[] "arguments" </i>
	 * 
	 * @param iVBytes
	 *            - The default initialization vector number of bytes
	 * @param arguements
	 *            - The default secret key length
	 * 
	 */
	public CryptTomb(int iVBytes, int keyLen, String... arguments) {
		initializationVectorAllocatableBytes = iVBytes;
		initDataMembers(arguments);
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * <b> Constructor Helper </b> <br>
	 * <i> Assists in construction of object; verifies and sets the string array
	 * of arguments </i>
	 * 
	 * @param input
	 *            - The string array of arguments
	 */
	private void initDataMembers(String[] input) {
		if (input == null || input.length != 4)
			throw new IllegalArgumentException("constructor string args");
		for (String s : input)
			if (s == null || s == "")
				throw new IllegalArgumentException("constructor string args");
		switch (input.length) {
		case 4:
			secretKeyAlgo = input[3];
		case 3:
			messageDigestAlgo = input[2];
		case 2:
			cipherTransformationName = input[1];
		case 1:
			specifiedCharsetName = input[0];
			break;
		default:
			throw new IllegalArgumentException("constructor string args");
		}
		doCipherKeyLengthCheck();
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * 
	 * 
	 * YET TO BE DEFINED / EXPLAINED / DOCUMENTED
	 * 
	 * 
	 */
	public String encrypt(final String message,
			final String initializationVector, final String key)
			throws Exception {
		Cipher c = Cipher.getInstance(cipherTransformationName);
		MessageDigest md = MessageDigest.getInstance(messageDigestAlgo);
		byte[] k = md.digest(key.getBytes(DEFAULT_CHARSET_NAME));
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(k, secretKeyAlgo),
				getIvParameterSpec(initializationVector));
		Base64.Encoder enc = Base64.getEncoder();
		return enc.encodeToString(c.doFinal(message.getBytes()));
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * 
	 *  
	 * YET TO BE DEFINED / EXPLAINED / DOCUMENTED
	 * 
	 * 
	 */
	public String decrypt(final String message,
			final String initializationVector, final String key)
			throws Exception {
		Cipher c = Cipher.getInstance(cipherTransformationName);
		MessageDigest md = MessageDigest.getInstance(messageDigestAlgo);
		byte[] k = md.digest(key.getBytes(DEFAULT_CHARSET_NAME));
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(k, secretKeyAlgo),
				getIvParameterSpec(initializationVector));
		Base64.Decoder dec = Base64.getDecoder();
		return new String(c.doFinal(dec.decode(message)));
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * <b> Get Initialization Vector Parameter Spec </b> <br>
	 * <i> Returns a new IvParameterSpec object, given the input String
	 * "initVector" </i>
	 * 
	 * @param initVector
	 *            - The input string to convert to bytes. Afterwards those bytes
	 *            are used to create the IvParameterSpec
	 * @return A new IvParameterSpec object
	 * 
	 */
	private IvParameterSpec getIvParameterSpec(String initVector) {
		if (initVector == null || initVector == "")
			throw new NullPointerException("Null initialize vector string");
		IvParameterSpec ivps = null;
		try {
			ivps = getIvParameterSpec(initVector.getBytes(specifiedCharsetName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ivps;
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * <b> Get Initialization Vector Parameter Spec </b> <br>
	 * <i> Returns a new IvParameterSpec object, given the input byte array.
	 * Note the padding of bytes for certain mismatch cases. </i>
	 * 
	 * @param inBytes
	 *            - The input bytes to use to create the IvParameterSpec
	 * @return A new IvParameterSpec object
	 * 
	 */
	private IvParameterSpec getIvParameterSpec(byte[] inBytes) {
		if (inBytes == null)
			throw new RuntimeException("Could not allocate bytes");
		byte[] requiredBytes = Arrays.copyOf(inBytes,
				initializationVectorAllocatableBytes);
		return new IvParameterSpec(requiredBytes);
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * <b> Check Cipher Key Length </b> <br>
	 * <i> Checks to see if the installed JRE has the appropriate privileges
	 * </i>
	 * 
	 */
	private void doCipherKeyLengthCheck() {
		try {
			if (Cipher.getMaxAllowedKeyLength(cipherTransformationName) < 256)
				throw new IllegalStateException(
						"Unlimited crypto files not present in this JRE");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * <b> Generate Secure Random Key </b> <br>
	 * <i> A method to get true Secure Random integer values </i>
	 * 
	 * @param iterations
	 *            - The number of values to add to the result
	 * @param refreshInstance
	 *            - Set true to refresh the underlying SecureRandom instance
	 *            before obtaining integer values from that SecureRandom
	 *            instance
	 * @return A string with a number (determined by iterations) of integers
	 *         appended to it.
	 * 
	 */
	public static final String genRandKey(int iterations,
			boolean refreshInstance) {
		if (refreshInstance)
			refresh();
		String result = "";
		for (int i = 0; i < iterations; i++)
			result += SECURE_RANDOM_KEY.nextInt();
		return result;
	}

	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	/**
	 * 
	 * <b> Refresh </b> <br>
	 * <i> Generates a new strong instance of a SecureRandom object. </i>
	 * 
	 */
	private static final void refresh() {
		try {
			SECURE_RANDOM_KEY = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
