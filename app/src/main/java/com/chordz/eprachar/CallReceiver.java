package com.chordz.eprachar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("CallReceiver", "Outgoing call to: " + phoneNumber);

            // Store the last dialed number in SharedPreferences or any other storage mechanism.
            // You can then retrieve and display this number in your app.
            saveLastDialedNumber(context, phoneNumber);
        }
    }

    private void saveLastDialedNumber(Context context, String phoneNumber) {
        Log.d("CallReceiver", "Saving last dialed number: " + phoneNumber);

        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                .edit()
                .putString("lastDialedNumber", phoneNumber)
                .apply();

    }
}
