package com.ciber.foodieshoot.applications.detection.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.RegisterErrorParser;
import com.ciber.foodieshoot.applications.detection.Auxiliar.RegisterResponseParser;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Validators;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private final int PROGRESS_ID = R.id.signup_progress;
    private final String[] FIELD_KEYS = Configurations.USER.getSignUpFields();
    private final int[] FIELD_IDS = {
            R.id.signup_email,
            R.id.signup_username,
            R.id.signup_first_name,
            R.id.signup_last_name,
            R.id.signup_password,
            R.id.signup_password_confirm
    };

    private LayoutAuxiliarMethods layout_auxiliar;
    private Validators validator;

    private RequestQueue request_object;
    private JsonObjectRequest json_object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Initiate auxiliar
        layout_auxiliar = new LayoutAuxiliarMethods(this);

        //Initialize validator
        validator = new Validators(this);

        //Prepare for rest requests
        request_object = Volley.newRequestQueue(this);
        layout_auxiliar.stopProgress(PROGRESS_ID);

        //Change text view colors
        int[] ids = {R.id.join_us_signup};
        String texts[] = {  " <font color=#FF0000><big>.</big></font>"};
        layout_auxiliar.changeColor(ids,texts);

        loginPress();
        signButtonPressed();
    }


    private void loginPress(){
        TextView login = (TextView) findViewById(R.id.login);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                layout_auxiliar.openActivity(LoginPage.class);
            }
        });
    }

    /**
     * Method to initiate server rest register
     */
    private void signButtonPressed(){
        Button signup_button = (Button) findViewById(R.id.sign_up);
        signup_button.setMovementMethod(LinkMovementMethod.getInstance());
        signup_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                layout_auxiliar.startProgress(PROGRESS_ID);
                if(validator.verifyFields(FIELD_IDS)) makeRegisterRequest();
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


    private void makeRegisterRequest(){
        String endpoint = layout_auxiliar.buildUrl(new String[]{Configurations.SERVER_URL,Configurations.REST_API,Configurations.REGISTER_PATH});
        Map<String,String> params = layout_auxiliar.buildParams(FIELD_KEYS,FIELD_IDS);

        JSONObject parameters = new JSONObject(params);
        json_object = new JsonObjectRequest(
                Request.Method.POST,
                endpoint,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(Configurations.REST_AUTH_SUCCESS,response.toString());
                        try {
                            //String status = response.get("status").toString();
                            com.google.gson.Gson gson = new com.google.gson.Gson();
                            RegisterResponseParser parsed_response = gson.fromJson(response.toString(),RegisterResponseParser.class);
                            String status = parsed_response.getStatus();
                            Log.e("Out",status);
                            if(status.equals("success")){
                                Log.e(Configurations.REST_AUTH_SUCCESS,"Success");
                                layout_auxiliar.setUserVars(response);
                                layout_auxiliar.openActivity(Logged_Home.class);
                            }
                            //fail
                            RegisterErrorParser error = parsed_response.getError();
                            ArrayList<String> marked_errors = new ArrayList<>();
                            ArrayList<Integer> marked_errors_ids = new ArrayList<>();
                            //Check Password
                            List<String> password_error = error.getPassword();

                            if(password_error != null){
                                String password_error_messages = "";
                                for(String pe : password_error){
                                    switch (pe){
                                        case "The password is too similar to the username.":
                                            password_error_messages += (!password_error_messages.equals("")? "\n" : "") + getString(R.string.password_similar_user);
                                            break;
                                        case "This password is too short. It must contain at least 8 characters.":
                                            password_error_messages += (!password_error_messages.equals("")? "\n" : "") + getString(R.string.password_short);
                                            break;
                                        case "This password is too common.":
                                            password_error_messages += (!password_error_messages.equals("")? "\n" : "") + getString(R.string.password_common);
                                            break;
                                        case "Passwords must match.":
                                            password_error_messages += (!password_error_messages.equals("")? "\n" : "") + getString(R.string.password_common);
                                            break;

                                    }
                                }
                                marked_errors.add(password_error_messages);
                                marked_errors.add(password_error_messages);
                                marked_errors_ids.add(R.id.signup_password);
                                marked_errors_ids.add(R.id.signup_password_confirm);
                            }


                            //Check user
                            List<String> username_errors = error.getUsername();
                            if(username_errors != null){
                                String username_error = (error.getUsername()).get(0);
                                if(username_error != null && username_error.equals("A user with that username already exists.")){
                                    marked_errors.add(getString(R.string.username_exists));
                                    marked_errors_ids.add(R.id.signup_username);
                                }
                            }

                            //Check email
                            List<String> email_errors = error.getEmail();
                            if(email_errors != null){
                                String email_error = email_errors.get(0);
                                if(email_error != null && email_error.equals("Email already exists.")){
                                    marked_errors.add(getString(R.string.email_exists));
                                    marked_errors_ids.add(R.id.signup_email);
                                }
                            }
                            validator.markErrors(marked_errors,marked_errors_ids);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(Configurations.REST_AUTH_FAIL,error.toString());
                    }
                }
        );
        request_object.add(json_object);
    }
}
