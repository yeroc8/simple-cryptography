public class Base32 extends Cipher {
    private final static String conversion = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef";

    public Base32(String text, String key) {
        super(text, key);
    }

    public void base32Encode() {
        byte[] bytes = this.text.getBytes();
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
        for (int j = 0; j < (binary.length() / 5); j++) {
            base32.append(conversion.charAt(Integer.parseInt(binary.substring(j * 5, (j + 1) * 5), 2)));
        }
        this.text = base32.toString();
    }

    public void base32Decode() {
        char[] textArr = text.toCharArray();
        int conversionValue;
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            conversionValue = conversion.indexOf(textArr[i]);
            if (Integer.toBinaryString(conversionValue).length() < 5) {
                for (int j = 0; j < (5 - Integer.toBinaryString(conversionValue).length()); j++) {
                    binary.append('0');
                }
            }
            binary.append(Integer.toBinaryString(conversionValue));
        }
        StringBuilder normalText = new StringBuilder();
        for (int j = 0; j < (binary.length() / 8); j++) {
            int c = Integer.parseInt(binary.substring(j * 8, (j + 1) * 8), 2);
            normalText.append((char) c);
        }
        this.text = normalText.toString();
    }
}
