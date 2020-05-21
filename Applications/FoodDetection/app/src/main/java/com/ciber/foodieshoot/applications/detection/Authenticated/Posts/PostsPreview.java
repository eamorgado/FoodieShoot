package com.ciber.foodieshoot.applications.detection.Authenticated.Posts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ciber.foodieshoot.applications.detection.Authenticated.Logged_Home;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Alert;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.FoodPostAnalyse;
import com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts.SingleFoodInfo;
import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.NetworkManager;
import com.ciber.foodieshoot.applications.detection.Auxiliar.Network.RestListener;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;
import com.ciber.foodieshoot.applications.detection.SplashActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class PostsPreview extends AppCompatActivity {

    private LayoutAuxiliarMethods layout_auxiliar;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    FusedLocationProviderClient client;
    AddressResultReceiver resultReceiver;
    String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_preview);
        layout_auxiliar = new LayoutAuxiliarMethods(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        resultReceiver = new AddressResultReceiver(null);


            client.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {
                                Log.i("Test", location.getLatitude() + " " + location.getLongitude());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



        displayFoods();
        save();
        discard();

    }

    private void generateRow(LayoutInflater inflater,LinearLayout parent, String description_message, String contents_message){
        ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.single_food_posts,parent,false);
        TextView description = (TextView) cl.findViewById(R.id.preview_description);
        TextView contents = (TextView) cl.findViewById(R.id.preview_contents);
        description.setText(description_message);
        contents.setText(contents_message);
        parent.addView(cl);
    }

    private void displayFoods(){
        Log.i("POST_PREVIEW_ACTIVITY",FoodPostAnalyse.getInstance().getServerResponse(),null);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout ll = (LinearLayout) findViewById(R.id.preview_posts);
        Log.i("POST_PREVIEW_PREVIEW",FoodPostAnalyse.getInstance().toString());
        float total_cals = FoodPostAnalyse.getInstance().getTotalCalories();

        ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.single_food_posts,ll,false);
        TextView tv = (TextView) cl.findViewById(R.id.preview_description);
        tv.setText("Total calories: ");
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv = (TextView) cl.findViewById(R.id.preview_contents);
        tv.setText(total_cals + " kcals");
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        ll.addView(cl);

        inflater.inflate(R.layout.view_seperator,ll,true);

        List<SingleFoodInfo> foods = FoodPostAnalyse.getInstance().getAllProcessedFoods();
        int i = 0;
        for(SingleFoodInfo food : foods){
            String[] keys = food.getKeys();
            /*String[] descriptions = {
                   "Name: ","Serving quantity: ","Serving unit: ","Serving weight (grams): ","Total food calories: ",
                    "Total fat: ","Total saturated fat: ","Cholesterol: ","Sodium: ","Total carbs: ","Fiber: ",
                    "Sugar: ","Protein: ","Potassium: ","Occurrences: ",
            };*/
            //int i = 0;
            for(String key : keys){
                String contents = food.getValues(key);
                String description = key + ": ";
                generateRow(inflater,ll,description,contents);
            }
            if((i++ < foods.size()-1) && foods.size() > 1)
                inflater.inflate(R.layout.view_seperator,ll,true);
        }
    }

    private void discard(){
        Button discard = (Button) findViewById(R.id.preview_discard);
        discard.setMovementMethod(LinkMovementMethod.getInstance());
        discard.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                layout_auxiliar.openActivity(DetectorActivity.class);
            }
        });
    }

    private void save(){
        Button save = (Button) findViewById(R.id.preview_save);
        save.setMovementMethod(LinkMovementMethod.getInstance());
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            PostsPreview.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    getCurrentLocation();
                }

                String endpoint = Configurations.SERVER_URL + Configurations.REST_API + Configurations.POST_SAVE_PATH;
                JSONObject request = new JSONObject();
                //get title
                LinearLayout ll = (LinearLayout) findViewById(R.id.preview_ll_1);
                EditText text = (EditText) ll.findViewById(R.id.preview_title_save);
                String title = text.getText().toString();
                try {
                    request.put("token", Configurations.USER.TOKEN.getValue());
                    request.put("save","True");
                    request.put("contents",new JSONObject(FoodPostAnalyse.getInstance().getServerResponse()));
                    if(!title.equals("")) request.put("title",title);
                     request.put("location", address);
                    NetworkManager.getInstance().postRequestFromJson(endpoint, request, new RestListener() {
                        @Override
                        public void parseResponse(JSONObject response) {
                            Log.i("POST_SAVED_RESPONSE",response.toString());
                            try{
                                String status = response.getString("status");
                                if(status.equals("success")){
                                    Log.i("POST_SAVED_SUCC","Post saved with success " + response.toString());
                                    String post_saved = "Post saved";
                                    Toast.makeText(SplashActivity.getContextOfApplication(),post_saved,Toast.LENGTH_LONG);
                                    layout_auxiliar.openActivityExtra(Logged_Home.class,"Posts");
                                }
                            }catch (JSONException je){}
                        }

                        @Override
                        public void handleError(VolleyError error) {
                            Log.e("POST_SAVE_ERROR_VOLEY",error.toString());
                            Runnable dismiss = new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out.");
                                    Intent i = new Intent(SplashActivity.getContextOfApplication(),DetectorActivity.class);
                                    startActivity(i);
                                }
                            };
                            Log.e(Configurations.REST_AUTH_FAIL,"Update Location Request timed out.");
                            Alert.infoUser(SplashActivity.getContextOfApplication(),getString(R.string.server_connection),getString(R.string.server_unavailable),getString(R.string.ok),dismiss);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("POST_SAVE_ERROR",e.getMessage());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(PostsPreview.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(PostsPreview.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() -1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            address = String.format("Latitude: %s\nLongitude: %s", latitude, longitude);
                        }
                    }
                }, Looper.getMainLooper());

    }

    private void startIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultData != null) {
                address = resultData.getString(Constants.RESULT_DATA_KEY);
            }
        }
    }



    @Override
    public void onBackPressed() {
        layout_auxiliar.openActivity(DetectorActivity.class);
    }
}
