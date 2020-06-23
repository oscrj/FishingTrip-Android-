package com.example.fishingtrip.models;

public class Catch {

    private int catchId;
    private String species;
    private double length;
    private double weight;
    private String fishingTrip;

    public Catch(int catchId, String species, double length, double weight, String fishingTrip) {
        this.catchId = catchId;
        this.species = species;
        this.length = length;
        this.weight = weight;
        this.fishingTrip = fishingTrip;
    }

    public int getCatchId() {
        return catchId;
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

    public String getFishingTrip() {
        return fishingTrip;
    }

    public void setFishingTrip(String fishingTrip) {
        this.fishingTrip = fishingTrip;
    }

    @Override
    public String toString() {
        return getSpecies() + "   " + getLength() + " cm   " + getWeight()  + " kg";
    }
}
