package jayed.triad.chirpchirp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonObject;

import jayed.triad.chirpchirp.classes.Account;


public class DescriptionChangeActivity extends Activity {

    private DescriptionChangeTask mDescriptionChangeTask = null;
    private EditText mNewDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_descriptionchange);

        mNewDescription = (EditText) findViewById(R.id.NewDescription);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) width * 4 / 5, (int) height * 3 / 5);

        Button b = (Button) findViewById(R.id.changeDescription);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String description = mNewDescription.getText().toString();
                    changeDescription(description);

            }
        });


    }

    private void changeDescription(String description) {
        if (mDescriptionChangeTask != null){
            return;
        }

        mNewDescription.setError(null);

        boolean cancel = false;
        View focusView = null;

        mDescriptionChangeTask = new DescriptionChangeTask(description);
        mDescriptionChangeTask.execute((Void) null);

    }




    public class DescriptionChangeTask extends AsyncTask<Void, Void, Boolean> {

        private final String mDescription;
        private String error;

        DescriptionChangeTask(String description) {
            mDescription = description;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                JsonObject json = new JsonObject();
                json.addProperty("userId", Account.getAccount().getAccountId());
                json.addProperty("descript", mDescription);
                JsonObject response = Factory.getMyInterface().chirpChangeDescription(json);
                Log.d("test", json.toString());
                Log.d("test", "login working");
//                Log.d("test", response.toString());
//                Account.getInstance(response); // parse userId into singleton
                // Simulate network access.
            } catch (LambdaFunctionException lfe) {
                error = lfe.getDetails().toString();
                Log.e("test", lfe.getDetails(), lfe);
                Log.e("test", lfe.getDetails());
                return false;

            }
            Log.d("test", "password changed");
//            Log.d("test", Account.getAccount().getUser().getChirps().toString());

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mDescriptionChangeTask = null;
//            showProgress(false);

            if (success) {
                finish();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }

            Log.d("test", "FINISH CHANGING DESCRIPTION");
            finish();
        }

    }


}
