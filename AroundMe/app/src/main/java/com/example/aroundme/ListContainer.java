package com.example.aroundme;

import java.util.List;

public class ListContainer extends ListItem
{
    List<ListItem> listItems;


    public  ListContainer(List<ListItem> items)
    {
        listItems = items;
    }

    public List<ListItem> getItems()
    {
        return listItems;
    }

    public void setListItems(List<ListItem> items)
    {
        listItems = items;
    }

}
