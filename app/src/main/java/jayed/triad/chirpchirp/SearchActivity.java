package jayed.triad.chirpchirp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jayed.triad.chirpchirp.classes.Account;
import jayed.triad.chirpchirp.classes.Chirp;
import jayed.triad.chirpchirp.classes.Chirps;

/**
 * Created by jimmychou516 on 16-05-06.
 */
public class SearchActivity extends Activity {

    private SearchTask mSearchTask = null;
    private EditText mSearchView;
    private String searchContent = "";

    private Chirps chirps;
    private List<Chirp> lochirps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        mSearchView = (EditText) findViewById(R.id.search_edit_field);

        Button b = (Button) findViewById(R.id.search_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "POST BUTTON CLICKED");

                searchContent = mSearchView.getText().toString();
                Log.d("test", "this is chirp: " + searchContent);
                if (searchContent.length() <= 30 && searchContent.length() > 0) {
                    Log.d("test", "Length less than 30");
                    searchChirp(searchContent);
//                    postChirp(searchContent);
                } else {
                    Log.d("test", "TOO LONG");
                }

            }
        });

        mSearchView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(keyCode == event.KEYCODE_ENTER){
                    //do what you want
                    Log.d("test", "ENTER BUTTON CLICKED");

                    searchContent = mSearchView.getText().toString();
                    Log.d("test", "this is chirp: " + searchContent);
                    if (searchContent.length() <= 30 && searchContent.length() > 0) {
                        Log.d("test", "Length less than 30");
                        searchChirp(searchContent);
//                    postChirp(searchContent);
                    } else {
                        Log.d("test", "TOO LONG");
                    }
                }
                return false;
            }
        });
    }

    private void searchChirp(String message){
        String[] user = parseUser(message);
        String[] hashtag = parseHashtag(message);

        if (user.length != 0 || hashtag.length != 0) {
            mSearchTask = new SearchTask(user, hashtag);
            mSearchTask.execute((Void) null);
        }
    }


    private String[] parseUser(String message){
        Pattern MY_PATTERN = Pattern.compile("@(\\w+)");
        Matcher mat = MY_PATTERN.matcher(message);
        List<String> strs=new ArrayList<String>();
        while (mat.find()) {
            strs.add(mat.group(1));
        }
        String[] hashtags = new String[strs.size()];
        if (strs.size() > 0) {
            for(int i = 0; i<strs.size(); i++){
                Log.d("test", "user: "+strs.get(i));
                hashtags[i] = strs.get(i);
            }
        }
        Log.d("test", "user array size: "+ hashtags.length);
        return hashtags;
    }

    private String[] parseHashtag(String message){
        Pattern MY_PATTERN = Pattern.compile("#(\\w+)");
        Matcher mat = MY_PATTERN.matcher(message);
        List<String> strs=new ArrayList<String>();
        while (mat.find()) {
            strs.add(mat.group(1));
        }
        String[] hashtags = new String[strs.size()];
        if (strs.size() > 0) {
            for(int i = 0; i<strs.size(); i++){
                Log.d("test", "hashtag: "+strs.get(i));
                hashtags[i] = strs.get(i);
            }
        }
        Log.d("test", "hashtag array size: "+ hashtags.length);
        return hashtags;
    }


    public class SearchTask extends AsyncTask<Void, Void, Boolean> {

        private final String[] mUser;
        private final String[] mHashtag;
        private String error;

        SearchTask(String[] user, String[] hashtag) {
            mUser = user;
            mHashtag = hashtag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                JsonObject json = new JsonObject();
                List<String> listUser = new ArrayList<String>(Arrays.asList(mUser));
                List<String> listHashtag = new ArrayList<String>(Arrays.asList(mHashtag));
                JsonArray arrayUser = new JsonArray();
                JsonArray arrayHashtag = new JsonArray();
                for (String next: listUser){
                    arrayUser.add(new JsonPrimitive(next));
                }
                for (String next: listHashtag){
                    arrayHashtag.add(new JsonPrimitive(next));
                }
                json.add("users", arrayUser);
                json.add("hashtags", arrayHashtag);

                JsonObject response = Factory.getMyInterface().chirpSearch(json);
                Log.d("test", json.toString());
                Log.d("test", "search working");
                Log.d("test", response.toString());

                chirps = new Chirps(response); // constructor for searched chirp
//                Log.d("otherusernametest", "trying to parse chirps into account");
                if (lochirps != null)
                    lochirps.clear();
                lochirps = chirps.getChirps();

//                Account.getInstance(response); // parse userId into singleton
                // Simulate network access.
            } catch (LambdaFunctionException lfe) {
                error = lfe.getDetails().toString();
                Log.e("test", lfe.getDetails(), lfe);
                Log.e("test", lfe.getDetails());
                return false;

            }
            Log.d("test", "search complete");
//            Log.d("test", Account.getAccount().getUser().getChirps().toString());

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.d("test", "FINISH Searching CHIRP");
            if (success) {
                populateListView();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }
    private void populateListView() {
        Log.d("chirptest", "1");
        ArrayAdapter<Chirp> adapter = new MyListAdapter();
        Log.d("chirptest", "2");
        ListView list = (ListView) findViewById(R.id.chirpsListView);
        Log.d("chirptest", "3");
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Chirp> {
        public MyListAdapter() {
            super(SearchActivity.this, R.layout.content_otherchirprelative, lochirps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.content_otherchirprelative, parent, false);
            }

            // Find the chirp to work with.
            final Chirp currentChirp = lochirps.get(position);
            Log.d("chirptest", Integer.toString(position));
            // Fill the view
//            ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
//            imageView.setImageResource(currentCar.getIconID());

            // ChirpUsername:
            TextView chirpUsernameText = (TextView) itemView.findViewById(R.id.chirpusername);
            chirpUsernameText.setText(currentChirp.getUserId());
            Log.d("viewuser", currentChirp.getUserId());
            chirpUsernameText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("otherusernametest", currentChirp.getUserId());
                    Intent myIntent = new Intent(SearchActivity.this, OtherProfileActivity.class);
                    myIntent.putExtra("key", currentChirp.getUserId()); //Optional parameters
                    SearchActivity.this.startActivity(myIntent);
                }
            });
            Log.d("chirptest", currentChirp.getUserId());
            Log.d("chirptest", "trying to get chirp username");
            // ChirpDescription:
            TextView chirpDescription = (TextView) itemView.findViewById(R.id.chirpDescription);
            chirpDescription.setText(currentChirp.getChirp());

            Log.d("chirptest", currentChirp.getChirp());
            // ChirpTimeStamp:
//            TextView chirpTimeStamp = (TextView) itemView.findViewById(R.id.chirpdate);
//            chirpTimeStamp.setText(currentChirp.getTimePosted().toString());
            return itemView;
        }
    }

}
