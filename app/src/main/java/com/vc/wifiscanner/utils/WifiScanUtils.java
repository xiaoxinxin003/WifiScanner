package com.vc.wifiscanner.utils;

import android.net.wifi.WifiManager;

import com.vc.wifiscanner.WifiScannerActivity;
import com.vc.wifiscanner.manager.ConfigManager;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by sw_01 on 2016/10/14.
 */
public class WifiScanUtils {

    public static void scanNearbyWifiDev(WifiScannerActivity activity, WifiManager wifiManager) {
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
    }
}
