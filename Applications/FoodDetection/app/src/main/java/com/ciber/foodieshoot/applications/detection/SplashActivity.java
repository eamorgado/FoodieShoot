package com.ciber.foodieshoot.applications.detection;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Authentication.LoginPage;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Alert;
import com.ciber.foodieshoot.applications.detection.Auxiliar.CalorieParser.FoodCalories;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends Activity {
    private static Context application_context;
    private static LayoutAuxiliarMethods layout_auxiliar;

    /**
     * Start activity
     * Set user not logged in
     * Initiate system network manager
     * Read file for calorie info
     * Initiate activity layout helper
     * Verify if user is logged in
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);

        application_context = this;

        Configurations.logout();
        Configurations.deleteProfilePic(false);

        //Initiate network manager
        NetworkManager.getInstance(this);

        //read json file
        FoodCalories.readFile(this);

        layout_auxiliar = new LayoutAuxiliarMethods(this);
        checkAuthenticated();
    }

    /**
     * Method to return context of activity
     *  used as a reference for classes outside activity type
     * @return
     */
    public static Context getContextOfApplication(){
        return application_context;
    }

    /**
     * Check if player is authenticated:
     *      1) Verify if app has saved token
     *      - No => Jump to login
     *      - Yes:
     *          + Verify Server connection if can't connect display error message and jump to login
     *          + Query the server to check if token is valid and authenticate user
     */
    private void checkAuthenticated(){
        if(!Configurations.checkToken()){
            //No saved token => jump to login page
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout_auxiliar.openActivity(LoginPage.class);
                }
            }, 1500);
        }
        else{
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
                        //Server is responsive => request auth
                        String status = response.get("status").toString();
                        if(status.equals("success")){
                            Configurations.authenticate();
                            layout_auxiliar.setUserVars(response);
                            Configurations.setToken(Configurations.USER.TOKEN.getValue());

                            //NetworkManager.getInstance().saveProfileImage(true);
                            NetworkManager.getInstance().setProfileImage();
                            SplashActivity.layout_auxiliar.openActivity(Logged_Home.class);
                        }
                        else{
                            //Status fail => go to login page
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SplashActivity.layout_auxiliar.openActivity(LoginPage.class);
                                }
                            }, 1500);
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                        SplashActivity.layout_auxiliar.openActivity(LoginPage.class);
                    }
                }
                @Override
                public void handleError(VolleyError error) {
                    //Server not connected
                    Log.e(Configurations.REST_AUTH_FAIL,error.toString());
                    Runnable dismiss = new Runnable() {
                        @Override
                        public void run() {
                            Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out.");
                            SplashActivity.layout_auxiliar.openActivity(LoginPage.class);
                        }
                    };
                    Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out. " + error.toString());
                    Alert.infoUser(SplashActivity.getContextOfApplication(),getString(R.string.server_connection),getString(R.string.server_unavailable),getString(R.string.ok),dismiss);
                }
            });
        }
    }
}