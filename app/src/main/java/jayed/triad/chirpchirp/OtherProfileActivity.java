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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jayed.triad.chirpchirp.classes.Account;
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
    private LikeTask mLike = null;
    private User otherUser;
    private String otherUsername;
    private JsonArray otherChirps;
    private TextView mUsername;
    private TextView mDescription;
    private ImageButton mProfileImage;
    private TextView mChirpDescription;
    private Chirps chirps;
    private List<Chirp> lochirps;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        otherUsername = intent.getStringExtra("key");
        Log.d("otherusernametest", "otherUsername");
        super.onCreate(savedInstanceState);
        setContentView(R    .layout.activity_otherprofile);
//        setContentView(R.layout.content_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                for(Chirp chirp: chirps.getChirps()) {
//                    Log.d("otherusernametest", chirp.getChirp());
//                }
//                Log.d("chirptest", Integer.toString(chirps.getChirps().size()));
//                populateListView();
                Log.d("test", "Other PROFILE ACTIVITY");
            }
        });

        lochirps = new ArrayList<Chirp>();
        otherChirps = new JsonArray();


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
        UserTask mUserTask = new UserTask();
        mUserTask.execute();

        //Log.d("test", "THIS PRINTS " + otherChirps.toString());

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                drawerView.bringToFront();
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


    }

    private void populateListView() {
        Log.d("chirptest", "1");
        ArrayAdapter<Chirp> adapter = new MyListAdapter();
        Log.d("chirptest", "2");
        ListView list = (ListView) findViewById(R.id.chirpsListView);
        Log.d("chirptest", "3");
        list.setAdapter(adapter);

        ProgressBar mProgress= (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setVisibility(View.GONE);
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

        if (id == R.id.nav_timeline) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), ProfileSettingsActivity.class));
        } else if (id == R.id.nav_search) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
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
                Log.d("testtest", this.otherChirps.toString());
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
            super(OtherProfileActivity.this, R.layout.content_otherchirprelative, lochirps);
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
            TextView chirpTimeStamp = (TextView) itemView.findViewById(R.id.chirptimeposted);
            chirpTimeStamp.setText(currentChirp.getTimePosted());
            Log.d("chirptest", currentChirp.getTimePosted());

            final TextView chirpLikeCount = (TextView) itemView.findViewById(R.id.likecount);
            if (!currentChirp.getLikeChirpers().isEmpty())
                chirpLikeCount.setText(String.valueOf(currentChirp.getLikeChirpers().size()));

            else chirpLikeCount.setText(String.valueOf(0));

            TextView chirprechirp = (TextView) itemView.findViewById(R.id.rechirp);
            if (currentChirp.getReChirpId() != 0) {
                chirprechirp.setVisibility(View.VISIBLE);
            }

            final ImageView likeButton = (ImageView) itemView.findViewById(R.id.likebutton);
            if (currentChirp.getLikeChirpers().contains(Account.getAccount().getAccountId())) {
                likeButton.setImageResource(R.mipmap.likedheart);
            } else {
                likeButton.setImageResource(R.mipmap.unlikedheart);
            }

            likeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//                        if ()
                    boolean toLike;
//                        if (!currentChirp.getLikeChirpers().isEmpty())
//                        Log.d("like", "1" + currentChirp.getLikeChirpers().toString());
                    if (currentChirp.getLikeChirpers().contains(Account.getAccount().getAccountId())) {
                        likeButton.setImageResource(R.mipmap.unlikedheart);
                        currentChirp.getLikeChirpers().remove(Account.getAccount().getAccountId());
                        toLike = false;
                    } else {
                        likeButton.setImageResource(R.mipmap.likedheart);
                        currentChirp.getLikeChirpers().add(Account.getAccount().getAccountId());
                        toLike = true;
                    }
//                        if (!currentChirp.getLikeChirpers().isEmpty())
//                        Log.d("like", "2" + currentChirp.getLikeChirpers().toString());
                    chirpLikeCount.setText(String.valueOf(currentChirp.getLikeChirpers().size()));
                    mLike = new LikeTask(currentChirp.getChirpId(), toLike);
                    mLike.execute((Void) null);
                }
            });

            return itemView;
        }
    }

    public class LikeTask extends AsyncTask<Void, Void, Boolean> {

        private String error;
        private int chirpId;
        private boolean likeState;
        LikeTask(int id, boolean like) {
            chirpId = id;
            likeState = like;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                JsonObject json = new JsonObject();
                json.addProperty("userId", Account.getAccount().getAccountId());
                json.addProperty("chirpId", chirpId);

                if (likeState) {
                    Factory.getMyInterface().chirpLike(json);
                } else {
                    Factory.getMyInterface().chirpUnlike(json);
                }
                // Simulate network access.
            } catch (LambdaFunctionException lfe) {
                error = lfe.getDetails().toString();
                Log.e("patest", lfe.getDetails(), lfe);
                Log.e("patest", lfe.getDetails());
                Log.e("patest", lfe.getDetails().toString());

            }
            Log.d("patest", "get my chirps passed");
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

    }
}

