package jayed.triad.chirpchirp.classes;


import java.util.List;
import java.util.Map;

public class User {
    private String userId;
    private String password;
    public List<Chirp> chirps;
    public List<User> following;
    public List<User> followers;
    public String profilePicture;
    public Map userProfile;


    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
}