package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodContents {
    private float total_calories;
    private List<SingleFoodInfo> processed;


    FoodContents(JSONObject contents) throws JSONException {
        total_calories = (float)contents.getDouble("total_calories");
        processed = new ArrayList<>();

        JSONArray processed_arr = contents.getJSONArray("processed");
        for(int i = 0; i < processed_arr.length(); i++){
            processed.add(new SingleFoodInfo(processed_arr.getJSONObject(i)));
        }
    }
    public float getTotalCalories() {
        return total_calories;
    }

    public List<SingleFoodInfo> getProcessed(){
        return  processed;
    }

    public JSONObject convertJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("total_calories",total_calories);
        JSONArray processed_array = new JSONArray();
        for(SingleFoodInfo food : processed)
            processed_array.put(food.convertJson());
        json.put("processed",processed_array);
        return json;
    }

    @Override
    public String toString() {
        String s = "{Total Calories: " + total_calories +", Processed: [";
        int i = 0;
        for(SingleFoodInfo food : processed){
            s += food.toString();
            if(i++ < (processed.size() - 1)) s += ", ";
        }
        s += "]}";
        return s;
    }

}