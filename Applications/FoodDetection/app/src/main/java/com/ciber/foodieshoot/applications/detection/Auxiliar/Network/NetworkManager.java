package com.ciber.foodieshoot.applications.detection.Auxiliar.Network;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

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


    public void saveProfileImage(){
        ContextWrapper cw = new ContextWrapper(SplashActivity.getContextOfApplication());
        File directory = cw.getDir("profile",Context.MODE_PRIVATE);
        if(directory.exists()){
            File path = new File(directory,"profile.png");
            if(path.exists())
                return;
        }

        String endpoint = Configurations.SERVER_URL + Configurations.REST_API + Configurations.PROFILE_PIC_PATH;
        ImageRequest request = new ImageRequest(
                endpoint,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ContextWrapper cw = new ContextWrapper(SplashActivity.getContextOfApplication());
                        File directory = cw.getDir("profile",Context.MODE_PRIVATE);
                        if(!directory.exists())
                            directory.mkdir();
                        File path = new File(directory,"profile.png");
                        FileOutputStream fos = null;
                        try{
                            fos = new FileOutputStream(path);
                            response.compress(Bitmap.CompressFormat.PNG,100,fos);
                            fos.close();
                        }catch (Exception e){
                            Log.e("SAVE_IMAGE",e.getMessage(),e);
                        }
                    }
                },0,0,null,
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
}
