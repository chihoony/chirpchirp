package jayed.triad.chirpchirp.classes;


import android.util.Log;

import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class User {
    private static User theUser;
    private String userId;
    private Date joinDate;
    /*
    private String password;
    public List<Chirp> chirps;
    public List<User> following;
    public List<User> followers;
    public String profilePicture;
    public Map userProfile;
    */

    private User(JsonObject jsonObject) {
        setUserId(jsonObject.getAsJsonObject("Item").get("userId").toString());
        setJoinDate(jsonObject.getAsJsonObject("Item").get("joinDate").getAsLong());
    }

    /**
     * Gets instance of EventLog - creates it
     * if it doesn't already exist.
     * (Singleton Design Pattern)
     * @return  instance of EventLog
     * @param jsonObject
     * @return User
     */
    public static User getInstance(JsonObject jsonObject) { // static -> global point of access by EventLog.getInstance()
        if (theUser == null)
            theUser = new User(jsonObject);
        Log.d("test", theUser.getUserId());
        Log.d("test", theUser.getJoinDate().toString());
        return theUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = new Date(joinDate);
        this.userId = userId;
    }

    /*
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Chirp> getChirps() {
        return chirps;
    }

    public void setChirps(List<Chirp> chirps) {
        this.chirps = chirps;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Map getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Map userProfile) {
        this.userProfile = userProfile;
    }
    */
}