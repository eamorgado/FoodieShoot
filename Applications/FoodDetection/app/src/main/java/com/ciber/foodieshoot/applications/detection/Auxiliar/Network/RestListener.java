package com.ciber.foodieshoot.applications.detection.Auxiliar.Network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RestListener {
    public void parseResponse(JSONObject response);
    public void handleError(VolleyError error);
}
