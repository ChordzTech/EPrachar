package com.chordz.eprachar;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.chordz.eprachar.preferences.AppPreferences;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        if (packageName.equals("com.whatsapp")) {
            PackageManager packageManager = this.getPackageManager();
            try {
                ApplicationInfo info = packageManager.getApplicationInfo(packageName, 0);
                String name = packageManager.getApplicationLabel(info).toString();
                try {
                    AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
                    List<AccessibilityNodeInfoCompat> sendMessageNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                    if (sendMessageNodeList == null || sendMessageNodeList.isEmpty()) {
                        return;
                    }
                    AccessibilityNodeInfoCompat sendMessage = sendMessageNodeList.get(0);
                    if (!sendMessage.isVisibleToUser()) {
                        return;
                    }
                    if (!AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.PRACHAR_ON_OFF)) {
                       return;
                    }
                    sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    try {
                        Thread.sleep(2000);
                        if (!AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.PRACHAR_ON_OFF)
                                || !AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)) {
                            // Now fire a click on the send button
                            return;
                        }
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        //add below line, if u want to close whatsApp;
                        //performGlobalAction(GLOBAL_ACTION_BACK);
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        Log.e("onAccessibilityEvent", "onAccessibilityEvent: ", e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onAccessibilityEvent: " + name);
                if (!AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.PRACHAR_ON_OFF)
                        || !AppPreferences.INSTANCE.getBooleanValueFromSharedPreferences(AppPreferences.WHATSAPP_ON_OFF)) {
                    // Now fire a click on the send button
                    return;
                }
                performGlobalAction(GLOBAL_ACTION_BACK);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED;
        info.packageNames = new String[]
                {"com.whatsapp"};
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        this.setServiceInfo(info);
        Log.d(TAG, "Accessibility service connected");
    }

}
