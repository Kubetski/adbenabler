package com.example.adbenabler;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AdminReceiver extends DeviceAdminReceiver {
    private static final String TAG = "AdbEnabler";

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.d(TAG, "Device admin enabled. Enabling ADB...");
        enableAdb(context);
    }

    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        Log.d(TAG, "Profile provisioning complete. Enabling ADB...");
        enableAdb(context);
    }

    private void enableAdb(Context context) {
        try {
            DevicePolicyManager dpm = (DevicePolicyManager)
                context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName cn = new ComponentName(context, AdminReceiver.class);

            if (dpm.isAdminActive(cn)) {
                dpm.setGlobalSetting(cn, "adb_enabled", "1");
                // Also set USB config for immediate ADB on cable connect
                dpm.setGlobalSetting(cn, "adb_enabled", "1");
                dpm.setGlobalSetting(cn, "development_settings_enabled", "1");
                Log.d(TAG, "ADB enabled via DevicePolicyManager.setGlobalSetting()");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to enable ADB: " + e.getMessage());
        }
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "This app is needed for ADB provisioning";
    }
}
