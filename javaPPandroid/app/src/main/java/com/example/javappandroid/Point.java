package com.example.javappandroid;

public class Point {
    private int id;
    private double longi;
    private double lati;
    private String name;
    private String description;
    private String build;

    public Point (int id,double longi,double lati,String name,String description,String build){
        this.id = id;
        this.longi = longi;
        this.lati = lati;
        this.name = name;
        this.description = description;
        this.build = build;
    }

    public Point (){
        this.id = id;
        this.longi = longi;
        this.lati = lati;
        this.name = name;
        this.description = description;
        this.build = build;
    }

    public Point (float longi,float lati){
        this.id = id;
        this.longi = longi;
        this.lati = lati;
        this.name = name;
        this.description = description;
        this.build = build;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }
}
