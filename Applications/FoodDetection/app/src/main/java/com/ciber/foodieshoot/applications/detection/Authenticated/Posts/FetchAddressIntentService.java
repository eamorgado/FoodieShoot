package com.ciber.foodieshoot.applications.detection.Authenticated.Posts;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

public class FetchAddressIntentService extends IntentService {

    private ResultReceiver resultReceiver;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
