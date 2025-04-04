package com.example.mscarealpha.ui.home;

public class AffirmationsAdapter {
   private int Id;
   private String Affirmation;

    public int getId() {
        return Id;
    }

    public String getAffirmation() {
        return Affirmation;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setAffirmation(String affirmation) {
        Affirmation = affirmation;
    }

    @Override
    public String toString() {
        return "AffirmationsAdapter{" +
                "Id=" + Id +
                ", Affirmation='" + Affirmation + '\'' +
                '}';
    }
}
