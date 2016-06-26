package jayed.triad.chirpchirp.classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwardjihunlee on 16-03-20.
 */
public class Chirp {
    private int chirpId;
    private int reChirpId;
    private String chirp;
    private String userId;
    private int viewCount;
    private boolean showStatus;
    private String timePosted;
    private List<String> hashTag;
    private List<String> likeChirpers;
    private List<String> reChirpers;



    /**
     * {
     "hashtag": [],
     "reChirpId": 0,
     "likeChirpers": {
     "datatype": "SS",
     "contents": {
     "FirstChirper": "FirstChirper"
     }
     },
     "chirpId": 7,
     "chirp": "hihihihihi",
     "userId": "sample1",
     "show": true,
     "reChirpers": {
     "datatype": "SS",
     "contents": {
     "0": "0"
     }
     }
     }
     * @param json
     * TODO: add the date stuff
     */
    public Chirp(JsonObject json) {
        super();
        this.hashTag = new ArrayList<>();
        this.likeChirpers = new ArrayList<>();
        this.reChirpers = new ArrayList<>();
        setHashtag(json.getAsJsonArray("hashtag")); // need to loop through to parse
        setReChirpId(json.get("reChirpId").getAsInt());
        setLikeChirpers(json.getAsJsonArray("likeChirpers")); // need to loop through to parse
        setChirpId(json.get("chirpId").getAsInt());
        setChirp(json.get("chirp").getAsString());
        setUserId(json.get("userId").getAsString());
        setShowStatus(json.get("toShow").getAsBoolean());
        setReChirpers(json.getAsJsonArray("reChirpers")); // need to loop through to parse
        if (json.get("postDate") != null)
        setTimePosted(json.get("postDate").getAsString());
        else setTimePosted(null);
    }
    public Chirp(String username, String description, String NOW){
        this.userId = username;
        this.chirp = description;
        this.timePosted = NOW;
        this.likeChirpers = null;
    }


    public Chirp(String userId, String content) {
        super();
        this.userId = userId;
        this.chirp = content;
    }

    public List<String> getHashtag() {
        return this.hashTag;
    }

    public void setHashtag(JsonArray hashtag) {
        for (JsonElement next : hashtag) {
            this.hashTag.add(next.getAsString());
        }
    }

    public List<String> getLikeChirpers() {
        return this.likeChirpers;
    }

    public void setLikeChirpers(JsonArray likeChirpers) {
        for (JsonElement next : likeChirpers) {
            this.likeChirpers.add(next.getAsString());
        }
    }

    public List<String> getReChirpers() {
        return this.likeChirpers;
    }

    public void setReChirpers(JsonArray reChirpers) {
        for (JsonElement next : reChirpers) {
            this.reChirpers.add(next.getAsString());
        }
    }

    public String getChirp() {
        return this.chirp;
    }

    public void setChirp(String chirp) {
        this.chirp = chirp;
    }

    public int getReChirpId() {
        return this.reChirpId;
    }

    public void setReChirpId(int reChirpId) {
        this.reChirpId = reChirpId;
    }


    public int getChirpId() {
        return chirpId;
    }

    public void setChirpId(int chirpId) {
        this.chirpId = chirpId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        if (timePosted != null)
        this.timePosted = timePosted;
        else this.timePosted = "Edward, the ruler of time";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chirp chirp = (Chirp) o;

        return chirpId == chirp.chirpId;

    }

    @Override
    public int hashCode() {
        return chirpId;
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
