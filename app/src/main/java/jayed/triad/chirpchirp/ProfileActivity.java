package jayed.triad.chirpchirp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jayed.triad.chirpchirp.classes.Account;
import jayed.triad.chirpchirp.classes.Chirps;

public class ProfileActivity extends AppCompatActivity {

    private ChirpsTask mChirps = null;
    private JsonArray myChirps;
    private TextView mUsername;
    private TextView mDescription;
    private Chirps chirps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        setContentView(R.layout.content_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d("test", "attempting to set account Id");
        mUsername = (TextView)findViewById(R.id.username);
        String username = Account.getAccount().getAccountId();
        mUsername.setText(username);

        String description = Account.getAccount().getDescription();
        mDescription = (TextView)findViewById(R.id.description);
        mDescription.setText(description);

        String error;

        ChirpsTask mChirp = new ChirpsTask();
        mChirp.execute((Void) null);
        //Log.d("test", "THIS PRINTS " + myChirps.toString());

    }

    public class ChirpsTask extends AsyncTask<Void, Void, Boolean> {

        private String error;

        ChirpsTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                JsonArray chirpsJson = Account.getAccount().getUser().getChirps();
                Log.d("test", chirpsJson.toString());
                JsonObject response = Factory.getMyInterface().chirpRegister(chirpsJson);
//                JsonObject response = Factory.getMyInterface().chirpGetMyChirps(chirpsJson);
                Log.d("test", "try to get list of user's own chirps");
                Log.d("test", response.toString());
                myChirps = response.getAsJsonObject("Responses").getAsJsonArray("Chirp");
                chirps = new Chirps(myChirps);
                // Simulate network access.
            } catch (LambdaFunctionException lfe) {
                error = lfe.getDetails().toString();
                Log.e("test", lfe.getDetails(), lfe);
                Log.e("test", lfe.getDetails());
                Log.e("test", lfe.getDetails().toString());

            }
            Log.d("test", "get my chirps passed");

            return true;
        }

    }

//    @Override
//    protected Boolean doInBackground(Void... params) {
//        // TODO: attempt authentication against a network service.
//        try {
//            JsonObject json = new JsonObject();
//            json.addProperty("userId", mUser);
//            json.addProperty("password", mPassword);
//            JsonObject response = Factory.getMyInterface().chirpLogin(json);
//            Log.d("test", "login working");
//            Log.d("test", response.toString());
//            Account.getInstance(response); // parse userId into singleton
//            // Simulate network access.
//        } catch (LambdaFunctionException lfe) {
//            error = lfe.getDetails().toString();
//            Log.e("test", lfe.getDetails(), lfe);
//            Log.e("test", lfe.getDetails());
//            return false;
//
//            // TODO: register the new account here.
//        }
//        Log.d("test", "login passed");
//        return true;
//    }

//    @Override
//    protected void onPostExecute(final Boolean success) {
//        mAuthTask = null;
//        showProgress(false);
//
//        if (success) {
//            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//        } else if (error.equals("{\"errorMessage\":\"error_user_not_found\"}")) {
//            mUserView.setError(getString(R.string.error_user_not_found));
//            mUserView.requestFocus();
//        } else {
//            mPasswordView.setError(getString(R.string.error_incorrect_password));
//            mPasswordView.requestFocus();
//        }
//
//    }
//
//    @Override
//    protected void onCancelled() {
//        mAuthTask = null;
//        showProgress(false);
//    }



}
