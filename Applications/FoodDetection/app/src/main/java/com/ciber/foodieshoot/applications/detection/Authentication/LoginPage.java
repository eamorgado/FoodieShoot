package com.ciber.foodieshoot.applications.detection.Authentication;

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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Alert;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Validators;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class LoginPage extends AppCompatActivity {
    private static Context app_context;
    private final int PROGRESS_ID = R.id.login_progress;
    private final String[] FIELD_KEYS = Configurations.USER.getLogingFields();
    private final int[] FIELD_IDS = {R.id.input_email,R.id.input_password};

    private LayoutAuxiliarMethods layout_auxiliar;
    private Validators validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate auxiliar
        layout_auxiliar = new LayoutAuxiliarMethods(this);
        app_context = this;
        //Initialize validator
        validator = new Validators(this);

        layout_auxiliar.stopProgress(PROGRESS_ID);

        //Change text view colors
        int[] ids = {R.id.forgot_pass};
        String texts[] = { // " <font color=#FF0000><big>.</big></font>",
                " <font color=#FF0000><big>?</big></font>"
        };
        layout_auxiliar.changeColor(ids,texts);

        //Add forgot password functionality
        forgotPasswordLink();

        //Add move to sign up page
        moveToSignUp();

        //Initiate login action
        loginButtonPressed();

        //jump to no account camera
        continueNoAccount();
    }

    public static Context getContextOfApplication(){
        return app_context;
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
                String endpoint = LayoutAuxiliarMethods.buildUrl(new String[]{Configurations.SERVER_URL,Configurations.FORGOT_PASSWORD_PATH});
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(endpoint));
                startActivity(browserIntent);
            }
        });
    }

    private void continueNoAccount(){
        Button continue_no_account = (Button) findViewById(R.id.continue_no_account);
        continue_no_account.setMovementMethod(LinkMovementMethod.getInstance());
        continue_no_account.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                layout_auxiliar.openActivity(DetectorActivity.class);
            }
        });
    }

    /**
     * Verify if input is given
     * If so initiate login request and validation
     */
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
        String endpoint = LayoutAuxiliarMethods.buildUrl(new String[]{Configurations.SERVER_URL,Configurations.REST_API,Configurations.LOGIN_PATH});
        Map<String,String> params = layout_auxiliar.buildParams(FIELD_KEYS,FIELD_IDS);

        NetworkManager.getInstance().postRequest(endpoint, params, new RestListener() {
            @Override
            public void parseResponse(JSONObject response) {
                try{
                    String status = response.get("status").toString();
                    if(status.equals("success")){
                        layout_auxiliar.setUserVars(response);
                        Switch keep_me_logged = (Switch) findViewById(R.id.keep_me_logged);
                        Configurations.deleteToken();
                        Configurations.authenticate();
                        if(keep_me_logged.isChecked())
                            Configurations.setToken(Configurations.USER.TOKEN.getValue());
                        Configurations.sendNotification(getString(R.string.login_login),getString(R.string.login_success), NotificationManager.IMPORTANCE_DEFAULT);
                        layout_auxiliar.openActivity(Logged_Home.class);
                    }

                    //fail -> display errors
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
                }catch(JSONException e){
                    e.printStackTrace();
                    //Check error
                    Log.e(Configurations.REST_AUTH_FAIL,"Fail: "+e.toString());
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() { layout_auxiliar.stopProgress(PROGRESS_ID);}
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
                if(error.networkResponse == null && (error instanceof TimeoutError || error instanceof NoConnectionError)){
                    String message = getString(R.string.server_timeout) + " - " + getString(R.string.server_unavailable);
                    Toast.makeText(LoginPage.getContextOfApplication(),message,Toast.LENGTH_LONG).show();
                }
                else{
                    String login = LoginPage.getContextOfApplication().getString(R.string.login_login);
                    String login_invalid_token = LoginPage.getContextOfApplication().getString(R.string.unable_to) + " " + login.toLowerCase();
                    login_invalid_token += ".\n" + LoginPage.getContextOfApplication().getString(R.string.token_invalid_expired);
                    Configurations.logout();
                    Configurations.deleteToken();
                    Configurations.deleteUservars();
                    Alert.infoUser(LoginPage.getContextOfApplication(),login,login_invalid_token,LoginPage.getContextOfApplication().getString(R.string.ok),dismiss);
                }
            }
        });
    }
}
