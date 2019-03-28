import java.util.Arrays;
import java.util.Comparator;

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

    public void transpose() {
        int textLen = text.length();
        int keyLen = key.length();
        char[][] matrix = new char[keyLen][(textLen+keyLen-1)/keyLen+1]; // round up width and add key space
        // split text into matrix
        for (int i = 0; i <= textLen; i++) {
            char ch;
            // place the key in the first column for sorting
            if (i == 0) {
                ch = key.charAt(i);
            } else {
                ch = text.charAt(i-1);
            }
            matrix[i%keyLen][i/keyLen] = ch;
        }
        // fill empty slots in last column with Xs
        for (char[] row : matrix) {
            if (row[row.length-1] == 0) {
                row[row.length-1] = 'X';
            }
        }
        // sort rows by key
        Arrays.sort(matrix, new Comparator<char[]>() {
            @Override
            public int compare(char[] o1, char[] o2) {
                return Character.compare(o1[0], o2[0]);
            }
        });
        // TODO: reconstruct text
    }
}
