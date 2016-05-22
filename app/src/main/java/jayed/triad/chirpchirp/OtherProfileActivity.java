package jayed.triad.chirpchirp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jayed.triad.chirpchirp.classes.Chirp;
import jayed.triad.chirpchirp.classes.Chirps;
import jayed.triad.chirpchirp.classes.ImageLoadTask;
import jayed.triad.chirpchirp.classes.User;

/**
 * Created by edwardjihunlee on 16-05-05.
 */
public class OtherProfileActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

    private ChirpsTask mChirps = null;
    private UserTask mUserTask = null;
    private User otherUser;
    private String otherUsername;
    private JsonArray otherChirps;
    private TextView mUsername;
    private TextView mDescription;
    private ImageButton mProfileImage;
    private TextView mChirpDescription;
    private Chirps chirps;
    private List<Chirp> lochirps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        otherUsername = intent.getStringExtra("key");
        Log.d("otherusernametest", "otherUsername");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        setContentView(R.layout.content_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                for(Chirp chirp: chirps.getChirps()) {
                    Log.d("otherusernametest", chirp.getChirp());
                }
                Log.d("chirptest", Integer.toString(chirps.getChirps().size()));
            }
        });

        lochirps = new ArrayList<Chirp>();
        otherChirps = new JsonArray();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Log.d("test", "attempting to set account Id");
//        mUsername = (TextView)findViewById(R.id.username);
//        String username = otherUser.getUserId();
//        mUsername.setText(username);
//
//        String description = otherUser.getDescription();
//        mDescription = (TextView)findViewById(R.id.description);
//        mDescription.setText(description);
//
//        mProfileImage = (ImageButton)findViewById(R.id.profileImageButton);
//        new ImageLoadTask(otherUser.getProfilePicture(), mProfileImage).execute();

//      populateChirplist();
        mUserTask = new UserTask();
        mUserTask.execute();

        //Log.d("test", "THIS PRINTS " + otherChirps.toString());

    }

    private void populateListView() {
        Log.d("chirptest", "1");
        ArrayAdapter<Chirp> adapter = new MyListAdapter();
        Log.d("chirptest", "2");
        ListView list = (ListView) findViewById(R.id.chirpsListView);
        Log.d("chirptest", "3");
        list.setAdapter(adapter);
    }

//    private void populateChirplist() {
//        lochirps.add(new Chirp("Jimmy", "0"));
//        lochirps.add(new Chirp("Ray", "1"));
//        lochirps.add(new Chirp("Edward", "2"));
//        lochirps.add(new Chirp("Jayed", "3"));
//        lochirps.add(new Chirp("Triad", "4"));
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class UserTask extends AsyncTask<Void, Void, Boolean> {
        private String error;

        UserTask() {

        }
        @Override
        protected Boolean doInBackground(Void... param) {
            try {
                JsonObject otherJson = new JsonObject();
                otherJson.addProperty("userId", otherUsername);
                Log.d("otherusernametest", otherJson.toString());
                otherUser = new User(Factory.getMyInterface().chirpGetUser(otherJson));
            }
            catch (LambdaFunctionException lfe) {
                error = lfe.getDetails().toString();
                Log.e("otherusernametest", lfe.getDetails(), lfe);
            }

            Log.d("otherusernametest", "got other User information");
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                if (lochirps != null)
                lochirps.clear();
                mChirps = new ChirpsTask(otherChirps);
                mChirps.execute();
                Log.d("test", "attempting to set account Id");
                mUsername = (TextView) findViewById(R.id.username);
                String username = otherUser.getUserId();
                mUsername.setText(username);

                String description = otherUser.getDescription();
                mDescription = (TextView) findViewById(R.id.description);
                mDescription.setText(description);

                mProfileImage = (ImageButton) findViewById(R.id.profileImageButton);
                new ImageLoadTask(otherUser.getProfilePicture(), mProfileImage).execute();
            }
        }
    }

    //    THIS IS WHERE THE PROFILE ACTIVITY GETS CHIRPS ASYNC
    public class ChirpsTask extends AsyncTask<Void, Void, Boolean> {

        private String error;
        private JsonArray otherChirps;

        ChirpsTask(JsonArray otherChirps) {
            lochirps = new ArrayList<Chirp>();
            this.otherChirps = otherChirps;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                JsonObject currentUser = new JsonObject();
                currentUser.addProperty("userId", otherUser.getUserId());
                JsonArray chirpsJson = otherUser.getChirps();
                currentUser.add("chirps", chirpsJson);
                Log.d("otherusernametest", currentUser.toString());
                this.otherChirps = Factory.getMyInterface().chirpOtherUserChirps(currentUser);
//                JsonObject response = Factory.getMyInterface().chirpGetotherChirps(chirpsJson);
                Log.d("otherusernametest", "try to get list of user's own chirps");
                Log.d("otherusernametest1", otherChirps.toString());
                chirps = new Chirps(otherChirps);
//                Log.d("otherusernametest", "trying to parse chirps into account");
                if (lochirps != null)
                lochirps.clear();
                lochirps = chirps.getChirps();
                // Simulate network access.
            } catch (LambdaFunctionException lfe) {
                error = lfe.getDetails().toString();
                Log.e("otherusernametest", lfe.getDetails(), lfe);
                Log.e("otherusernametest", lfe.getDetails());
                Log.e("otherusernametest", lfe.getDetails().toString());

            }
            Log.d("otherusernametest", "get my chirps passed");
            Log.d("otherusernametest", lochirps.get(0).getChirp());
            Log.d("otherusernametest1", lochirps.get(1).getUserId());
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                populateListView();
            }
        }

    }

    private class MyListAdapter extends ArrayAdapter<Chirp> {
        public MyListAdapter() {
            super(OtherProfileActivity.this, R.layout.content_chirprelative, lochirps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.content_chirprelative, parent, false);
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
            chirpUsernameText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("otherusernametest", currentChirp.getUserId());
                    Intent myIntent = new Intent(OtherProfileActivity.this, OtherProfileActivity.class);
                    myIntent.putExtra("key", currentChirp.getUserId()); //Optional parameters
                    OtherProfileActivity.this.startActivity(myIntent);
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
