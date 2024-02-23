package com.dns.buggycrypto;


import static android.app.ProgressDialog.show;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;


import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muddzdev.styleabletoast.StyleableToast;
import com.tapadoo.alerter.Alerter;

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
        System.out.println(backdoorFunc("SecretCode-is-1337","-7113!"));
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

//    public native String  stringObfuscatedKeyFromJNI();



    private void showMainCryptoOptions() {
        CharSequence[] cryptoOptions = new CharSequence[]{
                "Secure AES encryption using Method I",
                "Secure AES encryption using Method II",
                "Secure AES encryption using Method III",
                "Secure encryption using JNI",
                "AES Request Signing using JNI key"
        };

        new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, R.style.AppTheme))
                .setTitle("Select Option")
                .setSingleChoiceItems(cryptoOptions, -1, (dialog, which) -> {
                    // Item click listener
                    switch (which) {
                        case 0:
                            doSimpleCryptoStuff();
                            break;
                        case 1:
                            doJNIKeyCryptoStuff();
                            break;
                        case 2:
                            doObfuscatedJNICryptoStuff();
                            break;
                        case 3:
                            doBase64EncryptionJNI();
                            break;
                        case 4:
                            doKeySigningStuff();
                            break;
                    }
                })
//                .setPositiveButton("OK", (dialog, id) -> {
//                    // You can handle the "OK" button click here if needed,
//                    // or just let it dismiss the dialog.
//                })
//                .setNegativeButton("Cancel", (dialog, id) -> {
//                    // Handle the "Cancel" button.
//                })
                .show();

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
        // Context should be Activity context for AlertDialog.Builder
        Context context = this;
        // Create an input field for the plain text message with Material Design
        TextInputLayout textInputLayout = new TextInputLayout(context);
        TextInputEditText newTextEditText = new TextInputEditText(textInputLayout.getContext());

        newTextEditText.setSingleLine();
        newTextEditText.setHint("PlainText Message");
        textInputLayout.setPadding(50, 50, 50, 50); // Set padding
