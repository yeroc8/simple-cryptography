import java.util.*;
public class CompositeTest
{
	public static void main(String[] args)
	{
      String text, key1, key2;
		Scanner r = new Scanner(System.in);
      System.out.println("Give the text which you want to encode/decode: ");
      text = r.nextLine();
      System.out.println("Give the key which you want to use for the tranposing cipher: ");
      key1 = r.nextLine();
      Cipher c = new Cipher(Base32.encode(text.getBytes()), key1);
      System.out.println(c.getText());
      //c.transpose(false);
      System.out.println(c.getText());
      c.vigenere(false);
      System.out.println(c.getText());
      c.vigenere(true);
      System.out.println(c.getText());
      //c.transpose(true);
      System.out.println(c.getText());
      System.out.println(new String(c.decode(c.getText())));
      
      
      
	}
}