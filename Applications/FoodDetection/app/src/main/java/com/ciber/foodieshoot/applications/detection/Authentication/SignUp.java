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
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RegisterParser.RegisterErrorParser;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RegisterParser.RegisterResponseParser;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RegisterParser.RegisterResponseValidator;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Validators;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Initiate auxiliar
        layout_auxiliar = new LayoutAuxiliarMethods(this);

        //Initialize validator
        validator = new Validators(this);

        layout_auxiliar.stopProgress(PROGRESS_ID);

        //Change text view colors
        int[] ids = {R.id.join_us_signup};
        String texts[] = {  " <font color=#FF0000><big>.</big></font>"};
        layout_auxiliar.changeColor(ids,texts);
        seeTerms();
        signButtonPressed();
    }


    private void seeTerms(){
        TextView terms = (TextView) findViewById(R.id.terms);
        terms.setMovementMethod(LinkMovementMethod.getInstance());
        terms.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String endpoint = LayoutAuxiliarMethods.buildUrl(new String[]{Configurations.SERVER_URL,Configurations.TERMS});
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(endpoint));
                startActivity(browserIntent);
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
        String endpoint = LayoutAuxiliarMethods.buildUrl(new String[]{Configurations.SERVER_URL,Configurations.REST_API,Configurations.REGISTER_PATH});
        Map<String,String> params = layout_auxiliar.buildParams(FIELD_KEYS,FIELD_IDS);
        Context context = this;
        NetworkManager.getInstance().postRequest(endpoint, params, new RestListener() {
            @Override
            public void parseResponse(JSONObject response) {
                try {
                    RegisterResponseValidator register_validator = new RegisterResponseValidator(context,response);
                    if(register_validator.registerSuccess()){
                        Log.e(Configurations.REST_AUTH_SUCCESS,"Success");
                        layout_auxiliar.setUserVars(response);
                        Switch keep_me_logged = (Switch) findViewById(R.id.keep_me_logged);
                        if(keep_me_logged.isChecked()){
                            //NetworkManager.getInstance().saveProfileImage(true);
                            Configurations.USER_KEEP = true;
                            Configurations.setToken(Configurations.USER.TOKEN.getValue());
                        }
                        NetworkManager.getInstance().setProfileImage();
                        Configurations.sendNotification(getString(R.string.signup),getString(R.string.signup_success), NotificationManager.IMPORTANCE_DEFAULT);
                        layout_auxiliar.openActivity(Logged_Home.class);
                    }

                    register_validator.checkPasswordError(R.id.signup_password,R.id.signup_password_confirm);
                    register_validator.checkUserError(R.id.signup_username);
                    register_validator.checkEmailError(R.id.signup_email);

                    validator.markErrors(register_validator.getMarkedErrors(),register_validator.getMarkedErrorsIds());
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

            @Override
            public void handleError(VolleyError error) {
                Log.e(Configurations.REST_AUTH_FAIL,error.toString());
                Runnable dismiss = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(Configurations.REST_AUTH_FAIL,"Request timed out.");
                    }
                };
                String message = getString(R.string.server_timeout) + " - " + getString(R.string.server_unavailable);
                Toast.makeText(SplashActivity.getContextOfApplication(),message,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        layout_auxiliar.openActivity(LoginPage.class);
    }
}
