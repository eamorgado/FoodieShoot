package com.ciber.foodieshoot.applications.detection.Auxiliar.CalorieParser;

import android.content.Context;

import com.ciber.foodieshoot.applications.detection.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import com.ciber.foodieshoot.applications.detection.R;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

public class FoodCalories {
    public static final String FOOD_CALORIES_FILE = "food_cals.json";
    private static JSONObject json_object;
    private static CalorieJsonParser parsed_json;
    private static HashMap<String,Food> foods = new HashMap<>();

    public static void readFile(Context context){
        try{
            InputStream file = context.getAssets().open(FOOD_CALORIES_FILE);
            int size = file.available();
            byte[] buffer = new byte[size];
            file.read(buffer);
            file.close();
            String json = new String(buffer,"UTF-8");
            json_object = new JSONObject(json);

            com.google.gson.Gson gson = new com.google.gson.Gson();
            parsed_json = gson.fromJson(json_object.toString(), CalorieJsonParser.class);

            for(Food food: parsed_json.getFoods()){
                if(!foods.containsKey(food.getName()))
                    foods.put(food.getName(),food);
            }
        }catch(IOException | JSONException e){
            e.printStackTrace();
        }
    }

    public static HashMap<String,Food> getFoods(){
        return foods;
    }

    public static Food getFood(String food){
        return foods.containsKey(food) ? foods.get(food) : null;
    }

    public static String getTitle(String title){
        if (title != null) {
            Food food = FoodCalories.getFood(title);
            title += "_[" + food.getCalories() + " kcals, " + food.getQuantity() + " " + food.getUnit() + "]";
            return  title;
        }
        return "Unknown";
    }
}
