package com.example.campus_accessability_app;

import com.google.android.gms.maps.model.LatLng;

public class Node {
    protected int nodeNumber;
    protected LatLng coordinatesLatLng;
    protected String nodeName;

    public Node(int nodeNum, LatLng coordinates, String name) {
        nodeNumber = nodeNum;
        coordinatesLatLng = coordinates;
        nodeName = name;
    }
}
