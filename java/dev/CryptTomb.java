
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class CryptTomb extends CryptTombPassage {
	
	public CryptTomb(Cipher c, Key sks, IvParameterSpec ivps, byte[] keyData, byte[] ivData) {
		super(c, sks, ivps, keyData, ivData);
	}

	// Takes some bytes to be encrypted as a parameter, returns the resulting encrypted bytes
	protected byte[] encryptBytes(final byte[] bytes) throws Exception {
		super.initialize(Cipher.ENCRYPT_MODE);
		return Base64.getEncoder().encode(super.CTK_Cipher.doFinal(bytes));
	}
	
	protected byte[] decryptBytes(final byte[] bytes) throws Exception {
		super.initialize(Cipher.DECRYPT_MODE);
		return super.CTK_Cipher.doFinal(Base64.getDecoder().decode(bytes));
	}
	
	protected String encryptString(final String str, final Charset encoding) throws Exception {
		return new String(encryptBytes(str.getBytes()), encoding);
	}
	
	protected String decryptString(final String str, final Charset encoding) throws Exception {
		return new String(decryptBytes(str.getBytes()), encoding);
	}

	protected void encryptFile(final Path inputFile, final Path outputFile) throws Exception {
		final byte[] rawBytes = Files.readAllBytes(inputFile);
		final byte[] converted = encryptBytes(rawBytes);
		OutputStream result = new FileOutputStream(outputFile.toFile());
		result.write(converted);
		result.close();
	}
	
	protected void decryptFile(final Path inputFile, final Path outputFile) throws Exception {
		final byte[] rawBytes = Files.readAllBytes(inputFile);
		final byte[] converted = decryptBytes(rawBytes);
		OutputStream result = new FileOutputStream(outputFile.toFile());
		result.write(converted);
		result.close();
	}
}
