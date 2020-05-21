package com.ciber.foodieshoot.applications.detection.Configs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Authentication.LoginPage;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Alert;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.FoodPostList;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class Configurations {
    private static boolean AUTHENTICATED = false;
    //Preferences
    public static final String SHARED_PREFS = "sharedPreferences";

    public static final String REST_AUTH_FAIL = "Rest Authentication Fail";
    public static final String REST_AUTH_SUCCESS = "Rest Authentication Successful";

    public static final String HOST = "178.79.132.23";
    public static final int PORT = 80;
    public static final String SERVER_URL = "https://178.79.132.23";
    public static final String FORGOT_PASSWORD_PATH = "/password-reset/";

    public static final String REST_API = "/api/v1/";
    public static final String LOGIN_PATH = "account/login";
    public static final String REGISTER_PATH = "account/register";
    public static final String LOGOUT_PATH = "account/logout";
    public static final String PROFILE_PIC_PATH = "account/profile";
    public static final String PROFILE_PATH = "/profile/";

    public static final  String FOODS_ANALYSE = "foods/analyse";
    public static final String POST_SAVE_PATH = "foods/posts/save";
    public static final String POST_LIST_PATH = "foods/posts/list";
    public static final String POST_DELETE_PATH = "foods/posts/delete";


    public static boolean USER_KEEP = false;
    public static Drawable USER_PROFILE = null;

    public static enum USER{
        EMAIL("email",null),
        USERNAME("username",null),
        FIRST_NAME("first_name",null),
        LAST_NAME("last_name",null),
        TOKEN("token",null);

        private String key;
        private String value;
        USER(String key, String value){
            this.key = key;
            this.value = value;
        }

        public static String[] getLogingFields(){
            return new String[]{"email","password"};
        }

        public static String[] getSignUpFields(){
            String[] fields = new String[6];

            for(int i = 0; i < USER.values().length - 1; i++)
                fields[i] = USER.values()[i].key;
            fields[4] = "password";
            fields[5] = "password2";
            return fields;
        }

        public String getKey(){
            return key;
        }

        public String getValue(){
            return value;
        }

        public void setValue(String val){
            value = val;
        }
    }

    public static void setToken(String token){
        Configurations.deleteToken();
        Context context = SplashActivity.getContextOfApplication();
        SharedPreferences preferences = context.getSharedPreferences(Configurations.SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER.TOKEN.getKey(),token);
        editor.commit();
    }

    public static boolean checkToken(){
        Context context = SplashActivity.getContextOfApplication();
        SharedPreferences preferences = context.getSharedPreferences(Configurations.SHARED_PREFS,MODE_PRIVATE);
        String token = preferences.getString(USER.TOKEN.getKey(),"");
        if(token.equals(""))
            return false;
        USER.TOKEN.setValue(token);
        return true;
    }

    public static void deleteToken(){
        Context context = SplashActivity.getContextOfApplication();
        SharedPreferences preferences = context.getSharedPreferences(Configurations.SHARED_PREFS,MODE_PRIVATE);
        if(preferences.contains(USER.TOKEN.getKey())){
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(USER.TOKEN.getKey());
            editor.apply();
        }

        //delete profile pic
        //Configurations.deleteProfilePic(true);

        Configurations.USER_KEEP = false;
    }

    public static void deleteUservars(){
        for(USER user_val : USER.values())
            user_val.setValue(null);
    }

    public static void setProfile(Bitmap img){
        Configurations.USER_PROFILE = new BitmapDrawable(SplashActivity.getContextOfApplication().getResources(),img);
    }

    public static boolean isAuthenticated(){return AUTHENTICATED;}

    public static void authenticate(){
        AUTHENTICATED = true;
    }

    public static void logout(){
        AUTHENTICATED = false;
    }

    public static void logoutRequest(){
        if(!Configurations.isAuthenticated())
            return;

        //display confirm box
        Runnable ok = new Runnable() {
            @Override
            public void run() {
                String endpoint = LayoutAuxiliarMethods.buildUrl(new String[]{Configurations.SERVER_URL,Configurations.REST_API,Configurations.LOGOUT_PATH});
                NetworkManager.getInstance().getRequest(endpoint, new RestListener() {
                    @Override
                    public void parseResponse(JSONObject response) {
                        try{
                            String status = response.get("status").toString();
                            if(status.equals("success")){
                                Configurations.logout();
                                Configurations.deleteToken();
                                Configurations.deleteUservars();
                                String logout = SplashActivity.getContextOfApplication().getString(R.string.logout);
                                String logout_message = SplashActivity.getContextOfApplication().getString(R.string.logout_message);

                                Configurations.sendNotification(logout,logout_message, NotificationManager.IMPORTANCE_DEFAULT);

                                Intent intent = new Intent(SplashActivity.getContextOfApplication(),LoginPage.class);
                                SplashActivity.getContextOfApplication().startActivity(intent);
                            }
                            else{
                                //Fails Check detail and error
                                String logout = Logged_Home.getContextOfApplication().getString(R.string.logout);
                                String logout_invalid_token = Logged_Home.getContextOfApplication().getString(R.string.unable_to) + " " + logout.toLowerCase();
                                logout_invalid_token += ".\n" + Logged_Home.getContextOfApplication().getString(R.string.token_invalid_expired);
                                Runnable dismiss = new Runnable() {
                                    @Override
                                    public void run() {}
                                };
                                Alert.infoUser(Logged_Home.getContextOfApplication(),logout,logout_invalid_token,Logged_Home.getContextOfApplication().getString(R.string.ok),dismiss);
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                            //Check error
                            Log.e(Configurations.REST_AUTH_FAIL,"Fail: "+e.toString());
                        }
                    }
                    @Override
                    public void handleError(VolleyError error) {
                        Log.e(Configurations.REST_AUTH_FAIL,error.toString());
                        Runnable dismiss = new Runnable() {
                            @Override
                            public void run() {
                                Log.e(Configurations.REST_AUTH_FAIL,"Request timed out.");
                            }
                        };
                        String message = SplashActivity.getContextOfApplication().getString(R.string.server_timeout) + " - " + SplashActivity.getContextOfApplication().getString(R.string.server_unavailable);
                        Toast.makeText(Logged_Home.getContextOfApplication(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        //don't logout
        Runnable cancel = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Logged_Home.getContextOfApplication(),Logged_Home.class);
                Logged_Home.getContextOfApplication().startActivity(intent);
            }
        };

        String logout = SplashActivity.getContextOfApplication().getString(R.string.logout);
        String logout_message = SplashActivity.getContextOfApplication().getString(R.string.logout_confirm);
        Alert.alertUser(
                Logged_Home.getContextOfApplication(),
                logout,
                logout_message,
                SplashActivity.getContextOfApplication().getString(R.string.yes),
                SplashActivity.getContextOfApplication().getString(R.string.no),
                ok,
                cancel
        );
    }




    //Notifications
    private static void createNotificationChannel(int importance){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "user_channel";
            String description = "Channel for user notifications";
            NotificationChannel ch = new NotificationChannel("FoodieShoot_notify",name,importance);
            ch.setDescription(description);
            Context context = SplashActivity.getContextOfApplication();
            try{
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(ch);
            }
            catch(Exception e){}
        }
    }

    public static void sendNotification(String title,String message,int priority){
        Context context = SplashActivity.getContextOfApplication();
        Configurations.createNotificationChannel(priority);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"FoodieShoot_notify")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(priority);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1,builder.build());
    }

    public static boolean isHostAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(Configurations.HOST, Configurations.PORT), 3000);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    public static void deleteProfilePic(boolean user_keep_logged){
        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root);
        if(dir.exists()){
            String img_name = user_keep_logged? "profile.png" : "profile_forget.png";
            File img = new File(dir,img_name);
            if(img.exists()) img.delete();
        }
    }
}
