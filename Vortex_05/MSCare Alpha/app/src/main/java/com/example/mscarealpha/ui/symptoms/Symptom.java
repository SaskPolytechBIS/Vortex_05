package com.example.mscarealpha.ui.symptoms;

public class Symptom {

    public int id;
    public String bodyPart;
    public String symptomName;
    public int painLevel;
    public String notes;
    public String timestamp; // Added to store the time of logging

    // Constructor
    public Symptom(String bodyPart, String symptomName, int painLevel, String notes, String timestamp) {
        this.bodyPart = bodyPart;
        this.symptomName = symptomName;
        this.painLevel = painLevel;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public String getSymptomName() {
        return symptomName;
    }

    public int getPainLevel() {
        return painLevel;
    }

    public String getNotes() {
        return notes;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Adding a setter for the id
    public void setId(int id) {
        this.id = id;
    }
    // Setters
    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public void setSymptomName(String symptomName) {
        this.symptomName = symptomName;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
