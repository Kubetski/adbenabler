package com.example.adbenabler;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AdbEnabler";
    private static final int REQUEST_ADMIN = 1;
    private ComponentName adminComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adminComponent = new ComponentName(this, AdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        if (dpm.isAdminActive(adminComponent)) {
            Log.d(TAG, "Already device owner. Enabling ADB via setGlobalSetting");
            try {
                dpm.setGlobalSetting(adminComponent, "adb_enabled", "1");
                dpm.setGlobalSetting(adminComponent, "development_settings_enabled", "1");
            } catch (Exception e) {
                Log.e(TAG, "Failed: " + e.getMessage());
            }
            Toast.makeText(this, "ADB enabled! Connect USB cable.", Toast.LENGTH_LONG).show();
            finishAffinity();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            startActivityForResult(intent, REQUEST_ADMIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADMIN && resultCode == RESULT_OK) {
            DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
            try {
                dpm.setGlobalSetting(adminComponent, "adb_enabled", "1");
                dpm.setGlobalSetting(adminComponent, "development_settings_enabled", "1");
            } catch (Exception e) {
                Log.e(TAG, "setGlobalSetting failed: " + e.getMessage());
            }
            Toast.makeText(this, "ADB enabled! Connect USB cable.", Toast.LENGTH_LONG).show();
        }
        finishAffinity();
    }
}
