package com.example.campus_accessability_app;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

public class Report {
    int startNode;
    int endNode;
    String comment;
    ZonedDateTime timestamp;
    String username;

    public Report(int startNode, int endNode, String comment, Double timestamp, String username) {
        //this.startNode = MapFrontend.nodes.get(startNode);
        //this.endNode = MapFrontend.nodes.get(endNode);
        this.startNode = startNode;
        this.endNode = endNode;
        this.comment = comment;
        this.timestamp = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Math.round(timestamp * 1000)), ZoneId.systemDefault());
        this.username = username;
    }


    public String getStartNodeName(){
        String start = MapFrontend.nodeNames[startNode-1];
        return start;
    }

    public String getEndNodeName(){
        String end = MapFrontend.nodeNames[endNode-1];
        return end;
    }

    public LocalDate getDate(){
        return timestamp.toLocalDate();
    }

    public LocalTime getTimeStamp(){
        return timestamp.toLocalTime();
    }
}
