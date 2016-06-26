package jayed.triad.chirpchirp.classes;

/**
 * Created by MSRoh on 16-06-26.
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Hash {
    public static String getMD5Hash(String txt){
        MessageDigest m = null;
        String hash = null;
        try{
            m = MessageDigest.getInstance("MD5");
            m.update(txt.getBytes(), 0, txt.length());
            hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

}
