package jayed.triad.chirpchirp.classes;

import java.util.Date;

/**
 * Created by edwardjihunlee on 16-03-20.
 */
public class Chirp {
    private int chirpID;
    private int reChirp;
    private String content;
    private String userID;
    private int viewCount;
    private boolean showStatus;
    private Date timePosted;

    public Chirp() {}

    public Chirp(String userID, String content) {
        this.userID = userID;
        this.content = content;
    }

    public int getChirpID() {
        return chirpID;
    }

    public void setChirpID(int chirpID) {
        this.chirpID = chirpID;
    }

    public int getReChirp() {
        return reChirp;
    }

    public void setReChirp(int reChirp) {
        this.reChirp = reChirp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isShowStatus() {
        return showStatus;
    }

    public void setShowStatus(boolean showStatus) {
        this.showStatus = showStatus;
    }

    public Date getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(Date timePosted) {
        this.timePosted = timePosted;
    }


//    private String firstName;
//    private String lastName;
//
//    public NameInfo() {}
//
//    public NameInfo(String firstName, String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
}
