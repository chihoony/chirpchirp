package jayed.triad.chirpchirp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jayed.triad.chirpchirp.classes.Account;

public class ProfileActivity extends AppCompatActivity {

    private ChirpsTask mChirps = null;
    JsonArray myChirps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
                JsonArray chirps = Account.getAccount().getUser().getChirps();
                Log.d("test", chirps.toString());
                JsonObject response = Factory.getMyInterface().chirpGetMyChirps(chirps);
                Log.d("test", "try to get list of user's own chirps");
                Log.d("test", response.toString());
                myChirps = response.getAsJsonArray("Chirp");
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

}
