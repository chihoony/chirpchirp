package jayed.triad.chirpchirp;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.google.gson.JsonObject;

/*
 * A holder for lambda functions
 */
public interface MyInterface {

    /**
     * Invoke lambda function "echo". The function name is the method name
     */
    @LambdaFunction
    JsonObject echo(NameInfo nameInfo);

    @LambdaFunction
    JsonObject chirpGet(JsonObject json);

    @LambdaFunction
    JsonObject chirpCreateUser(String username, String email, String Password);

    /**
     * Invoke lambda function "echo". The functionName in the annotation
     * overrides the default which is the method name
     */
    @LambdaFunction(functionName = "echo")
    void noEcho(NameInfo nameInfo);
}