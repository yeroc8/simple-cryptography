public class Cipher {

    private String text;
    private String key;

    public Cipher(String text, String key) throws IllegalArgumentException {
        if (!text.matches("[A-Za-z]+") || !key.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Text and key must contain only english letters");
        }
        this.text = text;
        this.key = key;
    }

    public String getText() {
        return text;
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
        for (int i = 0; i <= textLen; i++) {
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
            if (backwards) {
                shiftAmount *= -1;
            }
            for (int i = 1; i < row.length; i++) {
                // TODO: conveyor belt
            }
        }
        // TODO: reconstruct text
    }

    public void transpose() {
        transpose(false);
    }
}
