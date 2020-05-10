package com.ciber.foodieshoot.applications.detection.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        changeColor();
        loginPress();
    }

    protected void changeColor(){
        int[] ids = {R.id.join_us_signup};
        String texts[] = {  " <font color=#FF0000><big>.</big></font>"};
        int i = 0;
        for(int id : ids){
            TextView view = (TextView)findViewById(id);
            String s = view.getText().toString() + texts[i++];
            view.setText(Html.fromHtml(s));
        }
    }

    protected void loginPress(){
        TextView login = (TextView) findViewById(R.id.login);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(
                        SignUp.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
