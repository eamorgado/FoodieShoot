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
    private RequestQueue request_object;
    private JsonObjectRequest json_object;
    private GestureDetector gdt;
    private static final int MIN_SWIPPING_DISTANCE = 10;
    private static final int THRESHOLD_VELOCITY = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Prepare for rest requests
        request_object = Volley.newRequestQueue(this);
        stopProgress();
        changeColor();
        forgotPasswordLink();
        addBottomScroll();
        checkLogin();
        addSignUp();
        loginButtonPressed();
    }

    protected void checkLogin(){
        if(Configurations.USER_KEY != null){
            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
            showMainActivity();
        }
    }

    protected void addSignUp(){
        TextView sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setMovementMethod(LinkMovementMethod.getInstance());
        sign_up.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showSignUpPage();
            }
        });
    }

    /**
     * Method to change colors for some components in login activity
     */
    protected void changeColor(){
        int[] ids = {R.id.there_login,R.id.forgot_pass};
        String texts[] = {  " <font color=#FF0000><big>.</big></font>",
                            " <font color=#FF0000><big>?</big></font>"
                        };
        int i = 0;
        for(int id : ids){
            TextView view = (TextView)findViewById(id);
            String s = view.getText().toString() + texts[i++];
            view.setText(Html.fromHtml(s));
        }
    }

    /**
     * Method to add a clickable link redirecting user to reset password page on website
     */
    protected void forgotPasswordLink(){
        TextView forgot_link = (TextView) findViewById(R.id.forgot_pass);
        forgot_link.setMovementMethod(LinkMovementMethod.getInstance());
        forgot_link.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(Configurations.FORGOT_PASSWORD_URL));
                startActivity(browserIntent);
            }
        });
    }

    protected void addBottomScroll(){
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
                showMainActivity();
                return false;
            }
            else if (e2.getX() - e1.getX() > MIN_SWIPPING_DISTANCE && Math.abs(velocityX) > THRESHOLD_VELOCITY)
            {
                Toast.makeText(getApplicationContext(), "You have swipped right side", Toast.LENGTH_SHORT).show();
                showMainActivity();
                return false;
            }
            return false;
        }
    }

    private void showMainActivity() {
        Intent intent = new Intent(
                LoginPage.this, DetectorActivity.class);
        startActivity(intent);
        finish();
    }

    protected void showSignUpPage(){
        Intent intent = new Intent(
                LoginPage.this, SignUp.class);
        startActivity(intent);
        finish();
    }

    private void startProgress(){
        ProgressBar pb = (ProgressBar) findViewById(R.id.login_progress);
        pb.setVisibility(View.VISIBLE);
    }

    private void stopProgress(){
        ProgressBar pb = (ProgressBar) findViewById(R.id.login_progress);
        pb.setVisibility(View.INVISIBLE);
    }
    protected void loginButtonPressed(){
        Button login_button = (Button) findViewById(R.id.login);
        login_button.setMovementMethod(LinkMovementMethod.getInstance());
        login_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startProgress();
                if(verifyInputs()) buildJsonRequest();
                stopProgress();
            }
        });
    }

    private boolean verifyInputs(){
        int[] ids = {R.id.input_email,R.id.input_password};
        int correct = 0;
        for(int id : ids){
            EditText text = (EditText) findViewById(id);
            if(text.getText().toString().equals("")) text.setError(getString(R.string.empty_field_error));
            else correct++;
        }
        return correct == ids.length;
    }

    private void buildJsonRequest(){
        String endpoint = Configurations.SERVER_URL + "account/login";
        String email = ((EditText) findViewById(R.id.input_email)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.input_password)).getText().toString().trim();
        Map<String,String> params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);

        JSONObject parameters = new JSONObject(params);
        json_object = new JsonObjectRequest(
                Request.Method.POST,
                endpoint,
                parameters,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        Log.e("Login Rest response",response.toString());
                        //Convert response to map
                        try {
                            String status = response.get("status").toString();
                            if(status.equals("success")){
                                Intent intent = new Intent(LoginPage.this, Logged_Home.class);
                                startActivity(intent);
                                finish();
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
                                stopProgress();
                            }
                        }, 1500);

                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                                error.printStackTrace();
                                Log.e("Login Rest error response",error.toString());
                            }
                        }
        );

        request_object.add(json_object);
    }
}
