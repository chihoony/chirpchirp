package jayed.triad.chirpchirp;

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
import android.widget.ImageButton;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jayed.triad.chirpchirp.classes.Account;
import jayed.triad.chirpchirp.classes.Chirp;
import jayed.triad.chirpchirp.classes.Chirps;
import jayed.triad.chirpchirp.classes.ImageLoadTask;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ChirpsTask mChirps = null;
    private JsonArray myChirps;
    private TextView mUsername;
    private TextView mDescription;
    private ImageButton mProfileImage;
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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                for(Chirp chirp: chirps) {
                    Log.d("patest", chirp.getChirp());
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.d("test", "attempting to set account Id");
        mUsername = (TextView)findViewById(R.id.username);
        String username = Account.getAccount().getAccountId();
        mUsername.setText(username);

        String description = Account.getAccount().getDescription();
        mDescription = (TextView)findViewById(R.id.description);
        mDescription.setText(description);

        mProfileImage = (ImageButton)findViewById(R.id.profileImageButton);
        new ImageLoadTask(Account.getAccount().getUser().getProfilePicture(), mProfileImage).execute();

        String error;

        ChirpsTask mChirp = new ChirpsTask();
        mChirp.execute((Void) null);
        //Log.d("test", "THIS PRINTS " + myChirps.toString());

    }

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

//    THIS IS WHERE THE PROFILE ACTIVITY GETS CHIRPS ASYNC
    public class ChirpsTask extends AsyncTask<Void, Void, Boolean> {

        private String error;

        ChirpsTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                JsonObject currentUser = new JsonObject();
                currentUser.addProperty("userId", Account.getAccount().getAccountId());
                JsonArray chirpsJson = Account.getAccount().getUser().getChirps();
                currentUser.add("chirps", chirpsJson);
                myChirps = Factory.getMyInterface().chirpGetMyChirps(currentUser);
//                JsonObject response = Factory.getMyInterface().chirpGetMyChirps(chirpsJson);
                Log.d("patest", "try to get list of user's own chirps");
                Log.d("patest", myChirps.toString());
                chirps = new Chirps(myChirps);
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
