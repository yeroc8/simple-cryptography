import Stego.*;
public class Base32{

    private final static String CONVERSION = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef";
    
    //public Base32(String key)
    //{
    	//super(key);
    //}
    
    /**
    Takes the field text, converts it to binary, then takes the first 5 characters and converts them back to decimal.
    Then, the program inserts the character whose index is equal to said decimal number.  The program continues
    this until the entire binary String is used up.
    */
    public static String encode(byte[] bytes) {
        //Declares variables
        StringBuilder binary = new StringBuilder();
        //Converts the bytes to binary
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        //If binary is not divisable by 5, adds zeroes until it is
        while (binary.length() % 5 != 0) {
            binary.append('0');
        }
        /*
        Takes 5 characters from binary, converts them to decmial, and inserts the character whose index is equal to said
        number into base32.
        */
        StringBuilder base32 = new StringBuilder();
        for (int i = 0; i < (binary.length() / 5); i++) {
            base32.append(CONVERSION.charAt(Integer.parseInt(binary.substring(i * 5, (i + 1) * 5), 2)));
        }
        //Returns the result
        return base32.toString();
    }

    /**
    Takes the field text, converts it to binary, then takes the first 8 characters and converts them back to decimal.
    Then, the program inserts the character whose unicode number is equal to said decimal number.  The program continues
    this until the entire binary String is used up.
    */
    public static byte[] decode(String text) {
        //Declares variables
        int conversionValue;
        StringBuilder binary = new StringBuilder();
        //Converts text to binary
        for (int i = 0; i < text.length(); i++) {
            conversionValue = CONVERSION.indexOf(text.charAt(i));
            //If any leading zeroes get deleted, adds them back in
            if (Integer.toBinaryString(conversionValue).length() < 5) {
                for (int j = 0; j < (5 - Integer.toBinaryString(conversionValue).length()); j++) {
                    binary.append('0');
                }
            }
            binary.append(Integer.toBinaryString(conversionValue));
        }
        /*
        Takes eight binary characters and converts them to decimal, then converts that decimal number to a char
        which is added to normalText
        */
        byte[] bytes = new byte[binary.length() / 8];
        for (int i = 0; i < (binary.length() / 8); i++) {
            int b = Integer.parseInt(binary.substring(i * 8, (i + 1) * 8), 2);
            bytes[i] = (byte) (b & 0xff);
        }
        //Returns the result
        return bytes;
    }
}
