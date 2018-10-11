package com.example.charles.opencv.News.Model;

import java.util.List;

/***
 * Class for representing the RSS object, which holds a feed and a list of items
 */
public class RSS {
    public String status;
    public Feed feed;
    public List<Item> items;

    public RSS(String status, Feed feed, List<Item> items) {
        this.status = status;
        this.feed = feed;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}