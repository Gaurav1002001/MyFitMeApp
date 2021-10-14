package com.example.myfitmeapp;

public class CountryModel {
    private String active;
    private String cases;
    private String country;
    private String critical;
    private String deaths;
    private String flag;
    private String recovered;
    private String todayCases;
    private String todayDeaths;

    public CountryModel() {
    }

    public CountryModel(String flag2, String country2, String cases2, String todayCases2, String deaths2, String todayDeaths2, String recovered2, String active2, String critical2) {
        this.flag = flag2;
        this.country = country2;
        this.cases = cases2;
        this.todayCases = todayCases2;
        this.deaths = deaths2;
        this.todayDeaths = todayDeaths2;
        this.recovered = recovered2;
        this.active = active2;
        this.critical = critical2;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag2) {
        this.flag = flag2;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country2) {
        this.country = country2;
    }

    public String getCases() {
        return this.cases;
    }

    public void setCases(String cases2) {
        this.cases = cases2;
    }

    public String getTodayCases() {
        return this.todayCases;
    }

    public void setTodayCases(String todayCases2) {
        this.todayCases = todayCases2;
    }

    public String getDeaths() {
        return this.deaths;
    }

    public void setDeaths(String deaths2) {
        this.deaths = deaths2;
    }

    public String getTodayDeaths() {
        return this.todayDeaths;
    }

    public void setTodayDeaths(String todayDeaths2) {
        this.todayDeaths = todayDeaths2;
    }

    public String getRecovered() {
        return this.recovered;
    }

    public void setRecovered(String recovered2) {
        this.recovered = recovered2;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active2) {
        this.active = active2;
    }

    public String getCritical() {
        return this.critical;
    }

    public void setCritical(String critical2) {
        this.critical = critical2;
    }
}
