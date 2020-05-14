package com.ciber.foodieshoot.applications.detection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Authentication.LoginPage;
import com.ciber.foodieshoot.applications.detection.Authentication.SignUp;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Alert;
import com.ciber.foodieshoot.applications.detection.Auxiliar.CalorieParser.FoodCalories;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SplashActivity extends Activity {
    private static Context application_context;
    private static LayoutAuxiliarMethods layout_auxiliar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        application_context = getApplicationContext();

        //Initiate network manager
        NetworkManager.getInstance(this);

        //read json file
        FoodCalories.readFile(this);

        layout_auxiliar = new LayoutAuxiliarMethods(this);
        checkAuthenticated();
    }

    public static Context getContextOfApplication(){
        return application_context;
    }

    private void checkAuthenticated(){
        if(!Configurations.checkToken()){
            //Jump to LoginPage
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout_auxiliar.openActivity(LoginPage.class);
                }
            }, 1500);
        }

        //Token exists => validate
        String token = Configurations.USER.TOKEN.getValue();
        //Perform login request
        String endpoint = Configurations.SERVER_URL + Configurations.REST_API + Configurations.LOGIN_PATH;
        Map<String,String> params = new HashMap<>();
        params.put("token",token);

        NetworkManager.getInstance().postRequest(endpoint, params, new RestListener() {
            @Override
            public void parseResponse(JSONObject response) {
                try{
                    String status = response.get("status").toString();
                    if(status.equals("success")){
                        SplashActivity.layout_auxiliar.setUserVars(response);
                        Configurations.setToken(Configurations.USER.TOKEN.getValue());
                        SplashActivity.layout_auxiliar.openActivity(Logged_Home.class);
                    }

                    //Status fail => go to login page
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SplashActivity.layout_auxiliar.openActivity(LoginPage.class);
                        }
                    }, 1500);
                }catch(JSONException e){
                    e.printStackTrace();
                    SplashActivity.layout_auxiliar.openActivity(LoginPage.class);
                }
            }
            @Override
            public void handleError(VolleyError error) {
                //TODO => improve error response with popup
                //Log.e(Configurations.REST_AUTH_FAIL,error.toString());
                Runnable dismiss = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out.");
                    }
                };
                Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out.");
                //Alert.infoUser(SplashActivity.getContextOfApplication(),"Connection","Server is not available or internet is not enabled","ok",dismiss);
                SplashActivity.layout_auxiliar.openActivity(LoginPage.class);
            }
        });
    }
}