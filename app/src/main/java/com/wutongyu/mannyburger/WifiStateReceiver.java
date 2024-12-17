package com.wutongyu.mannyburger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WifiStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
            showAlertDialog(context);
        }
    }

    private void showAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("WiFi 已关闭");
        builder.setMessage("请确保您的设备已连接到网络。");

        builder.setPositiveButton("重新检测", (dialog, which) -> {
            checkWifiStatus(context);
        });

        builder.setNegativeButton("退出", (dialog, which) -> {
            ((Activity) context).finish();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void checkWifiStatus(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            // WiFi 已开启，关闭对话框
            Toast.makeText(context, "WiFi 已开启", Toast.LENGTH_SHORT).show();
        } else {
            // WiFi 未开启，重新显示对话框
            showAlertDialog(context);
        }
    }
}
