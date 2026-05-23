package com.example.adbenabler;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.UserManager;
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

        try {
            DevicePolicyManager dpm = (DevicePolicyManager)
                context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName cn = new ComponentName(context, AdminReceiver.class);

            // Mark provisioning as complete so setup continues
            dpm.setProfileProvisioningUri(cn, Uri.EMPTY);
            Log.d(TAG, "Provisioning marked complete");

            // Allow the setup wizard to proceed
            UserManager um = context.getSystemService(UserManager.class);
            if (um != null) {
                um.setUserProvisioned();
                Log.d(TAG, "User marked as provisioned");
            }
        } catch (Exception e) {
            Log.e(TAG, "Provisioning completion failed: " + e.getMessage());
        }
    }

    private void enableAdb(Context context) {
        try {
            DevicePolicyManager dpm = (DevicePolicyManager)
                context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName cn = new ComponentName(context, AdminReceiver.class);

            if (dpm.isAdminActive(cn)) {
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