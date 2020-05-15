package com.ciber.foodieshoot.applications.detection.Configs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;

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
    public static final String HOST = "192.168.1.78";
    public static final int PORT = 8000;
    public static final String SERVER_URL = "http://192.168.1.78:8000";
    public static final String FORGOT_PASSWORD_PATH = "/password-reset/";
    public static final String REST_API = "/api/v1/";
    public static final String LOGIN_PATH = "account/login";
    public static final String REGISTER_PATH = "account/register";


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
    }

    public static boolean isAuthenticated(){return AUTHENTICATED;}

    public static void authenticate(){
        AUTHENTICATED = true;
    }

    public static void logout(){
        AUTHENTICATED = false;
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
}
