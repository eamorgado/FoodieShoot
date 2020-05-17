package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavedPosts {
    private String title;
    private String location;
    private String date;
    private String raw_date;
    private FoodContents contents;

    SavedPosts(JSONObject response) throws JSONException, ParseException {
        title = response.getString("title");
        location = response.getString("location");
        raw_date = response.getString("date");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = sdf.parse(raw_date);
        date = output.format(dt);
        contents = new FoodContents(response.getJSONObject("contents"));
    }

    public String getTitle(){
        return title;
    }

    public String getLocation(){
        return location;
    }

    public String getDate(){
        return date;
    }

    public String getRawDate(){
        return raw_date;
    }

    public FoodContents getContents(){
        return contents;
    }

    @NonNull
    @Override
    public String toString() {
        return "{title: " + title + ", " + "date: " + date + ", location: " + location + ", " + "contents: " + contents.toString() +"}";
    }
}
