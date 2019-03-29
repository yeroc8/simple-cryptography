public class Cipher {

    private String text;
    private String key;

    public Cipher(String text, String key) throws IllegalArgumentException {
        if (!text.matches("[A-Z]+") || !key.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Text and key must contain only capital english letters");
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
            matrix[i%keyLen][i/keyLen] = text.charAt(i);
        }
        if (!backwards) {
            // fill empty slots in last column with Xs
            for (char[] row : matrix) {
                if (row[row.length-1] == 0) {
                    row[row.length-1] = 'X';
                }
            }
        }
        // TODO: implement conveyor belt algorithm, reconstruct text
    }

    public void transpose() {
        transpose(false);
    }
}
