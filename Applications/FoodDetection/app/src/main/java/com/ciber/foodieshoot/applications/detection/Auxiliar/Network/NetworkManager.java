package com.ciber.foodieshoot.applications.detection.Auxiliar.Network;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;
    public RequestQueue request_queue;

    private NetworkManager(Context context){
        request_queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context){
        if(instance == null)
            instance = new NetworkManager(context);
        return instance;
    }

    public static synchronized NetworkManager getInstance(){
        if(instance == null)
            NetworkManager.getInstance(SplashActivity.getContextOfApplication());
        return instance;
    }


    public void postRequestFromJson(String endpoint, JSONObject params, final RestListener listener){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                endpoint,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.handleError(error);
                    }
                }
        );
        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request_queue.add(request);
    }

    public void postRequest(String endpoint, Map<String,? extends Object> params, final RestListener listener){
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                endpoint,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ":", "Url: " + endpoint + " response: " + response.toString());
                        if (response.toString() != null)
                            listener.parseResponse(response);
                        else
                            listener.handleError(new VolleyError("Server timed out"));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error: " + error.toString());
                        listener.handleError(new VolleyError(new TimeoutError()));
                    }
                }
        );

        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request_queue.add(request);
    }

    public void getRequest(String endpoint,final RestListener listener){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                endpoint,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ":", "Url: " + endpoint + " response: " + response.toString());
                        if (response.toString() != null)
                            listener.parseResponse(response);
                        else
                            listener.handleError(new VolleyError("Server timed out"));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error: " + error.toString());
                        listener.handleError(new VolleyError(new TimeoutError()));
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = "Token " + Configurations.USER.TOKEN.getValue();
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json; charset=UTF-8");
                headers.put("Authorization",token);
                return headers;
            }
        };
        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request_queue.add(request);
    }


    public void saveProfileImage(boolean user_keep_logged){
        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root);
        if(dir.exists()) {
            File img = new File(dir, "profile.png");
            if (img.exists() && user_keep_logged)
                return;
        }

        String endpoint = Configurations.SERVER_URL + Configurations.REST_API + Configurations.PROFILE_PIC_PATH;
        ImageRequest request = new ImageRequest(
                endpoint,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        String root = Environment.getExternalStorageDirectory().toString();
                        File dir = new File(root);
                        if(!dir.exists())
                            dir.mkdirs();
                        String image_name = user_keep_logged? "profile.png" : "profile_forget.png";
                        File img = new File(dir,image_name);
                        if(!img.exists()){
                            try{
                                FileOutputStream out = new FileOutputStream(img);
                                response.compress(Bitmap.CompressFormat.PNG,100,out);
                                out.flush();
                                out.close();
                                Log.i("SAVE_IMAGE","Image saved",null);
                            }catch(Exception e){
                                e.printStackTrace();
                                Log.e("SAVE_IMAGE",e.getMessage(),e);
                            }
                        }
                    }
                },0,0, ImageView.ScaleType.CENTER_CROP,null,
                new Response.ErrorListener(){
                    public void onErrorResponse(VolleyError error){
                        Log.e("SAVE_IMAGE","Getting user profile image error " + error.getMessage(),null);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = "Token " + Configurations.USER.TOKEN.getValue();
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json; charset=UTF-8");
                headers.put("Authorization",token);
                return headers;
            }
        };
        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request_queue.add(request);
    }

    public void setProfileImage(){
        String endpoint = Configurations.SERVER_URL + Configurations.REST_API + Configurations.PROFILE_PIC_PATH;
        ImageRequest request = new ImageRequest(
                endpoint,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Configurations.setProfile(response);
                    }
                },0,0, ImageView.ScaleType.CENTER_CROP,null,
                new Response.ErrorListener(){
                    public void onErrorResponse(VolleyError error){}
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = "Token " + Configurations.USER.TOKEN.getValue();
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json; charset=UTF-8");
                headers.put("Authorization",token);
                return headers;
            }
        };

        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request_queue.add(request);
    }
}
