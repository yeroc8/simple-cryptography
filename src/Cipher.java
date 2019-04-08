public class Cipher {

    protected String text;
    protected String key;

    public Cipher(String text, String key) throws IllegalArgumentException {
        if (!key.matches("[A-Za-z]+") || !text.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Text and key must contain only english letters");
        }
        this.text = text;
        this.key = key;
    }

    public String getText() {
        return this.text;
    }

    public void transpose(boolean backwards) {
        int textLen = text.length();
        int keyLen = key.length();
        char[][] matrix = new char[keyLen][(textLen+keyLen-1)/keyLen+1]; // round up width and add key space
        // put key in first column
        for (int i = 0; i < keyLen; i++) {
            matrix[i][0] = key.charAt(i);
        }
        // split text into matrix
        for (int i = 0; i < textLen; i++) {
            matrix[i%keyLen][i/keyLen+1] = text.charAt(i);
        }
        if (!backwards) {
            // fill empty slots in last column with Xs
            for (char[] row : matrix) {
                if (row[row.length-1] == 0) {
                    row[row.length-1] = 'X';
                }
            }
        }
        for (char[] row : matrix) {
            // get number A=0, B=1, etc.
            int shiftAmount;
            if (Character.isUpperCase(row[0])) {
                shiftAmount = row[0] - 'A';
            } else {
                shiftAmount = row[0] - 'a';
            }
            // complete rotation does not matter
            shiftAmount %= row.length - 1;
            int inverseShift = row.length-1 - shiftAmount;
            if (backwards) {
                // shift the rest of the way to full rotation
                shiftAmount = inverseShift;
            }
            // save end part that would get overwritten
            char[] overflow = new char[shiftAmount];
            System.arraycopy(row, inverseShift+1, overflow, 0, shiftAmount);
            // shift beginning to end
            System.arraycopy(row, 1, row, shiftAmount+1, inverseShift);
            // put overwritten end at beginning
            System.arraycopy(overflow, 0, row, 1, shiftAmount);
        }
        // reconstruct text
        char[] textArr = new char[textLen];
        for (int i = 0; i < textLen; i++) {
            textArr[i] = matrix[i%keyLen][i/keyLen+1];
        }
        text = new String(textArr);
    }

    public void transpose() {
        transpose(false);
    }
}
