package com.dns.buggycrypto;

import android.os.Bundle;
import android.util.Base64;
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
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String documentID = "1337133713371337";
        String signed_document_id = signDocumentID(documentID);

        performWebRequest(documentID,signed_document_id,"http://192.168.56.1:5000");


    }

    private String signDocumentID(String documentID) {
        System.out.println("Document ID = "+documentID);
        String signedDocumentID = "";
        try {
            CryptoClass crypt = new CryptoClass();;
            signedDocumentID = crypt.aesEncryptedString(documentID);
            System.out.println(signedDocumentID);
            return signedDocumentID;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return signedDocumentID;
    }

    private void performWebRequest(String document_id, String signed_document_id, String login_url) {

        WebRequest requestBody = new WebRequest();
        requestBody.setDocumentId(document_id);
        requestBody.setSignedDocumentId(signed_document_id);

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
                    System.out.println("Response hash = "+response.getWebRequestSignedHash());
                    System.out.println("Signature Crack Status = "+response.getRequestStatus());

                    if(response.getRequestStatus().contains("Congratulations")){
                        StyleableToast.makeText(getApplicationContext(), "Congratulations on cracking the signing!", Toast.LENGTH_LONG, R.style.greenColor2).show();
                    } else{
                        StyleableToast.makeText(getApplicationContext(), "Try harder!", Toast.LENGTH_LONG, R.style.redColor3).show();
                    }
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
