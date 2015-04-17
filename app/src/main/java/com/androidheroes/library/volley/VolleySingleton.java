package com.androidheroes.library.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kerry on 17/04/15.
 */
public class VolleySingleton {

    private static VolleySingleton volley_singleton = null;

    private RequestQueue request_queue;

    private VolleySingleton(Context context){
        request_queue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getInstance(Context context){
        if(volley_singleton == null){
            volley_singleton = new VolleySingleton(context);
        }
        return volley_singleton;
    }

    public RequestQueue getRequest_queue(){
        return request_queue;
    }

}
