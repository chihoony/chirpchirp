package jayed.triad.chirpchirp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import jayed.triad.chirpchirp.classes.Account;
import jayed.triad.chirpchirp.classes.Chirp;
import jayed.triad.chirpchirp.classes.Chirps;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LikeTask mLike = null;

    private MainTask mMainTask = null;

    private Chirps chirps;
    private List<Chirp> lochirps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);

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

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_timeline:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;
                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), ProfileSettingsActivity.class));
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    default:
                        return true;

                }
            }
        });

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

//        if (Account.getAccount().getUser().getFollowing().size() != 0) {
            MainTask mChirp = new MainTask();
            mChirp.execute();
//        }


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

    public class MainTask extends AsyncTask<Void, Void, Boolean> {

        private String error;

        MainTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                JsonObject json = new JsonObject();
                JsonArray arrayUser = Account.getAccount().getUser().getFollowing();
//                JsonArray arrayHashtag = new JsonArray();
//                for (String next: listUser){
//                    arrayUser.add(new JsonPrimitive(next));
//                }
//                for (String next: listHashtag){
//                    arrayHashtag.add(new JsonPrimitive(next));
//                }
                json.add("following", arrayUser);
//                json.add("hashtags", arrayHashtag);

                JsonArray response = Factory.getMyInterface().chirpMain(json);
//                Log.d("test", json.toString());
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

        ProgressBar mProgress= (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setVisibility(View.GONE);
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
