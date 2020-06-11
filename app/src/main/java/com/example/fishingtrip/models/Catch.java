package com.example.fishingtrip.models;

import java.time.LocalDateTime;

public class Catch {

    private String species;
    private double length;
    private double weight;
    private LocalDateTime timeStamp;
    private FishingTrip fishingTrip;

    public Catch(String species, double length, double weight, LocalDateTime timeStamp) {
        this.species = species;
        this.length = length;
        this.weight = weight;
        this.timeStamp = timeStamp;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public FishingTrip getFishingTrip() {
        return fishingTrip;
    }

    public void setFishingTrip(FishingTrip fishingTrip) {
        this.fishingTrip = fishingTrip;
    }

    @Override
    public String toString() {
        return "Catch{" +
                "species='" + species + '\'' +
                ", length=" + length +
                ", weight=" + weight +
                ", timeStamp=" + timeStamp +
                ", fishingTrip=" + fishingTrip +
                '}';
    }
}
