package com.ciber.foodieshoot.applications.detection.Authenticated;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ciber.foodieshoot.applications.detection.Authentication.LoginPage;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Alert;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.FoodPostList;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.SavedPosts;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.SingleFoodInfo;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post,container,false);
        getPostList();
        FoodPostList instance = FoodPostList.getInstance();
        if(instance == null || instance.getSize() == 0){
            ((RelativeLayout) v.findViewById(R.id.posts_no_post_rl)).setVisibility(View.VISIBLE);
            ((RelativeLayout) v.findViewById(R.id.posts_post_list_rl)).setVisibility(View.GONE);
            return v;
        }

        //Posts exist
        //Togle visibility
        ((RelativeLayout) v.findViewById(R.id.posts_no_post_rl)).setVisibility(View.GONE);
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.posts_post_list_rl);
        rl.setVisibility(View.VISIBLE);

        //Set total posts
        ConstraintLayout cl = (ConstraintLayout) rl.findViewById(R.id.posts_total_cl);
        TextView total = (TextView) cl.findViewById(R.id.posts_total_cl_number);
        total.setText("" + instance.getSize());

        //Get scrl v
        ScrollView sc = (ScrollView) rl.findViewById(R.id.posts_scroll);
        sc.setNestedScrollingEnabled(true);
        LinearLayout ll = (LinearLayout) sc.findViewById(R.id.posts_scroll_ll);
        int i = 0;
        for(SavedPosts post : instance.getPosts()){
            generatePost(inflater,ll,post,i++);
        }
        return v;
    }


    private void generatePost(LayoutInflater inflater,LinearLayout parent, SavedPosts post, int i){
        CardView cv = (CardView) inflater.inflate(R.layout.single_post_colapsible,parent,false);
        //set tile location and date
        ConstraintLayout cl = (ConstraintLayout) cv.findViewById(R.id.posts_card_cl_content);
        ConstraintLayout cl_title = (ConstraintLayout) cl.findViewById(R.id.posts_title_toggle);
        LinearLayout ll = (LinearLayout) cl_title.findViewById(R.id.posts_card_title_location_ll);
        TextView tv = (TextView) ll.findViewById(R.id.posts_card_title);
        String content = tv.getText().toString() + post.getTitle();
        tv.setText(content);
        tv = (TextView) ll.findViewById(R.id.posts_card_location);
        tv.setText(tv.getText().toString() + post.getLocation());
        tv = (TextView) ll.findViewById(R.id.posts_card_date);
        tv.setText(tv.getText().toString() + post.getDate());

        //Button togle
        ll = (LinearLayout) cl_title.findViewById(R.id.post_expand_ll);
        Button bt = (Button) ll.findViewById(R.id.posts_expand);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout cl = (ConstraintLayout) v.getParent().getParent().getParent();
                RelativeLayout rl = (RelativeLayout) cl.findViewById(R.id.posts_sc_rl);
                if(rl.getVisibility() == View.VISIBLE){
                    //TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    rl.setVisibility(View.GONE);
                    v.setBackground(Logged_Home.getContextOfApplication().getDrawable(R.drawable.arrow_down_post));
                }
                else{
                    rl.setVisibility(View.VISIBLE);
                    v.setBackground(Logged_Home.getContextOfApplication().getDrawable(R.drawable.arrow_up_post));
                }
            }
        });

        //add contents of post
        RelativeLayout rl = (RelativeLayout) cl.findViewById(R.id.posts_sc_rl);
        ll = (LinearLayout) rl.findViewById(R.id.posts_sc_rl_ll);
        //Add total cals
        ConstraintLayout cl_post = (ConstraintLayout) inflater.inflate(R.layout.single_food_posts,ll,false);
        tv = (TextView) cl_post.findViewById(R.id.preview_description);
        tv.setText("Total calories: ");
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv = (TextView) cl_post.findViewById(R.id.preview_contents);
        tv.setText(post.getContents().getTotalCalories() + " kcals");
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        ll.addView(cl_post);

        inflater.inflate(R.layout.view_seperator,ll,true);

        int j = 0;
        List<SingleFoodInfo> foods = post.getContents().getProcessed();
        for(SingleFoodInfo food : foods){
            String[] keys = food.getKeys();
            for(String key : keys){
                String contents = food.getValues(key);
                String description = key + ": ";
                generateRow(inflater,ll,description,contents);
            }
            if((i++ < foods.size()-1) && foods.size() > 1)
                inflater.inflate(R.layout.view_seperator,ll,true);
        }

        //Add delete func
        ((TextView)rl.findViewById(R.id.post_raw_date)).setText(post.getRawDate());
        bt = (Button) rl.findViewById(R.id.post_delete);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v.getParent();
                TextView tv = (TextView) rl.findViewById(R.id.post_raw_date);
                String raw_date = tv.getText().toString();
                //delete
                Runnable delete = new Runnable() {
                    @Override
                    public void run() {
                        String endpoint = Configurations.SERVER_URL + Configurations.REST_API + Configurations.POST_DELETE_PATH;
                        Map<String,String> params = new HashMap<>();
                        params.put("token",Configurations.USER.TOKEN.getValue());
                        params.put("delete",raw_date);
                        NetworkManager.getInstance().postRequest(endpoint, params, new RestListener() {
                            @Override
                            public void parseResponse(JSONObject response) {
                                try{
                                    if(response.getString("status").equals("success")){
                                        Configurations.sendNotification("Post delete","Post deleted with success", NotificationManager.IMPORTANCE_DEFAULT);
                                        Intent intent = new Intent(Logged_Home.getContextOfApplication(),Logged_Home.class);
                                        intent.putExtra("Redirect","Posts");
                                        startActivity(intent);
                                    }
                                    else{
                                        Intent intent = new Intent(Logged_Home.getContextOfApplication(),Logged_Home.class);
                                        intent.putExtra("Redirect","Posts");
                                        startActivity(intent);
                                    }
                                }catch (JSONException e){}
                            }
                            @Override
                            public void handleError(VolleyError error) {
                                Runnable dismiss = new Runnable() {
                                    @Override
                                    public void run() {}
                                };
                                Alert.infoUser(SplashActivity.getContextOfApplication(),getString(R.string.server_connection),getString(R.string.server_unavailable),getString(R.string.ok),dismiss);
                            }
                        });
                    }
                };

                Runnable cancel = new Runnable() {
                    @Override
                    public void run() {}
                };
                String message = "Are you sure you want to delete this post? (Action irreversible)";
                Alert.alertUser(
                        Logged_Home.getContextOfApplication(),
                        "Post delete",
                        message,getString(R.string.yes),
                        getString(R.string.no),
                        delete,
                        cancel
                );

            }
        });

        //add to layout
        if(i % 2 == 0)
            cv.setBackgroundColor(getResources().getColor(R.color.red_trasparent));
        parent.addView(cv);
    }

    private void generateRow(LayoutInflater inflater,LinearLayout parent, String description_message, String contents_message){
        ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.single_food_posts,parent,false);
        TextView description = (TextView) cl.findViewById(R.id.preview_description);
        TextView contents = (TextView) cl.findViewById(R.id.preview_contents);
        description.setText(description_message);
        contents.setText(contents_message);
        parent.addView(cl);
    }

    private void getPostList(){
        String endpoint = Configurations.SERVER_URL + Configurations.REST_API + Configurations.POST_LIST_PATH;
        NetworkManager.getInstance().getRequest(endpoint, new RestListener() {
            @Override
            public void parseResponse(JSONObject response) {
                Log.i("POST_LIST",response.toString());
                try{
                    FoodPostList.getInstance().newPosts(response);
                    Log.i("POST_LIST_DESC",FoodPostList.getInstance().toString());
                }catch (JSONException | ParseException e){
                    Log.i("POST_LIST_Error",e.getMessage());
                }
            }

            @Override
            public void handleError(VolleyError error) {
                Runnable dismiss = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(Configurations.REST_AUTH_FAIL,"Request timed out.");
                    }
                };
                Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out.");
                Alert.infoUser(SplashActivity.getContextOfApplication(),getString(R.string.server_connection),getString(R.string.server_unavailable),getString(R.string.ok),dismiss);
            }
        });
    }
}
