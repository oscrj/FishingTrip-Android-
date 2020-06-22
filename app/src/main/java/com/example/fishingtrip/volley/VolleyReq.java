package com.example.fishingtrip.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyReq {

    private RequestQueue requestQueue;
    private Context context;
    private static VolleyReq volleyReqInstance;

    private VolleyReq(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized VolleyReq getInstance(Context context){
        if (volleyReqInstance == null){
            volleyReqInstance = new VolleyReq(context);
        }
        return volleyReqInstance;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request req){
        getRequestQueue().add(req);
    }

}
