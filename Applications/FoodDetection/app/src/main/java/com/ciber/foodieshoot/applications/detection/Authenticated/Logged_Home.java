package com.ciber.foodieshoot.applications.detection.Authenticated;

import androidx.appcompat.app.AppCompatActivity;

import com.ciber.foodieshoot.applications.detection.Authentication.LoginPage;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Validators;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Logged_Home extends AppCompatActivity {
    private LayoutAuxiliarMethods layout_auxiliar;
    private Validators validator;

    //Swipe up feature
    private GestureDetector gdt;
    private static final int MIN_SWIPPING_DISTANCE = 10;
    private static final int THRESHOLD_VELOCITY = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged__home);
        //Initiate auxiliar
        layout_auxiliar = new LayoutAuxiliarMethods(this);

        //Initialize validator
        validator = new Validators(this);

        addBottomScroll();
    }



    private void addBottomScroll(){
        ImageView imageView = (ImageView) findViewById(R.id.open_camera);
        gdt = new GestureDetector(new Logged_Home.GestureListener());
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
            Toast.makeText(getApplicationContext(), getString(R.string.open_camera), Toast.LENGTH_SHORT).show();
            layout_auxiliar.openActivity(DetectorActivity.class);
            return false;
        }
        else if (e2.getX() - e1.getX() > MIN_SWIPPING_DISTANCE && Math.abs(velocityX) > THRESHOLD_VELOCITY)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.open_camera), Toast.LENGTH_SHORT).show();
            layout_auxiliar.openActivity(DetectorActivity.class);
            return false;
        }
        return false;
    }
    }

}
