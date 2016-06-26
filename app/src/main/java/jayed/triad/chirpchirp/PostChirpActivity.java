package jayed.triad.chirpchirp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jayed.triad.chirpchirp.classes.Account;

/**
 * Created by jimmychou516 on 16-05-06.
 */
public class PostChirpActivity extends Activity {

    private PostTask mPostTask = null;
    private EditText mChirpView;
    private String chirp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_postchirp);

        mChirpView = (EditText) findViewById(R.id.chirpText);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) width * 4 / 5, (int) height * 2 / 5);

        Button b = (Button) findViewById(R.id.button_post);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "POST BUTTON CLICKED");

                chirp = mChirpView.getText().toString();
                Log.d("test", "this is chirp: " + chirp);
                if (chirp.length() <= 30 && chirp.length() > 0) {
                    Log.d("test", "Length less than 30");
                    postChirp(chirp);
                } else {
                    Log.d("test", "TOO LONG");
                }

            }
        });


    }

    private void postChirp(String message) {

        String[] hashtag = parseHashtag(message);
        mPostTask = new PostTask(message, hashtag);
        mPostTask.execute((Void) null);
    }

    private String[] parseHashtag(String message){

        Pattern MY_PATTERN = Pattern.compile("#(\\w+)");
        Matcher mat = MY_PATTERN.matcher(message);
        List<String> strs=new ArrayList<String>();
        while (mat.find()) {

//            Log.d("test", mat.group(1));
//            Log.d("test", mat.toString());
            strs.add(mat.group(1));
        }

        String[] hashtags = new String[strs.size()];

        if (strs.size() > 0) {
            for(int i = 0; i<strs.size(); i++){
                Log.d("test", "hashtag: "+strs.get(i));
                hashtags[i] = strs.get(i);
            }
        }


        Log.d("test", "array size: "+ hashtags.length);

        return hashtags;
    }

    public class PostTask extends AsyncTask<Void, Void, Boolean> {

        private final String mChirp;
        private final String[] mHashtag;
        private String error;

        PostTask(String chirpmsg, String[] hashtag) {
            mChirp = chirpmsg;
            mHashtag = hashtag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                JsonObject json = new JsonObject();
                //TODO: THIS IS FOR TESTING ONLY, SO REMOVE THE SAMPLE1 AND 1234 PASSWORD
//                json.addProperty("userId", mUser);
//                json.addProperty("password", mPassword);
                json.addProperty("userId", Account.getAccount().getAccountId());
                json.addProperty("chirp", mChirp);
                List<String> list = new ArrayList<String>(Arrays.asList(mHashtag));
                JsonArray array = new JsonArray();
                for (String next: list){
                    array.add(new JsonPrimitive(next));
                }
                json.add("hashtag", array);
                json.add("chirps", Account.getAccount().getUser().getChirps());
                JsonObject response = Factory.getMyInterface().chirpPostNewChirp(json);
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
            Log.d("test", "login passed");
//            Log.d("test", Account.getAccount().getUser().getChirps().toString());

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//            } else if (error.equals("{\"errorMessage\":\"error_user_not_found\"}")) {
//                mUserView.setError(getString(R.string.error_user_not_found));
//                mUserView.requestFocus();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
            Log.d("test", "FINISH POSTING CHIRP");
            finish();
        }

    }


}
