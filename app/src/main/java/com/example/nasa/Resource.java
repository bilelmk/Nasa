package com.example.nasa;

public class Resource {
    private String dataset;
    private String planet;

    public Resource() {
    }

    public Resource(String dataset, String planet) {
        this.dataset = dataset;
        this.planet = planet;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }
}
