package jayed.triad.chirpchirp.classes;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmychou516 on 16-04-03.
 */
public class Chirps {

    private List<Chirp> chirps;

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
    }



}
