package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;

import com.ciber.foodieshoot.applications.detection.Configs.Configurations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodPostAnalyse {
    private FoodAnalysisResponse analysis_response = null;
    private String server_post_response = null;
    private FoodPostAnalyse instance = null;

    private FoodPostAnalyse(){}

    public FoodPostAnalyse getInstance(){
        if(instance == null)
            instance = new FoodPostAnalyse();
        return instance;
    }

    private void newPostAnalysis(JSONObject response) throws JSONException {
        com.google.gson.Gson gson = new com.google.gson.Gson();
        analysis_response = gson.fromJson(response.toString(),FoodAnalysisResponse.class);
        if(wasSuccessful()){
            server_post_response = response.get("contents").toString();
        }
    }

    private boolean wasSuccessful(){
        return analysis_response.getStatus().equals("success");
    }

    private double getTotalCalories(){
        return analysis_response.getContents().getTotalCalories();
    }

    private List<SingleFoodInfo> getAllProcessedFoods(){
        return analysis_response.getContents().getProcessed();
    }

    private JSONObject preprocessAnalysisRequest(ArrayList<String> foods) throws JSONException {
        JSONObject request = new JSONObject();
        request.put(Configurations.USER.TOKEN.getKey(),Configurations.USER.TOKEN.getValue());

        //Add food list
        JSONArray jarray = new JSONArray();
        for(String food : foods)
            jarray.put(food);
        request.put("foods",jarray);
        return  request;
    }
}
