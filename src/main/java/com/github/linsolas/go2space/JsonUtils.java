package com.github.linsolas.go2space;

import com.github.amsacode.predict4java.TLE;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonUtils {

    public static String toJson(TLE tle) {
        Gson gson = new Gson();
        return gson.toJson(tle);
    }

    public static String toJson(TLE tle, String source) {
        Gson gson = new Gson();
        JsonElement json = gson.toJsonTree(tle);
        json.getAsJsonObject().addProperty("source", source);
        return json.toString();
    }

}
