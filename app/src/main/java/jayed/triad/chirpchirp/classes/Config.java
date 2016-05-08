package jayed.triad.chirpchirp.classes;

/**
 * Created by edwardjihunlee on 16-03-13.
 */
public class Config {
    private static String keyValue;
    private static String accessKey;
    private static String secretKey;

    public Config() {
        this.keyValue = "us-east-1:582ced83-5008-4a52-9882-2ccd48cb49fb";
        this.accessKey = "AKIAIUMVOIGB4MUZNRIA";
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
