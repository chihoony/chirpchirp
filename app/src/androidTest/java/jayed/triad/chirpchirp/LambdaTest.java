package jayed.triad.chirpchirp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;

import dalvik.annotation.TestTargetClass;



public class LambdaTest {
    String s;

    @Before
    public void initJSONObject() {
//        JsonParser parser = new JsonParser();
//        String sample = "{\n" +
//                "\t\"operation\": \"read\",\n" +
//                "\t\"TableName\": \"Chirp\",\n" +
//                "    \"Key\": {\"userId\": \"apple\",\n" +
//                "    \t\t\"chirpId\": 2}\n" +
//                "}\n";
//        JSONObject json = (JSONObject) parser.parse(sample);
        s = "testBefore compiled";

    };

    @Test
    public void testJSONObj() {
        assertEquals(s, "TestBefore Compiled");

    };
}
