package com.ciber.foodieshoot.applications.detection.Authentication;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Validators;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginPage extends AppCompatActivity {
    private final int PROGRESS_ID = R.id.login_progress;
    private final String[] FIELD_KEYS = Configurations.USER.getLogingFields();
    private final int[] FIELD_IDS = {R.id.input_email,R.id.input_password};

    private LayoutAuxiliarMethods layout_auxiliar;
    private Validators validator;

    //Rest api requests;
    private RequestQueue request_object;
    private JsonObjectRequest json_object;

    //Swipe up feature
    private GestureDetector gdt;
    private static final int MIN_SWIPPING_DISTANCE = 10;
    private static final int THRESHOLD_VELOCITY = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initiate auxiliar
        layout_auxiliar = new LayoutAuxiliarMethods(this);

        //Initialize validator
        validator = new Validators(this);

        //Prepare for rest requests
        request_object = Volley.newRequestQueue(this);
        layout_auxiliar.stopProgress(PROGRESS_ID);

        //Change text view colors
        int[] ids = {R.id.there_login,R.id.forgot_pass};
        String texts[] = {  " <font color=#FF0000><big>.</big></font>",
                " <font color=#FF0000><big>?</big></font>"
        };
        layout_auxiliar.changeColor(ids,texts);
        forgotPasswordLink();
        addBottomScroll();
        checkLogin();
        moveToSignUp();
        loginButtonPressed();
    }

    private void checkLogin(){
        if(Configurations.USER.TOKEN.getValue() != null){
            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
            //Move to detection activity
            layout_auxiliar.openActivity(DetectorActivity.class);
        }
    }

    private void moveToSignUp(){
        TextView sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setMovementMethod(LinkMovementMethod.getInstance());
        sign_up.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                layout_auxiliar.openActivity(SignUp.class);
            }
        });
    }

    /**
     * Method to add a clickable link redirecting user to reset password page on website
     */
    protected void forgotPasswordLink(){
        TextView forgot_link = (TextView) findViewById(R.id.forgot_pass);
        forgot_link.setMovementMethod(LinkMovementMethod.getInstance());
        forgot_link.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String endpoint = layout_auxiliar.buildUrl(new String[]{Configurations.SERVER_URL,Configurations.FORGOT_PASSWORD_PATH});
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(endpoint));
                startActivity(browserIntent);
            }
        });
    }

    private void addBottomScroll(){
        ImageView imageView = (ImageView) findViewById(R.id.continue_no_login);
        gdt = new GestureDetector(new GestureListener());
        imageView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            } });
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if (e1.getX() - e2.getX() > MIN_SWIPPING_DISTANCE && Math.abs(velocityX) > THRESHOLD_VELOCITY)
            {
                Toast.makeText(getApplicationContext(), "You have swipped left side", Toast.LENGTH_SHORT).show();
                layout_auxiliar.openActivity(DetectorActivity.class);
                return false;
            }
            else if (e2.getX() - e1.getX() > MIN_SWIPPING_DISTANCE && Math.abs(velocityX) > THRESHOLD_VELOCITY)
            {
                Toast.makeText(getApplicationContext(), "You have swipped right side", Toast.LENGTH_SHORT).show();
                layout_auxiliar.openActivity(DetectorActivity.class);
                return false;
            }
            return false;
        }
    }

    private void loginButtonPressed(){
        Button login_button = (Button) findViewById(R.id.login);
        login_button.setMovementMethod(LinkMovementMethod.getInstance());
        login_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                layout_auxiliar.startProgress(PROGRESS_ID);;
                if(validator.verifyFields(FIELD_IDS)) makeLoginRequest();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout_auxiliar.stopProgress(PROGRESS_ID);
                    }
                }, 500);
            }
        });
    }

    private void makeLoginRequest(){
        String endpoint = layout_auxiliar.buildUrl(new String[]{Configurations.SERVER_URL,Configurations.REST_API,Configurations.LOGIN_PATH});
        Map<String,String> params = layout_auxiliar.buildParams(FIELD_KEYS,FIELD_IDS);

        JSONObject parameters = new JSONObject(params);
        json_object = new JsonObjectRequest(
                Request.Method.POST,
                endpoint,
                parameters,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        Log.e(Configurations.REST_API,response.toString());
                        //Parse response
                        try {
                            String status = response.get("status").toString();
                            if(status.equals("success")){
                                layout_auxiliar.setUserVars(response);
                                layout_auxiliar.openActivity(Logged_Home.class);
                            }
                            //fail
                            JSONObject jsonobject = (JSONObject) response.get("error");
                            Iterator<String> keys = jsonobject.keys();
                            while(keys.hasNext()){
                                String key = keys.next();
                                switch (key){
                                    case "credentials":
                                        ((EditText) findViewById(R.id.input_password)).setError(getString(R.string.credential_error)); break;
                                    case "user": ((EditText) findViewById(R.id.input_email)).setError(getString(R.string.invalid_email)); break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layout_auxiliar.stopProgress(PROGRESS_ID);
                            }
                        }, 1500);

                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                                error.printStackTrace();
                                Log.e(Configurations.REST_AUTH_FAIL,error.toString());
                            }
                        }
        );

        request_object.add(json_object);
    }
}
