package com.dns.buggycrypto;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;


public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context appContext;

    private VolleySingleton(Context context) {
        appContext = context;

        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (null == instance) {
            instance = new VolleySingleton(context.getApplicationContext());
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (null == requestQueue) {
            /* Set up cookie store */
            CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);

            requestQueue = Volley.newRequestQueue(appContext.getApplicationContext());

        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
