package com.ciber.foodieshoot.applications.detection.Auxiliar;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import com.ciber.foodieshoot.applications.detection.R;

import java.util.ArrayList;

public class Validators {
    private Context context;

    public Validators(Context context){
        this.context = context;
    }

    public boolean verifyFields(int[] ids){
        int filled = 0;
        for(int id : ids){
            EditText text = (EditText) ((Activity)context).findViewById(id);
            if(text.getText().toString().equals("")) text.setError(((Activity)context).getString(R.string.empty_field_error));
            else filled++;
        }
        return filled == ids.length;
    }

    public void markErrors(ArrayList<String> messages, ArrayList<Integer> ids){
        int i = 0;
        for(int id : ids)
            ((EditText) ((Activity)context).findViewById(id)).setError(messages.get(i++));
    }
}
