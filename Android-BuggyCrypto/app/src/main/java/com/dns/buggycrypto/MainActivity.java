package com.dns.buggycrypto;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;

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
