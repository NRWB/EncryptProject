
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * idea for this class == used to set and build the necessary objects that are required for encrypting and decrypting objects
 * 
 * @author Nick
 *
 */
public class CryptTombPassage extends CryptTombKeeper {
	
	public static final int DEFAULT_IV_PARAM_SPEC_SIZE = 16;
	public static final String DEFAULT_CIPHER_ALGO_NAME = "AES/CBC/PKCS5Padding";
	public static final String DEFAULT_MSG_DIGEST_ALGO_NAME = "SHA-256";
	public static final String DEFAULT_KEY_SPEC_ALGO_NAME = "AES";
	
	private byte[] CTP_rawKey;
	private byte[] CTP_rawIv;

	public CryptTombPassage(Cipher c, Key sks, IvParameterSpec ivps, byte[] keyData, byte[] ivData) {
		super(c, sks, ivps);
		CTP_rawKey = keyData;
		CTP_rawIv = ivData;
	}

	@Override
	protected void setKey(byte[] privKey) {
		this.CTP_rawKey = new byte[privKey.length];
		for (int i = 0; i < this.CTP_rawIv.length; i++)
			this.CTP_rawKey[i] = privKey[i];
	}
	
	// facade method / method wrapper
	public void setKeySpec(byte[] privKey) {
		this.setKey(privKey);
	}

	
	@Override
	protected void setIvParameterSpec(byte[] bb) {
		this.CTP_rawIv = new byte[bb.length];
		for (int i = 0; i < this.CTP_rawIv.length; i++)
			this.CTP_rawIv[i] = bb[i];
	}
	
	// facade method / method wrapper
	public void setIvParamSpec(byte[] bb) {
		this.setIvParameterSpec(bb);
	}
	
	// opMode being, ex, Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
	public void initialize(int opMode) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		init();
		super.CTK_Cipher.init(opMode, super.CTK_SecretKeySpec, super.CTK_IvParameterSpec);
	}
	
	// uses default settings, hard coded here, in the case of a null object present
	private void init() throws NoSuchAlgorithmException, NoSuchPaddingException {
		if (this.CTP_rawKey == null || this.CTP_rawIv == null)
			throw new NullPointerException("null");
		if (super.CTK_Cipher == null)
			super.CTK_Cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGO_NAME);
		else if (super.CTK_MessageDigest == null)
			super.CTK_MessageDigest = MessageDigest.getInstance(DEFAULT_MSG_DIGEST_ALGO_NAME);
		else if (super.CTK_SecretKeySpec == null)
			super.CTK_SecretKeySpec = new SecretKeySpec(super.CTK_MessageDigest.digest(this.CTP_rawKey), DEFAULT_KEY_SPEC_ALGO_NAME);
		else if (super.CTK_IvParameterSpec == null)
			super.CTK_IvParameterSpec = getIvParameterSpec(this.CTP_rawIv, DEFAULT_IV_PARAM_SPEC_SIZE);
	}

	private IvParameterSpec getIvParameterSpec(final byte[] input, final int size) {
		if (input == null)
			throw new NullPointerException("Could not allocate bytes");
		byte[] requiredBytes = Arrays.copyOf(input, size);
		return new IvParameterSpec(requiredBytes);
	}

}
