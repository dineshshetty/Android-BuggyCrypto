package com.dns.buggycrypto;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class NetworkRequest<T> extends JsonRequest<T> {

    private final Class<T> responseClass;

    private NetworkRequest(int method, String url, String requestString,
                           Class<T> responseClass, Response.Listener<T> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, requestString, listener, errorListener);
        /*Make sure that request is sent only once*/
        setRetryPolicy(new DefaultRetryPolicy(1000 * 60 * 5,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.responseClass = responseClass;
        Log.d("NetworkRequest", "creating network request to " + url);
    }

    public static NetworkRequest create(int method, String path, Object request,
                                        Class responseClass, Response.Listener listener, Context ctx, String login_url) {

        return createWithErrorListener(method, path, request, responseClass, listener, makeDefaultErrorListener(ctx), ctx, login_url);
    }

    public static NetworkRequest createWithErrorListener(int method, String path, Object request,
                                                         Class responseClass, Response.Listener listener,
                                                         Response.ErrorListener errorListener, Context ctx, String login_url) {

        /* Build the URI */
       // String hostname = "10.0.2.2:5000";
        String hostname = login_url;

        String uri = String.format("%s/%s", hostname,path);
        Log.d("Request URI", uri);
        /* Construct request body if there is one */
        String requestString = null;
        try {
            if (request != null) {
                ObjectMapper mapper = MapperSingleton.getInstance().getMapper();
                requestString = mapper.writeValueAsString(request);
                Log.d("Request body", requestString);
                Toast.makeText(ctx, "Request body : " + requestString.trim(), Toast.LENGTH_LONG).show();

            }
        } catch (JsonProcessingException e) {
            Log.e("NetworkRequest", e.getMessage());
            e.printStackTrace();
            //TODO: probably don't send request?
        }
        System.out.println(method);
        return new NetworkRequest(method, uri, requestString, responseClass, listener, errorListener);
    }


    private static Response.ErrorListener makeDefaultErrorListener(final Context ctx) {
        //TODO: better error handling
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                /* Figure out what it is and handle it */
                if (error instanceof AuthFailureError) {
                    Intent launchLoginActivity = new Intent(ctx, MainActivity.class);
                    ctx.startActivity(launchLoginActivity);
                } else if (error instanceof TimeoutError) {
                    Log.d("TimeoutError", "Request timed out");
                } else {
                    /* Log the error */
                    try {
                        Log.w("VolleyError", new String(error.networkResponse.data,
                                HttpHeaderParser.parseCharset(error.networkResponse.headers)));
                    } catch (UnsupportedEncodingException e) {
                        Log.d("UnsupportedEncoding", "UnsupportedEncodingException occurred");

                    } catch (NullPointerException e) {
                        Log.d("NullPointerException", "NullPointerException occurred");
                    }
                }
            }
        };
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        Log.d("NetworkResponse", "in network response!");
        Response<T> response;
        try {
            String json = new String(networkResponse.data,
                    HttpHeaderParser.parseCharset(networkResponse.headers));
            Log.d("Response", json);
            ObjectMapper mapper = MapperSingleton.getInstance().getMapper();
            T responseObject = mapper.readValue(json, responseClass);

            response = Response.success(responseObject, HttpHeaderParser.parseCacheHeaders(networkResponse));

        } catch (UnsupportedEncodingException e) {
            Log.w("NetworkRequestError", "UnsupportedEncodingException");
            e.printStackTrace();
            response = Response.error(new ParseError(e));
        } catch (IOException e) {
            Log.w("IOException", "IOException");
            e.printStackTrace();
            response = Response.error(new ParseError(e));
        }

        return response;
    }


}


