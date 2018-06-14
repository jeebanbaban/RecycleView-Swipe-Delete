package com.ingreens.recycleswipedelete.models;

/**
 * Created by root on 26/4/18.
 */

public class Players {
    private int id;
    private String name;
    private String team;
    private double strike_rate;
    String thumbnail;

    public Players() {
    }

    public Players(int id, String name, String team, double strike_rate) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.strike_rate = strike_rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getStrike_rate() {
        return strike_rate;
    }

    public void setStrike_rate(double strike_rate) {
        this.strike_rate = strike_rate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Players{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", strike_rate=" + strike_rate +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
