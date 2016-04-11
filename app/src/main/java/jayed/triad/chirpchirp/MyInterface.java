package jayed.triad.chirpchirp;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/*
 * A holder for lambda functions
 */
public interface MyInterface {

    /**
     * Invoke lambda function "echo". The function name is the method name
     */
    @LambdaFunction
    JsonObject chirpLogin(JsonObject json);

    @LambdaFunction
    JsonObject chirpCreateUser(JsonObject json);

    @LambdaFunction
    JsonObject chirpGet(JsonObject json);

    @LambdaFunction
    JsonObject chirpGetMyChirps(JsonObject json);


    @LambdaFunction
    JsonObject chirpRegister(JsonArray json);

    /**
     * Invoke lambda function "echo". The functionName in the annotation
     * overrides the default which is the method name

    @LambdaFunction(functionName = "echo")
    void noEcho(NameInfo nameInfo);
    */
}