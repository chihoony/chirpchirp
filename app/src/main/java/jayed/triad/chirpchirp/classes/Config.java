package jayed.triad.chirpchirp.classes;

/**
 * Created by edwardjihunlee on 16-03-13.
 */
public class Config {
    private static String keyValue;
    private static String accessKey = "AKIAI46EHF27JPKWY5FA";
    private static String secretKey = "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";

    public Config() {
        this.keyValue = "us-east-1:53f7e3e8-345c-4913-8155-4640b53777cf";
//        accessKey = "AKIAI46EHF27JPKWY5FA";
//        secretKey = "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";
    }

    public String getKeyValue() {
        return this.keyValue;
    }
    public static String getAccessKey() {
        return accessKey;
    }
    public static String getSecretKey() {
        return secretKey;
    }


}
