package com.ciber.foodieshoot.applications.detection.Auxiliar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ciber.foodieshoot.applications.detection.Configs.Configurations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class LayoutAuxiliarMethods{
    private Context context;

    public LayoutAuxiliarMethods(Context context){
        this.context = context;
    }

    //Progress bar handlers
    public void startProgress(int id){
        ProgressBar bar = (ProgressBar) ((Activity)context).findViewById(id);
        bar.setVisibility(View.VISIBLE);
    }
    public void stopProgress(int id){
        ProgressBar bar = (ProgressBar) ((Activity)context).findViewById(id);
        bar.setVisibility(View.INVISIBLE);
    }

    //Get field content
    public String getFieldContent(int id){
        return ((EditText)((Activity)context).findViewById(id)).getText().toString().trim();
    }

    public String buildUrl(String[] paths){
        String url = "";
        for(String path : paths) url += path;
        return url;
    }
    //Activity start
    public void openActivity(final Class<? extends Activity> destination){
        Intent intent = new Intent(this.context,destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity)this.context).finish();
    }

    //Color change
    /**
     * Method to change colors for some components in activity
     */
    public void changeColor(int[] ids, String[] texts){
        int i = 0;
        for(int id : ids){
            TextView view = (TextView)((Activity)context).findViewById(id);
            String s = view.getText().toString() + texts[i++];
            view.setText(Html.fromHtml(s));
        }
    }


    public HashMap<String,String> buildParams(String[] request_key, int[] request_ids){
        HashMap<String,String> params = new HashMap<>();
        int i = 0;
        for(String key : request_key) params.put(key,getFieldContent(request_ids[i++]));
        return params;
    }

    public void setUserVars(JSONObject response) throws JSONException {
        for(Configurations.USER user_val : Configurations.USER.values())
            user_val.setValue(response.get(user_val.getKey()).toString());
    }

}
