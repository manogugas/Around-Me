package com.example.aroundme;

import android.location.Location;

import java.io.Serializable;
import java.util.List;

public class ListItem implements Serializable, Comparable<ListItem> {
    private String title;
    private int imageId;
    private String description;
    private String category;
    private String placeName;
    private String startTime;
    private String endTime;
    private double[] location;


    public  ListItem(){}

    public ListItem(String title, int imageId, String description, String category, String placeName, String startTime, String endTime, double[] location)
    {
        this.title = title;
        this.imageId = imageId;
        this.description = description;
        this.category = category;
        this.placeName = placeName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    @Override
    public int compareTo(ListItem i)
    {
        return getTitle().compareTo(i.getTitle());
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getImageId()
    {
        return imageId;
    }

    public void  setImageId(int imageId)
    {
        this.imageId = imageId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }
}



/*
{"relevance":0.7062564,
"id":"fV2ZEwYPGz7o8RFmBe",
"title":"Tarp Dviejų Aušrų: D.Tiffany",
"description":"",
"category":"concerts",
"labels":["concert","music"],
"rank":38,
"local_rank":55,
"entities":[{"formatted_address":"Kaunas\nLithuania","entity_id":"cq4MNr6jGeSj69ScD6when","type":"venue","name":"Lizdas"}],
"duration":28800,
"start":"2019-11-08T21:00:00Z",
"end":"2019-11-09T05:00:00Z",
"updated":"2019-11-12T09:16:35Z",
"first_seen":"2019-11-12T09:11:49Z",
"timezone":"Europe\/Vilnius",
"location":[23.920506,54.89642],
"scope":"locality",
"country":"LT",
"place_hierarchies":[["6295630","6255148","597427","864477","598318","598316"]],
"state":"active"}
*/