package com.example.fishingtrip.models;

public class FishingTrip {

    private int fishingTripId;
    private String fishingMethod;
    private String waterType;
    private String location;
    private String appUser;
    private boolean isActive;

    public FishingTrip(int fishingTripId, String fishingMethod, String waterType, String location, String appUser, boolean isActive) {
        this.fishingTripId = fishingTripId;
        this.fishingMethod = fishingMethod;
        this.waterType = waterType;
        this.location = location;
        this.appUser = appUser;
        this.isActive = isActive;
    }

    public int getFishingTripId() {
        return fishingTripId;
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

    public String getAppUser() {
        return appUser;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return fishingMethod.toUpperCase() + ", " + location + ", " + waterType;
    }
}
