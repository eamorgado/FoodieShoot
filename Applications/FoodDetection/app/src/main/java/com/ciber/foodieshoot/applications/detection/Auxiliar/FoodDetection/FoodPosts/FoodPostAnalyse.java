package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;

import androidx.annotation.NonNull;

import com.ciber.foodieshoot.applications.detection.Configs.Configurations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodPostAnalyse {
    private static FoodAnalysisResponse analysis_response;
    private static String server_post_response;
    private static FoodPostAnalyse instance;

    private FoodPostAnalyse(){}

    public static synchronized FoodPostAnalyse getInstance(){
        if(instance == null)
            instance = new FoodPostAnalyse();
        return instance;
    }

    public void newPostAnalysis(JSONObject response) throws JSONException {
        instance.analysis_response = new FoodAnalysisResponse(response);
        if(wasSuccessful()){
            instance.server_post_response = response.get("contents").toString();
        }
    }

    public boolean wasSuccessful(){
        return instance.analysis_response.getStatus().equals("success");
    }

    public float getTotalCalories(){
        return instance.analysis_response.getContents().getTotalCalories();
    }

    public List<SingleFoodInfo> getAllProcessedFoods(){
        return instance.analysis_response.getContents().getProcessed();
    }

    public JSONObject preprocessAnalysisRequest(ArrayList<String> foods) throws JSONException {
        JSONObject request = new JSONObject();
        request.put(Configurations.USER.TOKEN.getKey(),Configurations.USER.TOKEN.getValue());

        //Add food list
        JSONArray jarray = new JSONArray();
        for(String food : foods)
            jarray.put(food);
        request.put("foods",jarray);
        return  request;
    }

    public String getServerResponse(){
        return instance.server_post_response;
    }

    @NonNull
    @Override
    public String toString() {
        return instance.analysis_response.toString();
    }
}
