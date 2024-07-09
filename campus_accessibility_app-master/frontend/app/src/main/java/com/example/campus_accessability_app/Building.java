package com.example.campus_accessability_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class Building {
    String name;
    Entrance[] entrances;
    Lift[] lifts;
    String[] toiletLocations;

    public Building(String buildingName, JSONObject buildingInfo) {
        name = buildingName;

        try {
            JSONArray _entrances = buildingInfo.getJSONArray("entrances");
            entrances = new Entrance[_entrances.length()];
            for (int i = 0; i < _entrances.length(); i++){
                JSONObject _entrance = _entrances.getJSONObject(i);
                entrances[i] = new Entrance(_entrance.getString("location"), _entrance.getString("steps"), _entrance.getString("doorwidth"), _entrance.getString("doorautomatic"));
            }

            JSONArray _lifts = buildingInfo.getJSONArray("lifts");
            lifts = new Lift[_lifts.length()];
            for (int i = 0; i < _lifts.length(); i++){
                JSONObject _lift = _lifts.getJSONObject(i);
                lifts[i] = new Lift(_lift.getString("location"), _lift.getString("dimensions"));
            }

            JSONArray _toiletlocations = buildingInfo.getJSONArray("toiletlocations");
            toiletLocations = new String[_toiletlocations.length()];
            for (int i = 0; i < _toiletlocations.length(); i++){
                toiletLocations[i] = _toiletlocations.getString(i);
            }
        } catch (Exception e) {
            // TODO: Handle errors better
            Log.e("myapp", Log.getStackTraceString(e));
        }
    }
}
