import Stego.Crypter;

public class StegoTest {
	public static void main(String[] args) {
		Crypter c = new Crypter("key.png"); // Key picture.
		
		//Crypt
		//c.encrypt("Hello, there", "secret.png");
		
		//Decrypt
		System.out.println(c.deCrypt("secret.png"));
		
	}
}


