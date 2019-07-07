package com.dns.buggycrypto;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;


import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.muddzdev.styleabletoast.StyleableToast;
import com.scottyab.aescrypt.AESCrypt;

import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {


    static {
        try {
            System.loadLibrary("getkey");
        } catch (UnsatisfiedLinkError ule) {
            Log.e("HelloC", "WARNING: Could not load native library: " + ule.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String secureKey = stringKeyFromJNI();
        System.out.println(secureKey);

        String documentID = "1337";
        String hashedDocumentId = signDocumentID(documentID, secureKey);


        performWebRequest(documentID,"http://192.168.56.1:5000");



    }

    private String signDocumentID(String documentID, String secureKey) {
        System.out.println("Document ID = "+documentID);
        System.out.println("secureKey = "+secureKey);

        try {
            String encryptedMsg = AESCrypt.encrypt(secureKey,documentID);
            System.out.println("encryptedMsg = "+encryptedMsg);
         //   System.out.println("decryptedMSG = "+AESCrypt.decrypt(secureKey,encryptedMsg));
            return encryptedMsg;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return documentID;
    }

    private void performWebRequest(String document_id, String login_url) {

        WebRequest requestBody = new WebRequest();
        requestBody.setDocumentId("1337");

        NetworkRequest<WebResponse> request =
                NetworkRequest.createWithErrorListener(Request.Method.POST, "show_data",
                        requestBody, WebResponse.class,
                        makeResponseListener(document_id), makeErrorListener(), getApplicationContext(), login_url);

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private Response.Listener<WebResponse>  makeResponseListener(String document_id) {

        return new Response.Listener<WebResponse>() {
            @Override
            public void onResponse(WebResponse response) {

                if (response.getSuccess()) {
                    System.out.println(response.getWebRequestMessage());
                }else{
                    System.out.println("Response Error. Something went wrong!");

                }
            }
        };
    }


    private Response.ErrorListener makeErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Log.d("TimeoutError", "Request timed out");
                    StyleableToast.makeText(getApplicationContext(), "Request timed out", R.style.redColor3).show();

                } else {
                    StyleableToast.makeText(getApplicationContext(), "An error occurred. See logs for more information.", R.style.redColor3).show();

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

    public native String  stringKeyFromJNI();


    private void showMainCryptoOptions() {

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setTitle("Select Option")
            .setMessage("What do you want to do?")
            .addButton("AES encrypt the plaintext secret file using Static Key", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                Toast.makeText(MainActivity.this, "Upgrade tapped", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            })
            .addButton("AES encrypt the plaintext secret file - using JNI key", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {

            });
        builder.show();

    }
}
