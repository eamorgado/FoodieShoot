package com.ciber.foodieshoot.applications.detection.Authenticated;

import androidx.appcompat.app.AppCompatActivity;

import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Validators;
import com.ciber.foodieshoot.applications.detection.Authenticated.Camera.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Logged_Home extends AppCompatActivity {
    private LayoutAuxiliarMethods layout_auxiliar;
    private Validators validator;

    //Swipe up feature
    private GestureDetector gdt;
    private static final int MIN_SWIPPING_DISTANCE = 20;
    private static final int THRESHOLD_VELOCITY = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged__home);
        //Initiate auxiliar
        layout_auxiliar = new LayoutAuxiliarMethods(this);

        //Initialize validator
        validator = new Validators(this);

        openCamera();
    }

    private void openCamera(){
        Button open_camera = (Button) findViewById(R.id.open_camera);
        open_camera.setMovementMethod(LinkMovementMethod.getInstance());
        open_camera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                layout_auxiliar.openActivity(com.ciber.foodieshoot.applications.detection.Authenticated.Camera.DetectorActivity.class);
            }
        });
    }

}
