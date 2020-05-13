package com.ciber.foodieshoot.applications.detection.Auxiliar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;

public class Alert {

    public static void alertUser(Context c,String title,String message,String positive_s, String negative_s, Runnable positive, Runnable negative){
        new AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive_s, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().post(positive);
                    }
                })
                .setNegativeButton(negative_s, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().post(negative);
                        dialog.dismiss();
                    }
                })
                .setIcon(c.getResources().getDrawable(R.drawable.logo))
                .show();
    }

    public static void infoUser(Context c,String title,String message,String positive_s, Runnable positive){
        new AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive_s, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().post(positive);
                    }
                })
                .setIcon(c.getResources().getDrawable(R.drawable.logo))
                .show();
    }
}
