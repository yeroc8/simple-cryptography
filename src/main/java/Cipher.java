public class Cipher {

    protected String text;
    protected String key;

    public Cipher(String text, String key) throws IllegalArgumentException {
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
    
    /**
    @param String, boolean
    If backwards is false, the program takes the first character of the field text and the variable phrase, adds their 
    respective indexes together, takes the modulus of said index by 52, and inserts the letter of the resulting index 
    into a variable newphrase. This continues for every letter of the field text.  Then, newphrase overwrites the field text.
    If backwards is true, the program subtracts the index phrase from the index of text, and does everything else the 
    same as if backwards is false. 
    */
    public void vigenere(String phrase, boolean backwards)
    {
      //Defines variables
      String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
      char[] alphabet2 = alphabet.toCharArray();
      StringBuilder newphrase = new StringBuilder();
      StringBuilder Phrase = new StringBuilder();
      Phrase.append(phrase);
      char newchar;
      int phraseindex, textindex, appendlength;
      /*
      If phrase's length is less than text's length, appends "A" to phrase until their lengths are equal.
      This is to prevent an error from the index being out of range.
      */
      if (Phrase.length() < this.text.length())
      {
         appendlength = (this.text.length() - Phrase.length());
         for (int i = 0; i < appendlength; i++)
         {
            Phrase.append("A");
         }
      }
      //Creates array versions of text and phrase, so that each letter can be itterated through one at a time.
      phrase = Phrase.toString();
      char[] phrase2 = phrase.toCharArray();
      char[] text2 = text.toCharArray();
      /*
      If backwards is false, takes each character from text and phrase, finds their index, and adds them together.
      If backwards is true, takes each character from text and phrase, finds their index, and subtracts
      phrase's index from text's index.
      */
      for (int j = 0; j < this.text.length(); j++)
      {
         phraseindex = alphabet.indexOf(String.valueOf(phrase2[j]));
         textindex = alphabet.indexOf(String.valueOf(text2[j]));
         if (!backwards)
         {
            newchar = alphabet2[(phraseindex + textindex)%alphabet.length()];
         }
         else
         {
            newchar = alphabet2[(textindex - phraseindex)%alphabet.length()];
         }
         newphrase.append(newchar);
      }
      //Saves changes by making text equal to newphrase
      this.text = newphrase.toString();
    }
}