//         textInputLayout.setHintTextAppearance(R.style.greenColor2); // Optionally, you can customize the hint appearance
        textInputLayout.addView(newTextEditText, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Create a container for the input field
        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams textInputLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Use MATCH_PARENT to expand width
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height is wrapped to content
        textInputLayout.setLayoutParams(textInputLayoutParams);
        container.addView(textInputLayout); // Adding to the container as before

        // AlertDialog with Material Design
        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("Enter String to Encrypt")
                .setView(container)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPlainText = newTextEditText.getText().toString().trim();
                        saveInputToPreferenceFile("plaintext_string", newPlainText, R.string.static_key_encryption_file);

                        try {
                            CryptoClass crypt = new CryptoClass();
                            String cipherText = crypt.simpleAesEncryptedString(newPlainText.trim());
                            System.out.println(cipherText.trim()); // Consider replacing System.out.println with Log.d for debugging
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("cipherText", cipherText);
                            clipboard.setPrimaryClip(clip);

                            Alerter.create(MainActivity.this)
                                    .setTitle("CipherText Saved Locally & Copied!")
                                    .setBackgroundColorRes(R.color.colorDefault)
                                    .setText(cipherText)
                                    .setDuration(5000)
                                    .setIcon(R.mipmap.ic_buggycryptologo)
                                    .setIconColorFilter(0)
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
//        Window window = dialog.getWindow();
//        if (window != null) {
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            // Optionally, you can also set the background transparent
//            // and remove any dialog-specific padding to truly utilize the full screen.
////            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////            window.getDecorView().setPadding(0, 0, 0, 0);
//
//            // Apply any additional window attributes as needed.
//            WindowManager.LayoutParams layoutParams = window.getAttributes();
//            layoutParams.dimAmount = 0.9f; // Dim amount for background
//            window.setAttributes(layoutParams);
//        }

    }


    private void doBase64EncryptionJNI() {
        // Context should be Activity context for AlertDialog.Builder
        Context context = this;

        // Create an input field for the plain text message with Material Design
        TextInputLayout textInputLayout = new TextInputLayout(context);
        TextInputEditText newTextEditText = new TextInputEditText(textInputLayout.getContext());

        newTextEditText.setSingleLine();
        newTextEditText.setHint("PlainText Message");
        textInputLayout.setPadding(50, 50, 50, 50); // Set padding
        //textInputLayout.setHintTextAppearance(R.style.YourCustomStyle); // Optionally, set a custom style
        textInputLayout.addView(newTextEditText, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Create a container for the input field
        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams textInputLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Use MATCH_PARENT to expand width
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height is wrapped to content
        textInputLayout.setLayoutParams(textInputLayoutParams);
        container.addView(textInputLayout); // Adding to the container

        // AlertDialog with Material Design
        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("Enter String to Encrypt")
                .setView(container)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPlainText = newTextEditText.getText().toString().trim();
                        saveInputToPreferenceFile("plaintext_string", newPlainText, R.string.base64_jni_key_encryption_file);

                        try {
                            CryptoClass crypt = new CryptoClass();
                            String cipherText = crypt.base64JNIEncryptedString(newPlainText);
                            System.out.println(cipherText);
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("cipherText", cipherText);
                            clipboard.setPrimaryClip(clip);

                            Alerter.create(MainActivity.this)
                                    .setTitle("CipherText Saved Locally & Copied!")
                                    .setBackgroundColorRes(R.color.colorDefault)
                                    .setText(cipherText)
                                    .setDuration(5000)
                                    .setIcon(R.mipmap.ic_buggycryptologo)
                                    .setIconColorFilter(0)
                                    .show();

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
        // Context should be Activity context for AlertDialog.Builder
        Context context = this;

        // Create an input field for the plain text message with Material Design
        TextInputLayout textInputLayout = new TextInputLayout(context);
        TextInputEditText newTextEditText = new TextInputEditText(textInputLayout.getContext());

        newTextEditText.setSingleLine();
        newTextEditText.setHint("PlainText Message");
        textInputLayout.setPadding(50, 50, 50, 50); // Set padding
//        textInputLayout.setHintTextAppearance(R.style.greenColor2); // Customizable
        textInputLayout.addView(newTextEditText, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Create a container for the input field
        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams textInputLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Use MATCH_PARENT to expand width
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height is wrapped to content
        textInputLayout.setLayoutParams(textInputLayoutParams);
        container.addView(textInputLayout); // Adding to the container as before

        // AlertDialog with Material Design
        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("Enter String to Encrypt")
                .setView(container)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    String newPlainText = newTextEditText.getText().toString().trim();
                    saveInputToPreferenceFile("plaintext_string", newPlainText, R.string.obfuscated_jni_key_encryption_file);

                    try {
                        CryptoClass crypt = new CryptoClass();
                        String cipherText = crypt.obfuscatedAesEncryptedString(newPlainText);
                        Log.d("EncryptedText", cipherText); // Use Log.d for debugging
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("cipherText", cipherText);
                        clipboard.setPrimaryClip(clip);

                        Alerter.create(MainActivity.this)
                                .setTitle("CipherText Saved Locally & Copied!")
                                .setBackgroundColorRes(R.color.colorDefault)
                                .setText(cipherText)
                                .setDuration(5000)
                                .setIcon(R.mipmap.ic_buggycryptologo)
                                .setIconColorFilter(0)
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    private void doJNIKeyCryptoStuff() {
        // Context should be Activity context for AlertDialog.Builder
        Context context = this;

        // Create an input field for the plain text message with Material Design
        TextInputLayout textInputLayout = new TextInputLayout(context);
        TextInputEditText newTextEditText = new TextInputEditText(textInputLayout.getContext());

        newTextEditText.setSingleLine();
        newTextEditText.setHint("PlainText Message");
        textInputLayout.setPadding(50, 50, 50, 50); // Set padding
        //textInputLayout.setHintTextAppearance(R.style.YourStyle); // Optional: Customize the hint appearance
        textInputLayout.addView(newTextEditText, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Adjust the layout parameters directly on the TextInputLayout as in the sample
        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams textInputLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Use MATCH_PARENT to expand width
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height is wrapped to content
        textInputLayout.setLayoutParams(textInputLayoutParams);
        container.addView(textInputLayout); // Add the TextInputLayout to the container

        // AlertDialog with Material Design
        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("Enter String to Encrypt")
                .setView(container)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    String newPlainText = newTextEditText.getText().toString().trim();
                    saveInputToPreferenceFile("plaintext_string", newPlainText, R.string.jni_key_encryption_file);

                    try {
                        CryptoClass crypt = new CryptoClass();
                        String cipherText = crypt.aesEncryptedString(newPlainText);
                        Log.d("EncryptedText", cipherText.trim()); // Use Log.d for debugging
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("cipherText", cipherText);
                        clipboard.setPrimaryClip(clip);

                        Alerter.create(MainActivity.this)
                                .setTitle("CipherText Saved Locally & Copied!")
                                .setBackgroundColorRes(R.color.colorDefault)
                                .setText(cipherText)
                                .setDuration(5000)
                                .setIcon(R.mipmap.ic_buggycryptologo)
                                .setIconColorFilter(0)
                                .show();                    } catch (Exception e) {
                        e.printStackTrace();
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

    public native String backdoorFunc(String datatojni,String datatojni2);

    public void debugJavaFunction1() {
        //Toast.makeText(MainActivity.this, "debugJavaFunction1 called", Toast.LENGTH_LONG).show();
        System.out.println("debugJavaFunction1 called");

    }

    public void debugJavaFunction2(String key, String value) {
       // Toast.makeText(MainActivity.this, "debugJavaFunction1 called", Toast.LENGTH_LONG).show();
        System.out.println("debugJavaFunction2 called");

    }

}
