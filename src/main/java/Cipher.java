import java.util.BitSet;

/**
 * @author yeroc8
 */
public class Cipher {

    private String text;
    private String[] keys;
    private int keysUsed = 0;
    private boolean backwards;

    /**
     * Constructs a new Cipher instance
     * @param text the text to run the ciphers on
     * @param keys the keys to use for the ciphers
     * @param backwards decrypt instead of encrypt
     * @throws IllegalArgumentException unless all the keys only contain english letters
     */
    public Cipher(String text, String[] keys, boolean backwards) throws IllegalArgumentException {
        if (backwards) {
            // only keep alphabetic chars in text
            StringBuilder builder = new StringBuilder();
            for (char c : text.toCharArray()) {
                if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                    builder.append(c);
                }
            }
            text = builder.toString();
        }
        for (int i = 0; i < keys.length; i++) {
            if (!keys[i].matches("[A-Za-z]+")) {
                throw new IllegalArgumentException("Key must contain only english letters");
            }
            if (keys[i].length() > text.length()) {
                keys[i] = keys[i].substring(0, text.length());
            }
        }
        this.text = text;
        this.keys = keys;
        this.backwards = backwards;
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

    /**
     * Retrieves the next stored key
     * @return the next key
     * @throws IllegalStateException if there are no keys left to use
     */
    private String getKey() throws IllegalStateException {
        if (keysUsed >= keys.length) {
            throw new IllegalStateException("No keys remaining");
        }
        if (backwards) {
            return keys[keys.length - ++keysUsed];
        } else {
            return keys[keysUsed++];
        }
    }

    /**
     * Uses a transposition cipher on the text
     * @throws IllegalStateException if there are no keys left to use
     */
    public void transpose() throws IllegalStateException {
        String key = getKey();
        int textLen = text.length();
        int keyLen = key.length();
        char[][] matrix = new char[keyLen][((textLen + keyLen - 1) / keyLen) + 1]; // round up width and add key space
        // put key in first column
        for (int i = 0; i < keyLen; i++) {
            matrix[i][0] = key.charAt(i);
        }
        // split text into matrix
        for (int i = 0; i < textLen; i++) {
            matrix[i % keyLen][i / keyLen + 1] = text.charAt(i);
        }
        if (!backwards) {
            // fill empty slots in last column with xs
            for (char[] row : matrix) {
                if (row[row.length - 1] == 0) {
                    row[row.length - 1] = 'x';
                    textLen++;
                }
            }
        }
        for (char[] row : matrix) {
            // get number from letter
            int shiftAmount = letterNum(row[0]);
            // complete rotation does not matter
            shiftAmount %= row.length - 1;
            if (backwards) {
                // shift the rest of the way to full rotation
                shiftAmount = row.length - 1 - shiftAmount;
            }
            int inverseShift = row.length - 1 - shiftAmount;
            // save end part that would get overwritten
            char[] overflow = new char[shiftAmount];
            System.arraycopy(row, inverseShift + 1, overflow, 0, shiftAmount);
            // shift beginning to end
            System.arraycopy(row, 1, row, shiftAmount + 1, inverseShift);
            // put overwritten end at beginning
            System.arraycopy(overflow, 0, row, 1, shiftAmount);
        }
        // reconstruct text
        char[] textArr = new char[textLen];
        char c;
        for (int i = 0; i < textLen; i++) {
            c = matrix[i % keyLen][i / keyLen + 1];
            // don't include xs in output when decrypting
            if (!backwards || c != 'x') {
                textArr[i] = c;
            }
        }

        text = new String(textArr);
    }

    /**
     * Uses a basic Vigenere cipher on the text
     * @throws IllegalStateException if there are no keys left to use
     */
    public void vigenere() throws IllegalStateException {
        String key = getKey();
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

    /**
     * Uses a case-mixing algorithm on the text
     * @throws IllegalStateException if there are no keys left to use
     */
    public void caseMix() throws IllegalStateException {
        BitSet key = BitSet.valueOf(getKey().getBytes());
        BitSet cases = new BitSet(text.length());
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) >= 'a') {
                cases.set(i);
            }
        }
        for (int i = 0; i < cases.length(); i++) {
            boolean cb = cases.get(i);
            boolean kb = key.get(i);
            if (cb && kb) {
                cases.clear(i);
            } else if (!cb && kb) {
                cases.set(i);
            }
        }
        StringBuilder newText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (cases.get(i)) {
                newText.append(Character.toLowerCase(text.charAt(i)));
            } else {
                newText.append(Character.toUpperCase(text.charAt(i)));
            }
        }
        text = newText.toString();
    }
}
