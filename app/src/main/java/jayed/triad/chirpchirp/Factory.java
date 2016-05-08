package jayed.triad.chirpchirp;

import android.support.v7.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

import jayed.triad.chirpchirp.classes.Config;

/**
 * Created by jimmychou516 on 16-03-20.
 */
public class Factory {

    /** the only EventLog in the system (Singleton Design Pattern) */
    private static Factory theFactory;
    private Config config = new Config();
    private String notSecretKey = config.getKeyValue();
    private LambdaInvokerFactory factory;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private MyInterface myInterface;

    /**
     * Prevent external construction.
     * (Singleton Design Pattern).
     */
    private Factory(AppCompatActivity activity) { // constructor is private -> Singleton
        // Initialize the Amazon Cognito credentials provider
        this.credentialsProvider = new CognitoCachingCredentialsProvider(
                activity.getApplicationContext(),
                notSecretKey, // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        this.factory = new LambdaInvokerFactory(
                activity.getApplicationContext(),
                Regions.US_EAST_1,
                credentialsProvider);

         this.myInterface = factory.build(MyInterface.class);

    }

    /**
     * Gets instance of EventLog - creates it
     * if it doesn't already exist.
     * (Singleton Design Pattern)
     * @return  instance of EventLog
     */
    public static Factory getInstance(AppCompatActivity activity) { // static -> global point of access by EventLog.getInstance()
        if (theFactory == null)
            theFactory = new Factory(activity);

        return theFactory;
    }

    public static MyInterface getMyInterface() { // static -> global point of access by EventLog.getInstance()
        //getInstance(activity);

        return theFactory.myInterface;
    }


    /*
    // Initialize the Amazon Cognito credentials provider
    CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
            this.getApplicationContext(),
            notSecretKey, // Identity Pool ID
            Regions.US_EAST_1 // Region
    );

    LambdaInvokerFactory factory = new LambdaInvokerFactory(
            this.getApplicationContext(),
            Regions.US_EAST_1,
            credentialsProvider);

    final MyInterface myInterface = factory.build(MyInterface.class);
    */
}
