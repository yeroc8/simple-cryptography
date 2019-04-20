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
      Cipher c = new Cipher(text, key1);
      c.setText(c.encode(c.getText().getBytes()));
      System.out.println(c.getText());
      c.transpose(false);
      System.out.println(c.getText());
      c.vigenere(false);
      System.out.println(c.getText());
      c.vigenere(true);
      System.out.println(c.getText());
      c.transpose(true);
      System.out.println(c.getText());
      c.setText(new String(c.decode(c.getText())));
      System.out.println(c.getText());
      
      
	}
}