package com.ciber.foodieshoot.applications.detection.Authenticated;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

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
        LinearLayout ll = (LinearLayout) sc.findViewById(R.id.posts_scroll_ll);
        int i = 0;
        for(SavedPosts post : instance.getPosts()){
            generatePost(inflater,ll,post,i);
        }
        return v;
    }


    private void generatePost(LayoutInflater inflater,LinearLayout parent, SavedPosts post, int i){
        CardView cv = (CardView) inflater.inflate(R.layout.single_post_colapsible,parent,false);
        //set tile location and date
        ConstraintLayout cl = (ConstraintLayout) cv.findViewById(R.id.posts_card_cl_content);
        LinearLayout ll = (LinearLayout) cl.findViewById(R.id.posts_card_title_location_ll);
        TextView tv = (TextView) ll.findViewById(R.id.posts_card_title);
        String content = tv.getText().toString() + post.getTitle();
        tv.setText(content);
        tv = (TextView) ll.findViewById(R.id.posts_card_location);
        tv.setText(tv.getText().toString() + post.getLocation());
        tv = (TextView) ll.findViewById(R.id.posts_card_date);
        tv.setText(tv.getText().toString() + post.getDate());

        //add to layout
        parent.addView(cv);
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
                if(error.networkResponse == null && (error instanceof TimeoutError || error instanceof NoConnectionError)){
                    String message = getString(R.string.server_timeout) + " - " + getString(R.string.server_unavailable);
                    Toast.makeText(Logged_Home.getContextOfApplication(),message,Toast.LENGTH_LONG).show();
                }
                else{
                    String login = LoginPage.getContextOfApplication().getString(R.string.login_login);
                    String login_invalid_token = LoginPage.getContextOfApplication().getString(R.string.unable_to) + " " + login.toLowerCase();
                    login_invalid_token += ".\n" + LoginPage.getContextOfApplication().getString(R.string.token_invalid_expired);
                    Configurations.logout();
                    Configurations.deleteToken();
                    Configurations.deleteUservars();
                    Alert.infoUser(Logged_Home.getContextOfApplication(),login,login_invalid_token,Logged_Home.getContextOfApplication().getString(R.string.ok),dismiss);
                }
            }
        });
    }
}
