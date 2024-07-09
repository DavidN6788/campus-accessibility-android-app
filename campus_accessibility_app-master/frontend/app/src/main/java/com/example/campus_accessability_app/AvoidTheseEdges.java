package com.example.campus_accessability_app;

import org.json.JSONArray;
import org.json.JSONObject;

public class AvoidTheseEdges {
    private JSONArray data = new JSONArray();

    public AvoidTheseEdges() { }

    public void addEdge(int startNode, int endNode) {
        JSONArray edge = new JSONArray();
        edge.put(startNode);
        edge.put(endNode);
        data.put(edge);
    }

    public JSONArray getAsJSONArray() {
        return data;
    }
}
