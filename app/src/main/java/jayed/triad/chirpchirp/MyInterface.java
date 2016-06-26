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
    JsonArray chirpGetMyChirps(JsonObject json);

    @LambdaFunction
    JsonObject chirpGetUser(JsonObject json);

    @LambdaFunction
    JsonArray chirpOtherUserChirps(JsonObject json);

    @LambdaFunction
    JsonObject chirpPostNewChirp(JsonObject json);

    @LambdaFunction
    JsonObject chirpSearch(JsonObject json);

    @LambdaFunction
    JsonArray chirpMain(JsonObject json);

    @LambdaFunction
    JsonObject chirpChangePassword(JsonObject json);

    /**
     * Invoke lambda function "echo". The functionName in the annotation
     * overrides the default which is the method name

    @LambdaFunction(functionName = "echo")
    void noEcho(NameInfo nameInfo);
    */
}