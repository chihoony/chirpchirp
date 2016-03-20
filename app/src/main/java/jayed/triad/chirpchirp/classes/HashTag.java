package jayed.triad.chirpchirp.classes;

import java.util.List;

/**
 * Created by edwardjihunlee on 16-03-20.
 */
public class HashTag {
    private String hashTagID;
    private List<Integer> chirpIDs;

    public HashTag() {}

    public HashTag(String hashTagID) {
        this.hashTagID = hashTagID;
    }

    public String getHashTagID() {
        return hashTagID;
    }

    public void setHashTagID(String hashTagID) {
        this.hashTagID = hashTagID;
    }

    public List<Integer> getChirpIDs() {
        return chirpIDs;
    }

    public void setChirpIDs(List<Integer> chirpIDs) {
        this.chirpIDs = chirpIDs;
    }
}
