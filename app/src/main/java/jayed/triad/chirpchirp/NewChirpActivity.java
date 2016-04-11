package jayed.triad.chirpchirp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonObject;

public class NewChirpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chirp);

        Button mUserNewChirpButton = (Button) findViewById(R.id.user_sign_in_button);
        mUserNewChirpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPostNewChirp();
            }
        });
    }

    private void attemptPostNewChirp() {
        // TODO: Create json file to send to AWS Lambda and new chirp will be posted there
//        try {
//            JsonObject json = new JsonObject();
//            json.addProperty("password", mPassword);
//            json.addProperty("email", mEmail);
//            json.add("chirps", user.getChirps());
//            json.addProperty("userId", "sample1");
//            Log.d("test", json.toString());
//            JsonObject response = Factory.getMyInterface().chirpCreateUser(json);
//            Log.d("test", "login working");
//            Log.d("test", response.toString());
//            // Simulate network access.
//        } catch (LambdaFunctionException lfe) {
//            error = lfe.getDetails().toString();
//            Log.e("test", lfe.getDetails(), lfe);
//            Log.e("test", lfe.getDetails());
//            Log.e("test", lfe.getDetails().toString());
//            return false;
//        }
    }


}
