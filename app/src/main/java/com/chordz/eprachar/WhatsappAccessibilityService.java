package com.chordz.eprachar;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.chordz.eprachar.preferences.AppPreferences;

import java.util.List;

public class WhatsappAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (getRootInActiveWindow() == null) {
            return;
        }

        AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());

        List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
        if (messageNodeList != null && !messageNodeList.isEmpty()) {
            AccessibilityNodeInfoCompat messageField = messageNodeList.get(0);
            if (messageField.getText() == null || messageField.getText().length() == 0
                    || !messageField.getText().toString().endsWith(getApplicationContext().getString(R.string.whatsapp_suffix))) { // So your service doesn't process any message, but the ones ending your apps suffix
                return;
            }
        }
        // Whatsapp send button id
        List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
        if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty()) {
            return;
        }

        AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get(0);
        if (!sendMessageButton.isVisibleToUser()) {
            return;
        }
        if (!AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)
                || !AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)) {
            // Now fire a click on the send button
            return;
        }
        if (AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)
        ) {
            sendMessageButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        // Now go back to your app by clicking on the Android back button twice: 
        // First one to leave the conversation screen 
        // Second one to leave whatsapp
        try {
            Thread.sleep(500); // hack for certain devices in which the immediate back click is too fast to handle
            if (!AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)
                    || !AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)) {
                // Now fire a click on the send button
                return;
            }
            if (AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)
            ) {
                performGlobalAction(GLOBAL_ACTION_BACK);
                performGlobalAction(GLOBAL_ACTION_BACK);
            }
            Thread.sleep(500);  // same hack as above
        } catch (InterruptedException ignored) {
        }
        if (!AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)
                || !AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)) {
            // Now fire a click on the send button
            return;
        }
        if (AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)
        ) {
            performGlobalAction(GLOBAL_ACTION_BACK);
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }

    @Override
    public void onInterrupt() {

    }
}