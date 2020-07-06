package com.dns.buggycrypto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    static {
        try {
            System.loadLibrary("getkey");
        } catch (UnsatisfiedLinkError ule) {
            Log.e("HelloC", "WARNING: Could not load native library: " + ule.getMessage());
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        showMainCryptoOptions();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showMainCryptoOptions();

        Button saveButton = (Button) findViewById(R.id.buttonShowOptions);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainCryptoOptions();
            }
        });

//        String documentID = "1337133713371337";
//        String signed_document_id = signDocumentID(documentID);
//
//        performWebRequest(documentID,signed_document_id,"http://192.168.56.1:5000");
//
//        showMainCryptoOptions();

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

        document_id = document_id.trim();
        signed_document_id = signed_document_id.trim();
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
                    System.out.println("response.getstatus() = "+response.getstatus());
                    System.out.println("response.message = "+response.message);
                    System.out.println("response.status = "+response.status);

                    System.out.println(response.getWebRequestMessage());
//                    Log.e("exploitstatus","IN here");
                    System.out.println("Response hash = "+response.getWebRequestSignedHash());
                    System.out.println("Signature Crack Status = "+response.getRequestStatus());

                    if(response.getRequestStatus().contains("Congratulation")){
                        StyleableToast.makeText(getApplicationContext(), "Congratulations on cracking the signing!", Toast.LENGTH_LONG, R.style.greenColor2).show();
                        System.out.println("Congratulations on cracking the signing!");
                    } else{
                        StyleableToast.makeText(getApplicationContext(), "Try harder!", Toast.LENGTH_LONG, R.style.redColor3).show();
                        System.out.println("Try harder!");
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

    public native String  stringObfuscatedKeyFromJNI();



    private void showMainCryptoOptions() {

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setTitle("Select Option")
            .setMessage("What do you want to do?")
            .addButton("Secure AES encryption using Method I", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
       //         Toast.makeText(MainActivity.this, "AES encrypt the plaintext input using Static Key", Toast.LENGTH_SHORT).show();
                //"Secure AES encryption using Static Key"
                doSimpleCryptoStuff();
             //   dialog.dismiss();
            })
            .addButton("Secure AES encryption using Method II", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
               //"Secure AES encryption using JNI key"
                doJNIKeyCryptoStuff();
             //   dialog.dismiss();

            }).addButton("Secure AES encryption using Method III", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    //"Secure AES encryption using JNI key"
                    doObfuscatedJNICryptoStuff();

                    //   dialog.dismiss();

                }).addButton("Secure encryption using JNI", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    doBase64EncryptionJNI();

                    //   dialog.dismiss();

                }).addButton("AES Request Signing using JNI key", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    doKeySigningStuff();
                    //   dialog.dismiss();

                });
        builder.show();

    }

    private void doKeySigningStuff() {
        String documentID = "1337133713371337";
        String signed_document_id = signDocumentID(documentID.trim());
        final EditText newTextEditText = new EditText(this);
        newTextEditText.setSingleLine();
        newTextEditText.setHint("EG: http://192.168.56.1:5000");
        //newMasterPassEditText.setTransformationMethod(new PasswordTransformationMethod());
        //android:inputType="textPassword"

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(50,50,50,50);
        newTextEditText.setLayoutParams(lp);
        RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(newTextEditText);



        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter Server Address to Connect to")
                //  .setMessage("Summary message")
                .setView(container)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String server_Address = String.valueOf(newTextEditText.getText()).trim();
                        try {
                            if(server_Address.isEmpty()){
                                //when blank user input, use default genymotion url
                                performWebRequest(documentID,signed_document_id,"http://192.168.56.1:5000".trim());

                            }else{
                                performWebRequest(documentID,signed_document_id,server_Address);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }



    private void doSimpleCryptoStuff() {

        final EditText newTextEditText = new EditText(this);
        newTextEditText.setSingleLine();
        newTextEditText.setHint("PlainText Message");
        //newMasterPassEditText.setTransformationMethod(new PasswordTransformationMethod());
        //android:inputType="textPassword"

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(50,50,50,50);
        newTextEditText.setLayoutParams(lp);
        RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(newTextEditText);



        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter String to Encrypt")
                //  .setMessage("Summary message")
                .setView(container)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPlainText = String.valueOf(newTextEditText.getText()).trim();
                        saveInputToPreferenceFile("plaintext_string",newPlainText.trim(),R.string.static_key_encryption_file);

                        try {
                            CryptoClass crypt = new CryptoClass();
                            String cipherText = crypt.simpleAesEncryptedString(newPlainText.trim());
                            System.out.println(cipherText.trim());
                            saveInputToPreferenceFile("secure_encrypted_string",cipherText.trim(),R.string.static_key_encryption_file);
                            Toast.makeText(MainActivity.this, "After Encryption : " + cipherText.trim(), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void doBase64EncryptionJNI() {

        final EditText newTextEditText = new EditText(this);
        newTextEditText.setSingleLine();
        newTextEditText.setHint("PlainText Message");
        //newMasterPassEditText.setTransformationMethod(new PasswordTransformationMethod());
        //android:inputType="textPassword"

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(50,50,50,50);
        newTextEditText.setLayoutParams(lp);
        RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(newTextEditText);


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter String to Encrypt")
                //  .setMessage("Summary message")
                .setView(container)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPlainText = String.valueOf(newTextEditText.getText()).trim();
                        saveInputToPreferenceFile("plaintext_string",newPlainText.trim(),R.string.base64_jni_key_encryption_file);

                        try {
                            CryptoClass crypt = new CryptoClass();
                            String cipherText = crypt.base64JNIEncryptedString(newPlainText.trim());
                            System.out.println(cipherText.trim());
                            saveInputToPreferenceFile("secure_encrypted_string",cipherText.trim(),R.string.base64_jni_key_encryption_file);
                            Toast.makeText(MainActivity.this, "After Encryption : " + cipherText.trim(), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void doObfuscatedJNICryptoStuff() {

        final EditText newTextEditText = new EditText(this);
        newTextEditText.setSingleLine();
        newTextEditText.setHint("PlainText Message");
        //newMasterPassEditText.setTransformationMethod(new PasswordTransformationMethod());
        //android:inputType="textPassword"

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(50,50,50,50);
        newTextEditText.setLayoutParams(lp);
        RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(newTextEditText);



        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter String to Encrypt")
                //  .setMessage("Summary message")
                .setView(container)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPlainText = String.valueOf(newTextEditText.getText()).trim();
                        saveInputToPreferenceFile("plaintext_string",newPlainText.trim(),R.string.obfuscated_jni_key_encryption_file);

                        try {

                            CryptoClass crypt = new CryptoClass();
                            String cipherText = crypt.obfuscatedAesEncryptedString(newPlainText.trim());
                            System.out.println(cipherText.trim());
                            saveInputToPreferenceFile("secure_encrypted_string",cipherText.trim(),R.string.obfuscated_jni_key_encryption_file);
                            Toast.makeText(MainActivity.this, "After Encryption : " + cipherText.trim(), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    private void doJNIKeyCryptoStuff() {

        final EditText newTextEditText = new EditText(this);
        newTextEditText.setSingleLine();
        newTextEditText.setHint("PlainText Message");
        //newMasterPassEditText.setTransformationMethod(new PasswordTransformationMethod());
        //android:inputType="textPassword"

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(50,50,50,50);
        newTextEditText.setLayoutParams(lp);
        RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(newTextEditText);



        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter String to Encrypt")
                //  .setMessage("Summary message")
                .setView(container)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPlainText = String.valueOf(newTextEditText.getText()).trim();
                        saveInputToPreferenceFile("plaintext_string",newPlainText.trim(),R.string.jni_key_encryption_file);

                        try {
                            CryptoClass crypt = new CryptoClass();
                            String cipherText = crypt.aesEncryptedString(newPlainText.trim());
                            System.out.println(cipherText.trim());
                            saveInputToPreferenceFile("secure_encrypted_string",cipherText.trim(),R.string.jni_key_encryption_file);
                            Toast.makeText(MainActivity.this, "After Encryption : " + cipherText.trim(), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void saveInputToPreferenceFile(String key, String value, int sharedPrefFile) {

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(sharedPrefFile), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();

    }
//    public native String tempFunc(String data,int size);


}
