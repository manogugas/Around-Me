package com.example.aroundme;

import java.io.Serializable;
import java.util.List;

public class ListItem implements Serializable, Comparable<ListItem> {
    private String title;
    private int imageId;
    private String description;

    public  ListItem(){}

    public ListItem(String title, int imageId, String description)
    {
        this.title = title;
        this.imageId = imageId;
        this.description = description;
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

}
