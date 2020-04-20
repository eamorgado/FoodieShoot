package com.ciber.foodieshoot.applications.detection;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeColor();
    }

    protected void changeColor(){
        TextView view = (TextView)findViewById(R.id.there_login);
        String text ="There <font color=#FF0000><big>.</big></font>";
        view.setText(Html.fromHtml(text));
    }
}
