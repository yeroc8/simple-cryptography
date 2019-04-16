public class VigenereTest {
    public static void main(String[] args) {
        Cipher cipher = new Cipher("testingTEXT", "yEeT");
        cipher.vigenere(false);
        System.out.println(cipher.getText());
        cipher.vigenere(true);
        System.out.println(cipher.getText());
    }
}
