package com.example.mscarenew.ui.medtrack;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JSONSerializer {
    private String mFilename;
    private Context mContext;

    public JSONSerializer(String fn, Context con) {
        mFilename = fn;
        mContext = con;
    }

    public void save(List<MedTrack> meds)
            throws IOException, JSONException {

        JSONArray jArray = new JSONArray();
        for (MedTrack m : meds) {
            jArray.put(m.convertToJSON());
        }

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(
                    mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    public ArrayList<MedTrack> load()
            throws IOException, JSONException {
        ArrayList<MedTrack> medList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray jArray = (JSONArray)
                    new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < jArray.length(); i++) {
                medList.add(new MedTrack(jArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // first run, ignore
        } finally {
            if (reader != null) reader.close();
        }
        return medList;
    }
}
