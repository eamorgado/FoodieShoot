package com.ciber.foodieshoot.applications.detection.Authenticated.Posts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Alert;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.FoodPostAnalyse;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.SingleFoodInfo;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PostsPreview extends AppCompatActivity {
    private LayoutAuxiliarMethods layout_auxiliar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_preview);
        layout_auxiliar = new LayoutAuxiliarMethods(this);
        displayFoods();
        save();
        discard();
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

        ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.single_food_posts,ll,false);
        TextView tv = (TextView) cl.findViewById(R.id.preview_description);
        tv.setText("Total calories: ");
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv = (TextView) cl.findViewById(R.id.preview_contents);
        tv.setText(total_cals + " kcals");
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        ll.addView(cl);

        inflater.inflate(R.layout.view_seperator,ll,true);

        List<SingleFoodInfo> foods = FoodPostAnalyse.getInstance().getAllProcessedFoods();
        int i = 0;
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
            if((i++ < foods.size()-1) && foods.size() > 1)
                inflater.inflate(R.layout.view_seperator,ll,true);
        }
    }

    private void discard(){
        Button discard = (Button) findViewById(R.id.preview_discard);
        discard.setMovementMethod(LinkMovementMethod.getInstance());
        discard.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                layout_auxiliar.openActivity(DetectorActivity.class);
            }
        });
    }

    private void save(){
        Button save = (Button) findViewById(R.id.preview_save);
        save.setMovementMethod(LinkMovementMethod.getInstance());
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String endpoint = Configurations.SERVER_URL + Configurations.REST_API + Configurations.POST_SAVE_PATH;
                JSONObject request = new JSONObject();
                //get title
                LinearLayout ll = (LinearLayout) findViewById(R.id.preview_ll_1);
                EditText text = (EditText) ll.findViewById(R.id.preview_title_save);
                String title = text.getText().toString();
                //get location
                /**
                 * PUT LOCATION HERE (COVERTED TO PLACE FROM LATITUDE AND LONG
                 * String location = ....
                 */
                try {
                    request.put("token", Configurations.USER.TOKEN.getValue());
                    request.put("save","True");
                    request.put("contents",new JSONObject(FoodPostAnalyse.getInstance().getServerResponse()));
                    if(!title.equals("")) request.put("title",title);
                    /**
                     * Make check for location
                     * if(location found) request.put("location",location);
                     */
                    NetworkManager.getInstance().postRequestFromJson(endpoint, request, new RestListener() {
                        @Override
                        public void parseResponse(JSONObject response) {
                            Log.i("POST_SAVED_RESPONSE",response.toString());
                            try{
                                String status = response.getString("status");
                                if(status.equals("success")){
                                    Log.i("POST_SAVED_SUCC","Post saved with success " + response.toString());
                                    String post_saved = "Post saved";
                                    Toast.makeText(SplashActivity.getContextOfApplication(),post_saved,Toast.LENGTH_LONG);
                                    layout_auxiliar.openActivityExtra(Logged_Home.class,"Posts");
                                }
                            }catch (JSONException je){}
                        }

                        @Override
                        public void handleError(VolleyError error) {
                            Log.e("POST_SAVE_ERROR_VOLEY",error.toString());
                            Runnable dismiss = new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out.");
                                    Intent i = new Intent(SplashActivity.getContextOfApplication(),DetectorActivity.class);
                                    startActivity(i);
                                }
                            };
                            if(error.networkResponse == null && (error instanceof TimeoutError || error instanceof NoConnectionError)){
                                Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out.");
                                Alert.infoUser(SplashActivity.getContextOfApplication(),getString(R.string.server_connection),getString(R.string.server_unavailable),getString(R.string.ok),dismiss);
                            }
                            else{
                                String login = SplashActivity.getContextOfApplication().getString(R.string.error);
                                String login_invalid_token = SplashActivity.getContextOfApplication().getString(R.string.token_invalid_expired);
                                Alert.infoUser(SplashActivity.getContextOfApplication(),login,login_invalid_token,SplashActivity.getContextOfApplication().getString(R.string.ok),dismiss);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("POST_SAVE_ERROR",e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        layout_auxiliar.openActivity(DetectorActivity.class);
    }
}
