package com.ciber.foodieshoot.applications.detection.Auxiliar.Network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RestListener {
    void parseResponse(JSONObject response);
    void handleError(VolleyError error);
    //void handleTimeout(VolleyError error);
}
