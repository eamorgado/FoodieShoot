package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection;
import com.ciber.foodieshoot.applications.detection.Auxiliar.CalorieParser.Food;
import com.ciber.foodieshoot.applications.detection.Auxiliar.CalorieParser.FoodCalories;

import java.util.ArrayList;

public class DetectedFoods {
    public static ArrayList<String> current_detections;
    public static boolean is_detecting;
    public static DetectedFoods instance = null;

    private DetectedFoods(){
        is_detecting = false;
        current_detections = new ArrayList<>();
    }

    public static boolean isDetecting(){return is_detecting;}
    public static void startDetection(){is_detecting = true;}
    public static void endDetection(){is_detecting = false;}

    public static void addDetection(String detection){
        if(is_detecting) current_detections.add(detection);
    }

    public static boolean hasDetections(){
        return !current_detections.isEmpty();
    }

    public static ArrayList<String> getDetections(){
        return current_detections;
    }
    public static void clearDetections(){
        current_detections.clear();
    }

    public static void resetDetections(){
        instance.endDetection();
        instance.clearDetections();
    }

    public static DetectedFoods getInstance(){
        if(instance == null)
            instance = new DetectedFoods();
        return instance;
    }

    public static Double getTotalCals(){
        double total = 0.0;
        for(String food : current_detections){
            try{
                total += Double.parseDouble(FoodCalories.getFood(food).getCalories());
            }catch (Exception e){}
        }
        return total;
    }
}
