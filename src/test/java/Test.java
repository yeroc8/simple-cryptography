public class Test {

   public static void main(String[] args) {
      Base32 test = new Base32();
      System.out.println(test.encode("a"));
      System.out.println(test.decode("a"));
   }
}