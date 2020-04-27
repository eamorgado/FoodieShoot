package com.ciber.foodieshoot.applications.detection;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;

public class LoginPage extends AppCompatActivity {
    private GestureDetector gdt;
    private static final int MIN_SWIPPING_DISTANCE = 10;
    private static final int THRESHOLD_VELOCITY = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeColor();
        forgotPasswordLink();
        addBottomScroll();
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
                browserIntent.setData(Uri.parse(Configurations.URL));
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
}
