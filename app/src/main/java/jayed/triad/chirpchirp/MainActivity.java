package jayed.triad.chirpchirp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import jayed.triad.chirpchirp.classes.Account;
import jayed.triad.chirpchirp.classes.Chirp;
import jayed.triad.chirpchirp.classes.Chirps;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String accountId;
    private JsonObject chirpsInJSONObject;
    private String error;
    private List<Chirp> chirpsToDisplay;
    private TextView mUsername;
    private TextView mChirpDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: FloatingActionButton for new chirp.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //TODO: make the floating action button to redirect to the newChrip activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToNewChirpActivity();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * 1. get current user information
         * 2. using the info, get user's following list (list of users that current user is following)
         * 3. get all chirps from each user in the list
         * 4. sort them in chronological order
         * 5. display
         */

        // TODO: (1)
        accountId = Account.getAccount().getAccountId(); //TODO: assign this to a variable

        // TODO: (2)
        try {
            JsonObject json = new JsonObject();
            json.addProperty("userId", accountId);
            json.add("chirps", Account.getAccount().getUser().getChirps());
            chirpsInJSONObject = Factory.getMyInterface().chirpGetMyChirps(json);
        } catch (LambdaFunctionException lfe) {
            error = lfe.getDetails().toString();
        }
        Chirps chirpsObject = new Chirps(chirpsInJSONObject.getAsJsonArray());
        chirpsToDisplay = chirpsObject.getChirps();
        mUsername = (TextView) findViewById(R.id.username);
        mUsername.setText(Account.getAccount().getAccountId());

        mChirpDescription = (TextView) findViewById(R.id.chirpDescription);
        mChirpDescription.setText(chirpsToDisplay.get(0).getChirp());







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

        if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        } else if (id == R.id.nav_timeline) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.nav_settings) {
            // TODO: uncomment after implementation of SettingsActivity
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void redirectToNewChirpActivity() {
        startActivity(new Intent(getApplicationContext(), NewChirpActivity.class));
    }
}
