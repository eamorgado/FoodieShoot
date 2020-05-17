package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FoodPostList {
    private static int size;
    private static List<SavedPosts> posts;
    private static FoodPostList instance;

    public FoodPostList() {}

    public static FoodPostList getInstance(){
        if(instance == null)
            instance = new FoodPostList();
        return instance;
    }

    public static void newPosts(JSONObject response) throws JSONException, ParseException {
        instance.size = response.getInt("size");
        JSONArray array_posts = response.getJSONArray("posts");
        instance.posts = new ArrayList<>();
        for(int i = 0; i < array_posts.length(); i++){
            instance.posts.add(new SavedPosts(array_posts.getJSONObject(i)));
        }
    }
    public static int getSize(){
        return instance.size;
    }

    public static List<SavedPosts> getPosts(){
        return instance.posts;
    }

    @NonNull
    @Override
    public String toString() {
        String s = "{size: " + size +", posts: [";
        int i = 0;
        for(SavedPosts post : instance.posts){
            s += post.toString();
            if((i++ < (posts.size() - 1)) && instance.posts.size() > 1)
                s += ", ";
        }
        s += "]}";
        return s;
    }
}
