public class Cipher extends Base32 {

    protected String text;
    protected String key;

    public Cipher(String text, String key) throws IllegalArgumentException {
    	//super(key2);
        if (!key.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Key must contain only english letters");
        }
        if (!text.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Internal error: Unknown character detected in Cipher.text");
        }
        if (key.length() > text.length()) {
            key = key.substring(0, text.length());
        }
        this.text = text;
        this.key = key;
    }

    public String getText() {
        return text;
    }

    /**
     * Gets the index of a letter in the alphabet
     * A=0, B=1, C=2, and so on
     * @param c the char to get a number for
     * @return  the index of the char
     */
    private int letterNum(char c) {
        if (Character.isUpperCase(c)) {
            return c - 'A';
        } else {
            return c - 'a';
        }
    }

    public void transpose(boolean backwards) {
        int textLen = text.length();
        int keyLen = key.length();
        char[][] matrix = new char[keyLen][(textLen + keyLen - 1) / keyLen + 1]; // round up width and add key space
        // put key in first column
        for (int i = 0; i < keyLen; i++) {
            matrix[i][0] = key.charAt(i);
        }
        // split text into matrix
        for (int i = 0; i < textLen; i++) {
            matrix[i % keyLen][i / keyLen + 1] = text.charAt(i);
        }
        if (!backwards) {
            // fill empty slots in last column with Xs
            for (char[] row : matrix) {
                if (row[row.length - 1] == 0) {
                    row[row.length - 1] = 'X';
                }
            }
        }
        for (char[] row : matrix) {
            // get number from letter
            int shiftAmount = letterNum(row[0]);
            // complete rotation does not matter
            shiftAmount %= row.length - 1;
            int inverseShift = row.length - 1 - shiftAmount;
            if (backwards) {
                // shift the rest of the way to full rotation
                int inverse = shiftAmount;
                shiftAmount = inverseShift;
                inverseShift = inverse;
            }
            // save end part that would get overwritten
            char[] overflow = new char[shiftAmount];
            System.out.println(inverseShift + " " + shiftAmount + " " + row.length);
            System.out.println(row);
            System.arraycopy(row, inverseShift + 1, overflow, 0, shiftAmount);
            // shift beginning to end
            System.arraycopy(row, 1, row, shiftAmount + 1, inverseShift);
            // put overwritten end at beginning
            System.arraycopy(overflow, 0, row, 1, shiftAmount);
            System.out.println(row);
        }
        // reconstruct text
        char[] textArr = new char[textLen];
        for (int i = 0; i < textLen; i++) {
            textArr[i] = matrix[i % keyLen][i / keyLen + 1];
        }
        this.text = new String(textArr);
    }

    public void vigenere(boolean backwards) {
        StringBuilder newText = new StringBuilder();
        int caesarShift;
        for (int i = 0; i < text.length(); i++) {
            caesarShift = letterNum(key.charAt(i % key.length())); // repeat key if text is longer than it
            if (backwards) {
                caesarShift = 26 - caesarShift; // inverse shift
            }
            if ((letterNum(text.charAt(i)) + caesarShift) >= 26) {
                caesarShift = -(26 - caesarShift); // go backwards instead if would shift past Z
            }
            newText.append((char) (text.charAt(i) + caesarShift));
        }
        text = newText.toString();
    }
}
