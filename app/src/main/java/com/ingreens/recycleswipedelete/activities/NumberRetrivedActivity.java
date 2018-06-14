package com.ingreens.recycleswipedelete.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ingreens.recycleswipedelete.R;
import com.ingreens.recycleswipedelete.receivers.CallReceiver;

public class NumberRetrivedActivity extends AppCompatActivity {

    private static final int CALL_NUMBER_PERMISSION_REQUEST_CODE = 1;
    private static final int REGISTER_RECEIVER_REQUEST_CODE = 2;
    CallReceiver callReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_retrived);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        callReceiver=new CallReceiver();
        getPermission();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        System.out.println("onCreate() method");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        System.out.println("onStart() method");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        System.out.println("onResume() method");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
    }

    @Override
    protected void onPause() {
        super.onPause();
       registerReceiver();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        System.out.println("onPause() method");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
    }

    @Override
    protected void onStop() {
        super.onStop();
        registerReceiver();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        System.out.println("onStop() method");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       unregisterReceiver(callReceiver);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        System.out.println("receiver ta unRegister holo");
        System.out.println("onDestroy() method a");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");

    }
    private void getPermission(){
        if (ContextCompat.checkSelfPermission(NumberRetrivedActivity.this,Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(NumberRetrivedActivity.this,Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(NumberRetrivedActivity.this,Manifest.permission.WRITE_CONTACTS)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(NumberRetrivedActivity.this,Manifest.permission.READ_CALL_LOG)==PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(NumberRetrivedActivity.this,Manifest.permission.WRITE_CALL_LOG)==PackageManager.PERMISSION_GRANTED){
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
            System.out.println("getPermission a dhukeche and permission granted hoye geche");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
            registerReceiver();
//            Intent intent=new Intent(String.valueOf(Intent.FLAG_RECEIVER_REGISTERED_ONLY));
//            startActivityForResult(intent,REGISTER_RECEIVER_REQUEST_CODE);

        }else {

            ActivityCompat.requestPermissions(NumberRetrivedActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.READ_CALL_LOG,Manifest.permission.WRITE_CALL_LOG},
                    CALL_NUMBER_PERMISSION_REQUEST_CODE);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
            System.out.println("getPermission a dhuke por permission request korche");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REGISTER_RECEIVER_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                try {
                   registerReceiver();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                       "user cancelled to register Receiver!", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to register receiver!!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CALL_NUMBER_PERMISSION_REQUEST_CODE:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getPermission();
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
                    System.out.println("permission request korar por granted hoyeche");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
                }else {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
                    System.out.println("permission denied kore dilo user");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
                    finish();
                }
                break;
        }
    }

    public void registerReceiver(){
        IntentFilter intentFilterOutgoing=new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
        IntentFilter intentFilterIncoming=new IntentFilter("android.intent.action.PHONE_STATE");
        registerReceiver(callReceiver,intentFilterIncoming);
        registerReceiver(callReceiver,intentFilterOutgoing);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        System.out.println("receiver ta register holo");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@2");
    }
}
