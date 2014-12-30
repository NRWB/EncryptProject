package BackEnd;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

/**
 * Keeps the necessary information needed to be used for encrypting / decrypting
 * 
 * future to do == http://docs.oracle.com/javase/7/docs/api/javax/crypto/SealedObject.html
 * 
 * idea of this class == blueprints for deriving classes (classes that extend from this abstract class)
 * 
 * @author Nick
 *
 */
public abstract class CryptTombKeeper {
	
	protected Cipher CTK_Cipher;
	protected Key CTK_SecretKeySpec;
	protected IvParameterSpec CTK_IvParameterSpec;
	protected MessageDigest CTK_MessageDigest;
	
	protected CryptTombKeeper(Cipher c, Key sks, IvParameterSpec ivps) {
		CTK_Cipher = c;
		CTK_SecretKeySpec = sks;
		CTK_IvParameterSpec = ivps;
	}
	
	protected void setCipher(Cipher c) {
		CTK_Cipher = c;
	}
	
	protected void setCipher(String transformationAlgo) throws GeneralSecurityException {
		CTK_Cipher = Cipher.getInstance(transformationAlgo);
	}
	
	protected void setMessageDigest(MessageDigest md) {
		CTK_MessageDigest = md;
	}
	
	protected void setMessageDigest(String algo) throws GeneralSecurityException {
		CTK_MessageDigest = MessageDigest.getInstance(algo);
	}
	
	protected void setKey(Key sks) {
		CTK_SecretKeySpec = sks;
	}
	
	protected abstract void setKey(byte[] privKey);
	
	protected abstract void setIvParameterSpec(byte[] v);
	
	protected Cipher getCipher() {
		return this.CTK_Cipher;
	}
	
	protected Key getKey() {
		return this.CTK_SecretKeySpec;
	}
	
	protected IvParameterSpec getIvParameterSpec() {
		return this.CTK_IvParameterSpec;
	}
	
	protected MessageDigest getMessageDigest() {
		return this.CTK_MessageDigest;
	}

}
