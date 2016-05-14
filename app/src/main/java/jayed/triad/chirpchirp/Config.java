package jayed.triad.chirpchirp.classes;

/**
 * Created by edwardjihunlee on 16-03-13.
 */
public class Config {
    private static String keyValue;
    private static String accessKey;
    private static String secretKey;

    public Config() {
//        this.keyValue = "us-east-1:582ced83-5008-4a52-9882-2ccd48cb49fb";
        this.keyValue = "us-east-1:277eee66-cdc3-4331-a958-1af7da59aa2a";
        this.accessKey = "AKIAIF3VI2OVFINYMULA";
        this.secretKey = "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";
    }

    public String getKeyValue() {
        return this.keyValue;
    }
    public String getAccessKey() {
        return this.accessKey;
    }
    public String getSecretKey() {
        return this.secretKey;
    }


}
