package jayed.triad.chirpchirp.classes;


import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
//    public static final String[] array1 = {"Apple", "Banana", "Cookie"};
//    public static final String[] array2 = {};

    private String userId;
    private Date joinDate;
    private Date lastActive;
    private String profilePicture; // store as URL
    private URL email;
    private String description;

//    private List<Chirp> chirps; // store as array of maps (with userId and chirpId)
//    private List<User> following; //store as list of gson array of userId
//    private List<User> followers; //store as list of gson array of userId
//    private List<Chirps> likedChirps; //store as list of gson array of userId

    private JsonArray chirps;
    private JsonArray following; //store as list of gson array of userId
    private JsonArray followers; //store as list of gson array of userId
    private JsonArray likedChirps; //store as list of gson array of userId

    public User(JsonObject jsonObject) {
        setUserId(jsonObject.getAsJsonObject("Item").get("userId").toString());
        setJoinDate(jsonObject.getAsJsonObject("Item").get("joinDate").getAsLong());
        setLastActive(jsonObject.getAsJsonObject("Item").get("joinDate").getAsLong());
        setEmail(jsonObject.getAsJsonObject("Item").get("email").toString());
        setProfilePicture(jsonObject.getAsJsonObject("Item").get("profilePicture").toString());
        setDescription(jsonObject.getAsJsonObject("Item").get("description").toString());
        setChirps(jsonObject.getAsJsonObject("Item").getAsJsonArray("chirps"));
        setFollowing(jsonObject.getAsJsonObject("Item").getAsJsonArray("following"));
        setFollowers(jsonObject.getAsJsonObject("Item").getAsJsonArray("followers"));
        setLikedChirps(jsonObject.getAsJsonObject("Item").getAsJsonArray("likedChirps"));
        //setChirps(jsonObject.getAsJsonObject("Item").get("chirps").getAsJsonArray());
//        setChirps(array1);
//        setChirps(array2);

        Log.d("test", this.getUserId());
        Log.d("test", this.getJoinDate().toString());
    }
    public String removeQuotes(String str) {
        str = str.replace("\"", "");
        return str;
    }

    public String getUserId() {
        return removeQuotes(userId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = new Date(joinDate);
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = new Date(lastActive);
    }

    public String getProfilePicture() {
        return removeQuotes(profilePicture);
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public URL getEmail() {
        return email;
    }

    public void setEmail(String email) {
        try {
            this.email = new URL(email);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getDescription() { return removeQuotes(description); }

    public void setDescription(String description) {
        this.description = description;
    }

    public JsonArray getChirps() {
        return chirps;
    }

    public void setChirps(JsonArray chirps) {
        this.chirps = chirps;
    }

    public JsonArray getFollowing() {
        return following;
    }

    public void setFollowing(JsonArray following) {
        this.following = following;
    }

    public JsonArray getFollowers() {
        return followers;
    }

    public void setFollowers(JsonArray followers) {
        this.followers = followers;
    }

    public JsonArray getLikedChirps() {
        return likedChirps;
    }

    public void setLikedChirps(JsonArray likedChirps) {
        this.likedChirps = likedChirps;
    }

// PARSERS BELOW

    public List<String> parseFollowers(JsonArray followers) {
        List<String> listofFollowers = new ArrayList<String>();
        for (JsonElement item : followers) {
            listofFollowers.add(item.toString());
        }
        return listofFollowers;
    }

    public List<String> parseFollowing(JsonArray following) {
        List<String> listofFollowing = new ArrayList<String>();
        for (JsonElement item : following) {
            listofFollowing.add(item.toString());
        }
        return listofFollowing;
    }

}