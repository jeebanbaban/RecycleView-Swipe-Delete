package com.ingreens.recycleswipedelete.receivers;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.Date;

/**
 * Created by jeeban on 17/5/18.
 */

public class CallReceiver extends BroadcastReceiver {

    //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;  //because the passed incoming is only valid in ringing
    private static String contactName;
    //Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        //this.context=context;

//        TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        PhoneCallListener phoneCallListener=new PhoneCallListener();
//        telephonyManager.listen(phoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);

//------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------
       /* String statee=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (statee==null){
            String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("outgoing call number===="+number);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }else if (statee.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("incoming call number===="+number);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }else {

        }*/
//------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------
        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

            //String number=intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            //int state=0;
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

//            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(savedNumber));
//            resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME});
            //contactName=getContactName(savedNumber,context);
            getContactDetails(savedNumber,context);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("First Outgoing call number");
            System.out.println("outgoing call number===="+savedNumber);
            System.out.println("outgoing call PersonName===="+contactName);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }
        else{
            String incomingStateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(incomingStateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(incomingStateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(incomingStateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number);
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start){}
    protected void onOutgoingCallStarted(Context ctx, String number, Date start){}
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end){}
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end){}
    protected void onMissedCall(Context ctx, String number, Date start){}


    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                //contactName=getContactName(number,context);
                getContactDetails(number,context);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println("onIncomingCallStarted()");
                System.out.println("incoming call number when ringing===="+number);
                System.out.println("incoming call PersonName===="+contactName);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                    //contactName=getContactName(savedNumber,context);
                    getContactDetails(savedNumber,context);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println(" onOutgoingCallStarted()");
                    System.out.println("outgoing call number when received/offhook===="+savedNumber);
                    System.out.println("outgoing call PersonName===="+contactName);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                   // contactName=getContactName(savedNumber,context);
                    getContactDetails(savedNumber,context);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println("onMissedCall()");
                    System.out.println("missed call number ===="+savedNumber);
                    System.out.println("missed call PersonName===="+contactName);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
                else if(isIncoming){
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    //contactName=getContactName(savedNumber,context);
                    getContactDetails(savedNumber,context);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println("onIncomingCallEnded()");
                    System.out.println("incoming call number bt call ended===="+savedNumber);
                    System.out.println("incoming call PersonName===="+contactName);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
                else{
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                    //contactName=getContactName(savedNumber,context);
                    getContactDetails(savedNumber,context);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println("onOutgoingCallEnded()");
                    System.out.println("outgoing call number bt call ended===="+savedNumber);
                    System.out.println("outgoing call PersonName===="+contactName);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
                break;
        }
        lastState = state;


    }
//------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------

    //method to retrieve contact name

    private void getContactDetails(String number, Context context) {
        StringBuffer stringBuffer=new StringBuffer();
        String contactNumber = "";

        // // define the columns I want the query to return
        String[] projection = new String[] {
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.HAS_PHONE_NUMBER };

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));

        Uri contctUri = Uri.withAppendedPath(
                CallLog.Calls.CONTENT_FILTER_URI,
                Uri.encode(number));

        // query time
        //Cursor cursor = context.getContentResolver().query(contactUri,projection, null, null, null);

        Cursor cursr = context.getContentResolver().query(contctUri,
                null, null, null, null);
        // querying all contacts = Cursor cursor =
        // context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
        // projection, null, null, null);

        if (cursr.moveToFirst()) {
            /*contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));*/
            String callNumber=cursr.getString(cursr.getColumnIndex(CallLog.Calls.NUMBER));
            String callType=cursr.getString(cursr.getColumnIndex(CallLog.Calls.TYPE));
             String callDate=cursr.getString(cursr.getColumnIndex(CallLog.Calls.DATE));
            Date callDayTime=new Date(Long.valueOf(callDate));
            String callDuration=cursr.getString(cursr.getColumnIndex(CallLog.Calls.DURATION));
            stringBuffer.append("Call Details :");
            String dir=null;
            int dirCode=Integer.parseInt(callType);
            switch (dirCode){
                case CallLog.Calls.OUTGOING_TYPE:
                    dir="OUTGOING";
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    System.out.println("outgoing call number=="+callNumber);
                    System.out.println("outgoing call type=="+callType);
                    System.out.println("outgoing call date=="+callDate);
                    System.out.println("outgoing callDayTime=="+callDayTime);
                    System.out.println("outgoing call durattion=="+callDuration);
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    System.out.println("incoming call number=="+callNumber);
                    System.out.println("incoming call type=="+callType);
                    System.out.println("incoming call date=="+callDate);
                    System.out.println("incoming callDayTime=="+callDayTime);
                    System.out.println("incoming call durattion=="+callDuration);
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    System.out.println("missed call number=="+callNumber);
                    System.out.println("missed call type=="+callType);
                    System.out.println("missed call date=="+callDate);
                    System.out.println("missed callDayTime=="+callDayTime);
                    System.out.println("missed call durattion=="+callDuration);
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    break;
            }
            stringBuffer.append("\nPhone Number:--- " + callNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration +"\nCall type:--- " +callType);
            stringBuffer.append("\n----------------------------------");
        }
        cursr.close();
        //return contactNumber.equals("") ? number : contactName;

    }


}
