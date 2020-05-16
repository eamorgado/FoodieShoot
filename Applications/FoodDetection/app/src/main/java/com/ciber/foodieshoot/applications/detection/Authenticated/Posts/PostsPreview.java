package com.ciber.foodieshoot.applications.detection.Authenticated.Posts;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.FoodPostAnalyse;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.SingleFoodInfo;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;

import java.util.List;

public class PostsPreview extends AppCompatActivity {
    private LayoutAuxiliarMethods layout_auxiliar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_preview);
        layout_auxiliar = new LayoutAuxiliarMethods(this);
        displayFoods();
    }

    private void generateRow(LayoutInflater inflater,LinearLayout parent, String description_message, String contents_message){
        ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.single_food_posts,parent,false);
        TextView description = (TextView) cl.findViewById(R.id.preview_description);
        TextView contents = (TextView) cl.findViewById(R.id.preview_contents);
        description.setText(description_message);
        contents.setText(contents_message);
        parent.addView(cl);
    }

    private void displayFoods(){
        Log.i("POST_PREVIEW_ACTIVITY",FoodPostAnalyse.getInstance().getServerResponse(),null);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout ll = (LinearLayout) findViewById(R.id.preview_posts);
        Log.i("POST_PREVIEW_PREVIEW",FoodPostAnalyse.getInstance().toString());
        float total_cals = FoodPostAnalyse.getInstance().getTotalCalories();
        generateRow(inflater,ll,"Total calories: ",total_cals + " kcals");

        List<SingleFoodInfo> foods = FoodPostAnalyse.getInstance().getAllProcessedFoods();
        for(SingleFoodInfo food : foods){
            String[] keys = food.getKeys();
            /*String[] descriptions = {
                   "Name: ","Serving quantity: ","Serving unit: ","Serving weight (grams): ","Total food calories: ",
                    "Total fat: ","Total saturated fat: ","Cholesterol: ","Sodium: ","Total carbs: ","Fiber: ",
                    "Sugar: ","Protein: ","Potassium: ","Occurrences: ",
            };*/
            //int i = 0;
            for(String key : keys){
                String contents = food.getValues(key);
                String description = key + ": ";
                generateRow(inflater,ll,description,contents);
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        layout_auxiliar.openActivity(DetectorActivity.class);
    }
}
