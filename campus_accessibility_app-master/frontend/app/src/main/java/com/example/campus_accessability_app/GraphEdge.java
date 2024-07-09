package com.example.campus_accessability_app;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/** Stores GPS coordinates of the points along a graph edge, between nodes A and B */
public class GraphEdge {
    private Node nodeA;
    private Node nodeB;
    private ArrayList<LatLng> points;

    public GraphEdge(Node firstNode, Node secondNode, ArrayList<LatLng> pointsArray) {
        nodeA = firstNode;
        nodeB = secondNode;
        points = pointsArray;
    }

    protected ArrayList<LatLng> getEdgePoints() {
        return points;
    }

    protected Node getNodeA() {
        return nodeA;
    }

    protected Node getNodeB(){return nodeB; }
}
