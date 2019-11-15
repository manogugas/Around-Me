package com.example.aroundme;

import org.json.JSONArray;

import java.lang.reflect.Array;

public class ModelPost
{
    private String id;
    private double relevance;
    private String title;
    private String description;
    private String category;
    private String labels;
    private int rank;
    private int localRank;
    private String formatted_address;
    private String entity_id;
    private String type;
    private String name;
    private int duration;
    private String start;
    private String end;
    private  String updated;
    private double[] location = new double[2]; //    "location": [25.277869,54.684158]

    public  ModelPost() {}

    public ModelPost(String id, double relevance, String title, String description, String category,
            String labels, int rank, int localRank, String formatted_address, String entity_id,
            String type, String name, int duration, String start, String end, String updated,
            double[] location)
    {
        this.id = id;
        this.relevance = relevance;
        this.title = title;
        this.description = description;
        this.category = category;
        this.labels = labels;
        this.rank = rank;
        this.localRank = localRank;
        this.formatted_address = formatted_address;
        this.entity_id = entity_id;
        this.type = type;
        this.name = name;
        this.duration = duration;
        this.start = start;
        this.end = end;
        this.updated = updated;
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setLocalRank(int localRank) {
        this.localRank = localRank;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public double getRelevance() {
        return relevance;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getLabels() {
        return labels;
    }

    public int getRank() {
        return rank;
    }

    public int getLocalRank() {
        return localRank;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getUpdated() {
        return updated;
    }

    public double[] getLocation() {
        return location;
    }
}


/*
{
  "relevance": 0.90712607,
  "id": "2WuuLsboGXwfoRW5uA",
  "title": "Jardin Exotique",
  "description": "",
  "category": "concerts",
  "labels": [
    "concert",
    "music"
  ],
  "rank": 0,
  "local_rank": 0,
  "entities": [
    {
      "formatted_address": "4 Islandijos gatvė\n01401 Vilnius\nLithuania",
      "entity_id": "KafEa783tsx93fZwZXa5NC",
      "type": "venue",
      "name": "Opium Club"
    }
  ],
  "duration": 25200,
  "start": "2019-11-09T21:00:00Z",
  "end": "2019-11-10T04:00:00Z",
  "updated": "2019-10-30T12:01:03Z",
  "first_seen": "2019-10-30T11:30:14Z",
  "timezone": "Europe/Vilnius",
  "location": [
    25.277869,
    54.684158
  ],
  "scope": "locality",
  "country": "LT",
  "place_hierarchies": [
    [
      "6295630",
      "6255148",
      "597427",
      "864485",
      "593118",
      "593116"
    ]
  ],
  "state": "active"
},


*/