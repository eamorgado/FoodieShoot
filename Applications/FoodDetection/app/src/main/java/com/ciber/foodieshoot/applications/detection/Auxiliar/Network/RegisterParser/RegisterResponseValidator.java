package com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RegisterParser;

import android.content.Context;

import com.ciber.foodieshoot.applications.detection.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterResponseValidator {
    private Context context;
    private ArrayList<String> marked_errors;
    private ArrayList<Integer> marked_errors_ids;
    private RegisterResponseParser parsed_response;
    private RegisterErrorParser error = null;

    public RegisterResponseValidator(Context context, JSONObject response){
        this.context = context;
        com.google.gson.Gson gson = new com.google.gson.Gson();
        parsed_response = gson.fromJson(response.toString(),RegisterResponseParser.class);
        marked_errors = new ArrayList<>();
        marked_errors_ids = new ArrayList<>();
    }

    private void instanceError(){
        if(error == null) error = parsed_response.getError();
    }
    public boolean registerSuccess(){
        if(parsed_response.getStatus().equals("success"))
            return true;
        instanceError();
        return false;
    }

    public void checkPasswordError(int password, int password2){
        instanceError();
        List<String> password_error = error.getPassword();
        if(password_error != null){
            String error_messages = "";
            for(String message : password_error){
                switch (message){
                    case "The password is too similar to the username.":
                        error_messages += (!error_messages.equals("")? "\n" : "") + context.getString(R.string.password_similar_user);
                        break;
                    case "This password is too short. It must contain at least 8 characters.":
                        error_messages += (!error_messages.equals("")? "\n" : "") + context.getString(R.string.password_short);
                        break;
                    case "This password is too common.":
                        error_messages += (!error_messages.equals("")? "\n" : "") + context.getString(R.string.password_common);
                        break;
                    case "Passwords must match.":
                        error_messages += (!error_messages.equals("")? "\n" : "") + context.getString(R.string.password_common);
                        break;
                }
            }
            marked_errors.add(error_messages);
            marked_errors.add(error_messages);
            marked_errors_ids.add(password);
            marked_errors_ids.add(password2);
        }
    }

    public void checkUserError(int username){
        instanceError();
        List<String> username_errors = error.getUsername();
        if(username_errors != null){
            String username_error = (error.getUsername()).get(0);
            if(username_error != null && username_error.equals("A user with that username already exists.")){
                marked_errors.add(context.getString(R.string.username_exists));
                marked_errors_ids.add(username);
            }
        }
    }

    public void checkEmailError(int email){
        instanceError();
        List<String> email_errors = error.getEmail();
        if(email_errors != null){
            String email_error = (error.getUsername()).get(0);
            if(email_error != null && email_error.equals("Email already exists.")){
                marked_errors.add(context.getString(R.string.email_exists));
                marked_errors_ids.add(email);
            }
        }
    }

    public ArrayList<String> getMarkedErrors(){
        return marked_errors;
    }

    public ArrayList<Integer> getMarkedErrorsIds(){
        return marked_errors_ids;
    }
}
