package Stego;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class encrypts text into image using key-picture. The same key-picture can be used to obtain the text from the image.
 * <p>
 * <p>
 * To start: Create new Crypter object and use methods encrypt() and decrypt().
 * <p>
 * If you are able to decrypt any text encrypted by this Crypter, without using the Key-picture during the
 * process(even if you use several pictures created with the same key and the same string for research), please contact
 * me @ joonas.vali[2]hotmail.com and explain me how did you manage that. :)
 *
 * @author Joonas Vali 2009 Apr.
 * NB: Picture class by Robert Sedgewick and Kevin Wayne is used for IO and image modification.
 */


public class Crypter {
    private Picture copy;
    private Picture key;
    private int bytes;
    private int countc; //count chars at text, when crypting

    /**
     * Construct Crypter object.
     *
     * @param key insert Key-picture for encryption or decryption. Must be .png!
     */
    public Crypter(String key) {
        this.key = new Picture(key);
        copy = new Picture(key);
        bytes = this.key.width() * this.key.height() - 2;

    }

    /**
     * Decrypt the String from the picture
     *
     * @param bytes the encrypted image bytes
     * @return Returns the String extracted from the picture
     */
    public String deCrypt(byte[] bytes) {
        if (bytes.length < 1)
            return null;
        int allowed = deCalcAllowed();
        int count = 0;
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < key.width(); i++)
            for (int j = 0; j < key.height(); j++) {
                if (!(i == 1 && j == 1)) {
                    count++;
                    if (count == allowed) {
                        text.append(deCryptChar(i, j));
                        count = 0;
                    }
                }
            }

        return text.toString();
    }

    /**
     * encrypt String into the picture using key-picture defined in constructor.
     *
     * @param text insert the String for encryption
     * @return the encrypted image bytes
     */
    public byte[] encrypt(String text) throws IOException {
        copy = new Picture(key.toString());
        countc = 0;
        int count = 0;

        int allowed = (int) Math.floor(bytes / text.length()); // bytes between characters. includes char.
        allowed(allowed);

        //int badData = (int)Math.floor(bytes/allowed)-text.length();

        for (int i = 0; i < key.width(); i++)
            for (int j = 0; j < key.height(); j++) {
                if (!(i == 1 && j == 1)) {
                    count++;
                    if (count == allowed) {
                        cryptChar(i, j, next(text));
                        count = 0;
                    } else {
                        cryptChar(i, j, 0);
                    }
                }
            }

        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ImageIO.write(copy.getImage(), "png", bo);
        return bo.toByteArray();
    }

    private char deCryptChar(int x, int y) {
        Color d = difference(x, y);
        int a = d.getRed() + d.getGreen() + d.getBlue();
        return (char) a;
    }

    private void cryptChar(int x, int y, int a) {
        Color pix = key.get(x, y);
        int blue = pix.getBlue();
        int red = pix.getRed();
        int green = pix.getGreen();

        if (a == 0) {
            int intensity = 30; // close to 40 is most secure
            int r = (int) (Math.random() * intensity);
            if (blue >= 128) blue -= r;
            else blue += r;
            r = (int) (Math.random() * intensity);
            if (green >= 128) green -= r;
            else green += r;
            r = (int) (Math.random() * intensity);
            if (red >= 128) red -= r;
            else red += r;
        } else {
            Coord c = split3(a);
            int r = c.x;
            if (blue >= 128) blue -= r;
            else blue += r;
            r = c.y;
            if (green >= 128) green -= r;
            else green += r;
            r = c.z;
            if (red >= 128) red -= r;
            else red += r;
        }
        pix = new Color(red, green, blue);
        copy.set(x, y, pix);

    }

    private int next(String text) {
        char a = 0;
        if (text.length() > countc) {
            a = text.charAt(countc);
            countc++;
        }
        return a;
    }

    private void allowed(int allowed) {
        Color pix = key.get(1, 1);
        int blue = pix.getBlue();
        int red = pix.getRed();
        int green = pix.getGreen();

        int count = 0;
        while (allowed > 127) {
            count++;
            allowed -= 127;
        }
        if (count > 0) {
            Coord e = split2(count);
            if (red < 128) {
                red += e.x;
            } else {
                red -= e.x;
            }
            if (green < 128) {
                green += e.y;
            } else {
                green -= e.y;
            }
        }
        if (blue < 128) {
            blue += allowed;
        } else {
            blue -= allowed;
        }

        pix = new Color(red, green, blue);
        copy.set(1, 1, pix);


    }

    private int deCalcAllowed() {
        Color d = difference(1, 1);
        return (d.getRed() * 127 + d.getGreen() * 127) + d.getBlue();
    }

    private Color difference(int x, int y) {
        Color pix = key.get(x, y);
        int blue = pix.getBlue();
        int red = pix.getRed();
        int green = pix.getGreen();
        Color c = copy.get(x, y);
        int Cblue = c.getBlue();
        int Cred = c.getRed();
        int Cgreen = c.getGreen();
        return new Color(Math.abs(red - Cred), Math.abs(green - Cgreen), Math.abs(blue - Cblue));
    }

    private Coord split2(int a) {
        int r = (int) (Math.random() * a);
        a -= r;
        return new Coord(a, r);
    }

    private Coord split3(int a) {
        int z = 0;
        int r = (int) (Math.random() * a);
        a -= r;
        if (a > r) {
            z = (int) (Math.random() * a);
            a -= z;
        } else {
            z = (int) (Math.random() * r);
            r -= z;
        }

        return new Coord(a, r, z);
    }


}

class Coord {
    int x;
    int y;
    int z;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}

