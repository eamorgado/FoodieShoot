package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SingleFoodInfo {
    private Map<String,String> food_contents;
    private String[] keys = {
            "Name","Serving quantity","Serving unit","Serving weight (grams)","Total calories",
            "Total fat","Total saturated fat","Cholesterol","Sodium","Total carbs","Fiber",
            "Sugar","Protein","Potassium","Occurrences"
    };

    SingleFoodInfo(JSONObject food_info) throws JSONException {
        food_contents = new HashMap<>();
        String val = "";
        for(String key : keys){
            val = "" + food_info.get(key);
            food_contents.put(key,val);
        }

    }

    public String[] getKeys(){
        return keys;
    }

    public String getValues(String key){
        if(food_contents.containsKey(key))
            return food_contents.get(key);
        return "";
    }


    @Override
    public String toString() {
        String s = "{";
        int i = 0;
        for(String key : food_contents.keySet()){
            s += key +": " + food_contents.get(key);
            if(i++ < keys.length) s += ", ";
        }
        s += "}";
        return s;
    }

}
