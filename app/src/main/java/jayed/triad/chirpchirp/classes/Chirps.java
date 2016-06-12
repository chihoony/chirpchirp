package jayed.triad.chirpchirp.classes;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jimmychou516 on 16-04-03.
 */
public class Chirps {

    private List<Chirp> chirps;
    private static final Comparator<Chirp> CHIRP_ORDER =
            new Comparator<Chirp>() {
                public int compare(Chirp c1, Chirp c2) {
                    return (c1.getChirpId() > c2.getChirpId() ? -1 :
                            (c1.getChirpId() == c2.getChirpId() ? 0 : 1));
                }
            };

    public Chirps(JsonArray listChirps) {
        chirps = new ArrayList<>();
        if (listChirps.size() != 0) {
            for (JsonElement next : listChirps) {
                Chirp chirp = new Chirp(next.getAsJsonObject());
                chirps.add(chirp);
            }
        }

        for (Chirp next : chirps) {
            Log.d("test", next.getChirp());
        }
        sortChirps();
    }

    public Chirps(JsonObject result) { // constructor for searched chirps
        chirps = new ArrayList<>();
        //parsing the chirps
        JsonArray userChirpJson = result.getAsJsonArray("returnedUserChirp");
        JsonArray hashtagChirpJson = result.getAsJsonArray("returnedHashtagChirp");
        List<Chirp> userChirps = new ArrayList<>();
        List<Chirp> hashtagChirps = new ArrayList<>();
        if (userChirpJson.size() != 0) {
            for (JsonElement next : userChirpJson) {
                Chirp chirp = new Chirp(next.getAsJsonObject());
                userChirps.add(chirp);
                Log.d("test", "userChirp called: "+chirp.getUserId());
            }
        }
        if (hashtagChirpJson.size() != 0) {
            for (JsonElement next : hashtagChirpJson) {
                Chirp chirp = new Chirp(next.getAsJsonObject());
                hashtagChirps.add(chirp);
                Log.d("test", "hashtag called: "+chirp.getUserId());
            }
        }

        if (userChirps.size() == 0){
            Collections.sort(hashtagChirps, CHIRP_ORDER);
            chirps = hashtagChirps;
        } else if (hashtagChirps.size() == 0) {
            Collections.sort(userChirps, CHIRP_ORDER);
            chirps = userChirps;
        } else {
            // if userChirps contain hashtag, then add the chirp to list
            Set<Chirp> chirpSet = new HashSet<>();
            for (Chirp next : hashtagChirps) {
                if (userChirps.contains(next)) {
                    chirpSet.add(next);
                }
            }

            List<Chirp> returnedChirps = new ArrayList<>();
            for (Chirp toAdd : chirpSet) {
                returnedChirps.add(toAdd);
            }

            Collections.sort(returnedChirps, CHIRP_ORDER);
            chirps = returnedChirps;
        }
    }

    public List<Chirp> getChirps() {
        return chirps;
    }

    public void sortChirps() {
        Collections.sort(chirps, CHIRP_ORDER);
    }

}
