package jayed.triad.chirpchirp.classes;


import android.util.Log;

import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Account {
    private static Account theAccount;
    private User user;
    private String userId;
    private String email;
    private Date joinDate;
    /*
    private String password;
    public List<Chirp> chirps;
    public List<Account> following;
    public List<Account> followers;
    public String profilePicture;
    public Map userProfile;
    */

    private Account(JsonObject jsonObject) {
        setAccountId(jsonObject.getAsJsonObject("Item").get("userId").toString());
        setEmail(jsonObject.getAsJsonObject("Item").get("email").toString());
        setJoinDate(jsonObject.getAsJsonObject("Item").get("joinDate").getAsLong());
        parseUser(jsonObject);
    }

    /**
     * Gets instance of EventLog - creates it
     * if it doesn't already exist.
     * (Singleton Design Pattern)
     * @return  instance of EventLog
     * @param jsonObject
     * @return Account
     */
    public static Account getInstance(JsonObject jsonObject) { // static -> global point of access by EventLog.getInstance()
        if (theAccount == null)
            theAccount = new Account(jsonObject);
        Log.d("test", theAccount.getAccountId());
        Log.d("test", theAccount.getEmail());
        Log.d("test", theAccount.getJoinDate().toString());
        return theAccount;
    }

    public String getAccountId() {
        return userId;
    }

    private void setAccountId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return userId;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    private void setJoinDate(long joinDate) {
        this.joinDate = new Date(joinDate);
    }

    public User getUser() {
        return user;
    }

    public void parseUser(JsonObject jsonObject) {
        user = new User(jsonObject);
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

    public List<Account> getFollowing() {
        return following;
    }

    public void setFollowing(List<Account> following) {
        this.following = following;
    }

    public List<Account> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Account> followers) {
        this.followers = followers;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Map getAccountProfile() {
        return userProfile;
    }

    public void setAccountProfile(Map userProfile) {
        this.userProfile = userProfile;
    }
    */
}