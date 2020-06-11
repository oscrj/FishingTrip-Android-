package com.example.fishingtrip.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FishingTrip {

    private int fishingTripId;
    private String fishingMethod;
    private String waterType;
    private String location;
    private LocalDate date;
    private AppUser appUser;

    public FishingTrip(int fishingTripId, String fishingMethod, String waterType, String location, LocalDate date) {
        this.fishingTripId = fishingTripId;
        this.fishingMethod = fishingMethod;
        this.waterType = waterType;
        this.location = location;
        this.date = date;
    }

    public int getFishingTripId() {
        return fishingTripId;
    }

    public void setFishingTripId(int fishingTripId) {
        this.fishingTripId = fishingTripId;
    }

    public String getFishingMethod() {
        return fishingMethod;
    }

    public void setFishingMethod(String fishingMethod) {
        this.fishingMethod = fishingMethod;
    }

    public String getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public String toString() {
        return "FishingTrip{" +
                "fishingTripId=" + fishingTripId +
                ", fishingMethod='" + fishingMethod + '\'' +
                ", waterType='" + waterType + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", appUser=" + appUser +
                '}';
    }
}
