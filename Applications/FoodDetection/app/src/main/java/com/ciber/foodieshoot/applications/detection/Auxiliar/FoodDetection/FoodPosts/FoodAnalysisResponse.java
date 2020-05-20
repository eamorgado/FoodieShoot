package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FoodAnalysisResponse {
    private String status;
    private FoodContents contents;

    FoodAnalysisResponse(JSONObject response) throws JSONException {
        status = response.getString("status");
        contents = new FoodContents(response.getJSONObject("contents"));
    }

    public String getStatus(){return status;}
    public FoodContents getContents(){return contents;}

    public JSONObject convertJsonContents() throws JSONException {
        return contents.convertJson();
    }

    @Override
    public String toString() {
        return "{Status: " + status + ", Contents: {" + contents.toString() + "}}";
    }

}
