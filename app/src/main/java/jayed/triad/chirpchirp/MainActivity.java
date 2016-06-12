package jayed.triad.chirpchirp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jayed.triad.chirpchirp.ProfileActivity;
import jayed.triad.chirpchirp.ProfileSettingsActivity;
import jayed.triad.chirpchirp.R;
import jayed.triad.chirpchirp.classes.Chirp;
import jayed.triad.chirpchirp.classes.Chirps;

public class MainActivity extends AppCompatActivity {

    private MainTask mMainTask = null;
    private EditText mSearchView;
    private String searchContent = "";

    private Chirps chirps;
    private List<Chirp> lochirps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainTask mChirp = new MainTask();
        mChirp.execute((Void) null);
    }

    public class MainTask extends AsyncTask<Void, Void, Boolean> {

//        private final String[] mUser;
//        private final String[] mHashtag;
        private String error;

        MainTask() {
//            mUser = user;
//            mHashtag = hashtag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                JsonObject json = new JsonObject();
//                List<String> listUser = new ArrayList<String>(Arrays.asList(mUser));
//                List<String> listHashtag = new ArrayList<String>(Arrays.asList(mHashtag));
                JsonArray arrayUser = new JsonArray();
                JsonArray arrayHashtag = new JsonArray();
//                for (String next: listUser){
//                    arrayUser.add(new JsonPrimitive(next));
//                }
//                for (String next: listHashtag){
//                    arrayHashtag.add(new JsonPrimitive(next));
//                }
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
            super(MainActivity.this, R.layout.content_otherchirprelative, lochirps);
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
                    Intent myIntent = new Intent(MainActivity.this, OtherProfileActivity.class);
                    myIntent.putExtra("key", currentChirp.getUserId()); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
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
