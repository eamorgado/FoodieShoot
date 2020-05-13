package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ciber.foodieshoot.applications.detection.Auxiliar.CalorieParser.Food;
import com.ciber.foodieshoot.applications.detection.Auxiliar.CalorieParser.FoodCalories;
import com.ciber.foodieshoot.applications.detection.R;

import java.util.ArrayList;

public final class DisplayFoods {
    public static ArrayList<String> foods = new ArrayList<>();
    public static Double cals = 0.0;
    public static Context context = null;

    public static void setContext(Context cntx){
        context = cntx;
        foods = new ArrayList<>();
        cals = 0.0;
    }

    public static void setFoodsForDisplay(ArrayList<String> fs,double total){
        foods.clear();
        for(String f : fs)
            foods.add(f);
        cals = total;
        DisplayFoods.display();
    }

    public static void display(){
        View v = (View)((Activity)context).findViewById(R.id.seperator);
        v.setVisibility(View.GONE);
        TableLayout tl = (TableLayout) ((Activity)context).findViewById(R.id.table_predictions);
        tl.removeAllViews();
        tl.setVisibility(View.GONE);
        int i = 0;
        if(!foods.isEmpty()){
            for(String food : foods){
                TableRow row = new TableRow(context);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                TextView tv = new TextView(context);
                String quantaty = FoodCalories.getFood(food).getQuantity();
                String units = FoodCalories.getFood(food).getUnit();
                String cal = FoodCalories.getFood(food).getCalories();
                tv.setText(food + " -- " + quantaty + " " + units + " -- " + cal + " kcals");
                row.addView(tv);
                tl.addView(row,i++);
            }
            TextView tv = new TextView(context);
            tv.setText("Total cals: " + cals + " kcals");
            tl.addView(tv,i);
            tl.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);
        }
    }
}
