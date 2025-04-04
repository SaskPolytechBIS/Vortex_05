package com.example.mscarealpha.ui.notes;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppointmentNotes {
    private String mTitle;
    private String mDescription;
    private boolean mQuestions;
    private boolean mNotes;
    private boolean mTodo;

    private String mDate;


    private String mCreationDate; // New field for creation date

    private static final String JSON_TITLE = "title";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_QUESTIONS = "questions";
    private static final String JSON_NOTES = "notes";
    private static final String JSON_TODO = "todo";
    private static final String JSON_CREATION_DATE = "creation_date"; // New JSON key for creation date

    // Getter and Setter for mCreationDate
    public String getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(String mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    // Constructor for creating a new note
    public AppointmentNotes(String title, String description, boolean questions, boolean notes, boolean todo) {
        this.mTitle = title;
        this.mDescription = description;
        this.mQuestions = questions;
        this.mNotes = notes;
        this.mTodo = todo;
        this.mCreationDate = getCurrentDate(); // Set the creation date to current date
    }

    // Constructor for creating a note from a JSONObject


    // Default constructor


    // Convert to JSON


    // Method to get the current date
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    // Getter and Setter methods for other fields

    private static final String JSON_DATE = "date";

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean isQuestions() {
        return mQuestions;
    }

    public void setQuestions(boolean mQuestions) {
        this.mQuestions = mQuestions;
    }

    public boolean isNotes() {
        return mNotes;
    }

    public void setNotes(boolean mNotes) {
        this.mNotes = mNotes;
    }

    public boolean isTodo() {
        return mTodo;
    }

    public void setTodo(boolean mTodo) {
        this.mTodo = mTodo;
    }


    // Constructor
    // Only used when new is called with a JSONObject
    public AppointmentNotes(JSONObject jo) throws JSONException {

        mTitle =  jo.getString(JSON_TITLE);
        mDescription = jo.getString(JSON_DESCRIPTION);
        mQuestions = jo.getBoolean(JSON_QUESTIONS);
        mNotes = jo.getBoolean(JSON_NOTES);
        mTodo = jo.getBoolean(JSON_TODO);
        mDate = jo.getString(JSON_DATE);

    }

    // Now we must provide an empty default constructor
    // for when we create a Note as we provide a
    // specialized constructor.
    public AppointmentNotes (){
    }

    public JSONObject convertToJSON() throws JSONException{

        JSONObject jo = new JSONObject();

        jo.put(JSON_TITLE, mTitle);
        jo.put(JSON_DESCRIPTION, mDescription);
        jo.put(JSON_QUESTIONS, mQuestions);
        jo.put(JSON_NOTES, mNotes);
        jo.put(JSON_TODO, mTodo);
        jo.put(JSON_DATE, mDate);

        return jo;
    }

}
