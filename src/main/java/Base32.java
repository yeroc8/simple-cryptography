public class Base32 {

    private final static String CONVERSION = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef";

    public static String encode(byte[] bytes) {
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        while (binary.length() % 5 != 0) {
            binary.append('0');
        }
        StringBuilder base32 = new StringBuilder();
        for (int i = 0; i < (binary.length() / 5); i++) {
            base32.append(CONVERSION.charAt(Integer.parseInt(binary.substring(i * 5, (i + 1) * 5), 2)));
        }
        return base32.toString();
    }

    public static byte[] decode(String text) {
        int conversionValue;
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            conversionValue = CONVERSION.indexOf(text.charAt(i));
            if (Integer.toBinaryString(conversionValue).length() < 5) {
                for (int j = 0; j < (5 - Integer.toBinaryString(conversionValue).length()); j++) {
                    binary.append('0');
                }
            }
            binary.append(Integer.toBinaryString(conversionValue));
        }
        byte[] bytes = new byte[binary.length() / 8];
        for (int i = 0; i < (binary.length() / 8); i++) {
            int b = Integer.parseInt(binary.substring(i * 8, (i + 1) * 8), 2);
            bytes[i] = (byte) (b & 0xff);
        }
        return bytes;
    }
}
