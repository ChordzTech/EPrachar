package com.chordz.eprachar;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.chordz.eprachar.data.StoreData;
import com.chordz.eprachar.data.response.DataItem;
import com.chordz.eprachar.data.response.ElectionMessageResponse;

import java.util.List;

public class EndCallReceiver extends BroadcastReceiver {
    private AccessibilityServiceManager serviceManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String phoneNumber = bundle.getString("incoming_number");
        String phoneStateString = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.e("TAG", "onReceive: " + phoneNumber + " " + phoneStateString);
        if (phoneNumber != null && !phoneNumber.isEmpty()
                && ((phoneStateString.contains("OFFHOOK")) || (phoneStateString.contains("RINGING")))
        ) {
            openWhatsApp(context, phoneNumber);
        }
    }

    private void openWhatsApp(Context context, String phoneNumber) {


        serviceManager = new AccessibilityServiceManager(context);

        if (serviceManager.hasAccessibilityServicePermission(MyAccessibilityService.class)) {


            ElectionMessageResponse response = StoreData.INSTANCE.getMsgDetails();
            if (response != null && response.getData() != null) {

                List<DataItem> details = response.getData();


                String defaultMessage = details.get(0).getAMessage();
                String defaultImage = details.get(0).getAImage();
                sendSMSMessage(phoneNumber, defaultMessage);

                //Code For New Line
//                String formattedMessage = defaultMessage.replace("|", "\n");


                // Format the phone number to include the country code (e.g., +1 for the US)
                String formattedPhoneNumber = formatPhoneNumber(phoneNumber);

                // Create an Intent to open WhatsApp with the specified phone number and default message
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                whatsappIntent.setData(Uri.parse("https://wa.me/" + formattedPhoneNumber + "?text=" + Uri.encode(defaultMessage)));

                // Add FLAG_ACTIVITY_NEW_TASK flag
                whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Start the activity
                context.startActivity(whatsappIntent);
                Log.e("TAG", "onReceive: MyAccessibilityService" + "phoneNumber" + ": " + formattedPhoneNumber);

            }
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
//            serviceManager.requestUserForAccessibilityService( context);
        }
    }

    private void sendSMSMessage(String phoneNumber, String defaultMessage) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, phoneNumber, defaultMessage, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void openWhatsAppIntent(Context context) {
        Intent intent = new Intent();
        intent.setPackage("com.whatsapp");
        context.startActivity(intent);
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Implement your phone number formatting logic if needed
        return phoneNumber;
    }


    interface OnPhoneStateReceived{
        void onPhoneStateReceived(String phoneNumber);
    }




}