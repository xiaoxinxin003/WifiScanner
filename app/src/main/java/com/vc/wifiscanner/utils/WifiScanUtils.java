package com.vc.wifiscanner.utils;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.vc.wifiscanner.WifiScannerActivity;
import com.vc.wifiscanner.manager.ConfigManager;

import java.util.List;

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

    public static int getConfigWifiMaxPriority(WifiManager wifiManager){
        int maxPri = 0;
        List<WifiConfiguration> configurations =  wifiManager.getConfiguredNetworks();
        if (null == configurations){
            return maxPri;
        }
        for (WifiConfiguration conf:configurations) {
            if (conf.priority >= maxPri ){
                maxPri = conf.priority;
            }
        }
        return maxPri;
    }
}
