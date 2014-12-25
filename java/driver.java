/**
 * Example use, driver
 * 
 * @author Nick
 *
 */
public class driver {
	public static void main(String[] args) {
		CryptTomb ct = new CryptTomb();
		String message = "Hello World! Foo Bar ABC 123";
		String secretMessage = "";
		String origMessage = "";
		String secretKey = CryptTomb.genRandKey(64, true);
		try {
			secretMessage = ct.encrypt(message, "sixteen012345678", secretKey);
			origMessage = ct.decrypt(secretMessage, "sixteen012345678", secretKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(message);
		System.out.println(secretMessage);
		System.out.println(origMessage);
	}
}
