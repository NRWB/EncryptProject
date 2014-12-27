
import java.io.File;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class driver {
	
	public static final String paf = "<arbitrary path to a specific folder>";
	
	public static void main(String[] args) throws Exception {
		
		final byte[] priv_key = new byte[]{0,0,9,9,2,4,1,8,0,3,8,0,0,5,1,0,5,3,2,2,7,5,6,1,1,5,0,6,6,5,4,2,7,1};
		final byte[] priv_iV = new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		
		Cipher c = Cipher.getInstance(CryptTombPassage.DEFAULT_CIPHER_ALGO_NAME);
		MessageDigest md = MessageDigest.getInstance(CryptTombPassage.DEFAULT_MSG_DIGEST_ALGO_NAME);
		Key sKeySpec = new SecretKeySpec(md.digest(priv_key), CryptTombPassage.DEFAULT_KEY_SPEC_ALGO_NAME);
		IvParameterSpec ivps = new IvParameterSpec(priv_iV);
		
		CryptTomb ct = new CryptTomb(c, sKeySpec, ivps, priv_key, priv_iV);
		
		File checker = new File(paf + "obfusVID.mp4");
		if (!checker.exists())
			checker.createNewFile();
		
		checker = new File(paf + "DeportivoLaCorua_RealMadrid2.mp4");
		if (!checker.exists())
			checker.createNewFile();
		
		ct.encryptFile(Paths.get(paf + "DeportivoLaCorua_RealMadrid.mp4"), Paths.get(paf + "obfusVID.mp4"));
		ct.decryptFile(Paths.get(paf + "obfusVID.mp4"), Paths.get(paf + "DeportivoLaCorua_RealMadrid2.mp4"));
		
	}
	
	
	
	
	
	
	/*
	
	public static void main(String[] args) throws Exception {
		
		final byte[] priv_key = new byte[]{0,0,9,9,2,4,1,8,0,3,8,0,0,5,1,0,5,3,2,2,7,5,6,1,1,5,0,6,6,5,4,2,7,1};
		final byte[] priv_iV = new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		
		Cipher c = Cipher.getInstance(CryptTombPassage.DEFAULT_CIPHER_ALGO_NAME);
		MessageDigest md = MessageDigest.getInstance(CryptTombPassage.DEFAULT_MSG_DIGEST_ALGO_NAME);
		Key sKeySpec = new SecretKeySpec(md.digest(priv_key), CryptTombPassage.DEFAULT_KEY_SPEC_ALGO_NAME);
		IvParameterSpec ivps = new IvParameterSpec(priv_iV);
		
		CryptTomb ct = new CryptTomb(c, sKeySpec, ivps, priv_key, priv_iV);
		
		File checker = new File(paf + "obfus.png");
		if (!checker.exists())
			checker.createNewFile();
		
		checker = new File(paf + "Alice z 2.png");
		if (!checker.exists())
			checker.createNewFile();
		
		ct.encryptFile(Paths.get(paf + "Alice in Wonderland Avatar.png"), Paths.get(paf + "obfus.png"));
		ct.decryptFile(Paths.get(paf + "obfus.png"), Paths.get(paf + "Alice z 2.png"));
		
	}
	
	*/
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 

	public static void main(String[] args) throws Exception {
		
		final byte[] priv_key = new byte[]{0,0,9,9,2,4,1,8,0,3,8,0,0,5,1,0,5,3,2,2,7,5,6,1,1,5,0,6,6,5,4,2,7,1};
		final byte[] priv_iV = new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		
		Cipher c = Cipher.getInstance(CryptTombPassage.DEFAULT_CIPHER_ALGO_NAME);
		MessageDigest md = MessageDigest.getInstance(CryptTombPassage.DEFAULT_MSG_DIGEST_ALGO_NAME);
		Key sKeySpec = new SecretKeySpec(md.digest(priv_key), CryptTombPassage.DEFAULT_KEY_SPEC_ALGO_NAME);
		IvParameterSpec ivps = new IvParameterSpec(priv_iV);
		
		CryptTomb ct = new CryptTomb(c, sKeySpec, ivps, priv_key, priv_iV);
		
		String str1 = "Hello World!";
		String str2 = "";
		String str3 = "";
		
		str2 = new String ( ct.encryptBytes(str1.getBytes()) , "UTF-8");
		
		str3 = new String ( ct.decryptBytes(str2.getBytes()) , "UTF-8");
		
		System.out.println(str1);
		System.out.println(str2);
		System.out.println(str3);
		
	}
	
	*/
	
	
}
