package jayed.triad.chirpchirp.classes;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public List<Chirp> getChirps() {
        return chirps;
    }

    public void sortChirps() {
        Collections.sort(chirps, CHIRP_ORDER);
    }

}
