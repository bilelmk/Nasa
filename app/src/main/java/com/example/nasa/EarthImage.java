package com.example.nasa;

public class EarthImage {
    private String cloud_score;
    private String date;
    private String id;
    private String resource;
    private String service_version;
    private String url;

    public EarthImage() {
    }

    public EarthImage(String cloud_score, String date, String id, String resource, String service_version, String url) {
        this.cloud_score = cloud_score;
        this.date = date;
        this.id = id;
        this.resource = resource;
        this.service_version = service_version;
        this.url = url;
    }

    public String getCloud_score() {
        return cloud_score;
    }

    public void setCloud_score(String cloud_score) {
        this.cloud_score = cloud_score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getService_version() {
        return service_version;
    }

    public void setService_version(String service_version) {
        this.service_version = service_version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
