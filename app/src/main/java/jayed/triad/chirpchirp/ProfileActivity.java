package jayed.triad.chirpchirp;

import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jayed.triad.chirpchirp.classes.Account;
import jayed.triad.chirpchirp.classes.Chirp;
import jayed.triad.chirpchirp.classes.Chirps;
import jayed.triad.chirpchirp.classes.ImageLoadTask;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ChirpsTask mChirps = null;
    private LikeTask mLike = null;
    private JsonArray myChirps;
    private TextView mUsername;
    private TextView mDescription;
    private TextView mFollower;
    private ImageButton mProfileImage;
    private TextView mChirpDescription;
    private Chirps chirps;
    private Config config = new Config();
    private String accessKey = config.getAccessKey();
    private String secretKey = config.getSecretKey();
    private AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
    private static Context mContext;
    private List<Chirp> lochirps;

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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Log.d("otherusernametest", "hello");
                startActivity(new Intent(getApplicationContext(), PostChirpActivity.class));
            }
        });

        Button b = (Button) findViewById(R.id.search_view_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initializing Drawer Layout and ActionBarToggle
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        Log.d("test", "attempting to set account Id");
        mUsername = (TextView)findViewById(R.id.username);
        String username = Account.getAccount().getAccountId();
        mUsername.setText(username);

        String description = Account.getAccount().getDescription();
        mDescription = (TextView)findViewById(R.id.description);
        mDescription.setText(description);

        mProfileImage = (ImageButton)findViewById(R.id.profileImageButton);
//        new ImageLoadTask(Account.getAccount().getUser().getProfilePicture(), mProfileImage).execute();

        mFollower = (TextView)findViewById(R.id.followerText);
        mFollower.setText(String.valueOf(Account.getAccount().getUser().getFollowers().size())  + " Followers");

        // TODO: download profile image from S3 and display on the Imagebutton

        // REDUNDANT
//        File profileImageFile = new File(this.getFilesDir().getAbsolutePath(), "temp_image");
//        mContext = getApplicationContext();
//        TransferUtility transferUtility = new TransferUtility(s3, mContext);
//        TransferObserver observer = transferUtility.download(
//                "chirpprofileimages",
//                Account.getAccount().getAccountId(),
//                profileImageFile
//        );

        new ImageLoadTask("https://s3.amazonaws.com/chirpprofileimages/" + Account.getAccount().getAccountId(), mProfileImage).execute();

//      populateChirplist();

        ChirpsTask mChirp = new ChirpsTask();
        mChirp.execute();
        //Log.d("test", "THIS PRINTS " + myChirps.toString());

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

    //    THIS IS WHERE THE PROFILE ACTIVITY GETS CHIRPS ASYNC
    public class ChirpsTask extends AsyncTask<Void, Void, Boolean> {

        private String error;

        ChirpsTask() {
            lochirps = new ArrayList<Chirp>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                JsonObject currentUser = new JsonObject();
                currentUser.addProperty("userId", Account.getAccount().getAccountId());
                JsonArray chirpsJson = Account.getAccount().getUser().getChirps();
                Log.d("patest", chirpsJson.toString());
                currentUser.add("chirps", chirpsJson);
                Log.d("otherusernametest", currentUser.toString());
//                myChirps = new JsonArray();
                Log.d("viewuser", Factory.getMyInterface().chirpGetMyChirps(currentUser).toString());
                myChirps = Factory.getMyInterface().chirpGetMyChirps(currentUser);
//                JsonObject response = Factory.getMyInterface().chirpGetMyChirps(chirpsJson);
                Log.d("patest", "try to get list of user's own chirps");
                Log.d("patest", myChirps.toString());
                chirps = new Chirps(myChirps);
//                Log.d("patest", "trying to parse chirps into account");
                if (lochirps != null)
                lochirps.clear();
                lochirps = chirps.getChirps();
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
            if (success) {
                populateListView();
            }
        }

    }

    private class MyListAdapter extends ArrayAdapter<Chirp> {
        public MyListAdapter() {
            super(ProfileActivity.this, R.layout.content_chirprelative, lochirps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.content_chirprelative, parent, false);
            }

            // Find the car to work with.
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
                    Log.d("viewuser", currentChirp.getUserId());
                    Intent myIntent = new Intent(ProfileActivity.this, OtherProfileActivity.class);
                    myIntent.putExtra("key", currentChirp.getUserId()); //Optional parameters
                    ProfileActivity.this.startActivity(myIntent);
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
