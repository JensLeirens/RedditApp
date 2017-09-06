package mafken.redditapp;

import android.text.format.DateUtils;

public class Subreddit {
    private String name;
    private String title;
    private String author;
    private long created;
    private String selftext;
    private String thumbnail;
    private int num_comments;
    private String url;
    private int ups;

    public Subreddit(String name, String title, String author, long created, String selftext, String thumbnail, int num_comments, String url, int ups) {
        this.name = name;
        this.title = title;
        this.author = author;
        this.created = created;
        this.selftext = selftext;
        this.thumbnail = thumbnail;
        this.num_comments = num_comments;
        this.url = url;
        this.ups = ups;
    }

    public Subreddit() {
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(int numberOfComments) {
        num_comments = numberOfComments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getReadableTimestamp() {
        return DateUtils.getRelativeTimeSpanString(created * 1000).toString();
    }

}
