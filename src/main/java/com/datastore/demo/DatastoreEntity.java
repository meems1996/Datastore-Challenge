package com.datastore.demo;

class DatastoreEntity {

    private String stb;
    private String title;
    private String provider;
    private String date;
    private Double rev;
    private String viewTime;

    //    Constructor
    public DatastoreEntity(String stb, String title, String provider, String date, Double rev, String viewTime) {
        this.stb = stb;
        this.title = title;
        this.provider = provider;
        this.date = date;
        this.rev = rev;
        this.viewTime = viewTime;
    }

//    Getter and Setters for this entity
    public String getStb() {
        return stb;
    }

    public void setStb(String stb) {
        this.stb = stb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRev() {
        return rev;
    }

    public void setRev(double rev) {
        this.rev = rev;
    }

    public String getViewTime() {
        return viewTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }

    @Override
    public String toString() {
        return stb + "," + title + "," + provider + "," + date + "," + rev + "," + viewTime;
    }
}
