package com.vc.wifiscanner.utils;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.vc.wifiscanner.WifiScannerActivity;
import com.vc.wifiscanner.manager.ConfigManager;

import java.util.Comparator;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by sw_01 on 2016/10/14.
 */
public class WifiScanUtils {

    private static final int MAX_PRIORITY = 99999;

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
            return MAX_PRIORITY;
        }
        for (WifiConfiguration conf:configurations) {
            if (conf.priority >= maxPri ){
                maxPri = conf.priority;
            }
        }
        return maxPri;
    }

    private static void sortByPriority(final List<WifiConfiguration> configurations) {
        java.util.Collections.sort(configurations, new Comparator<WifiConfiguration>() {

            @Override
            public int compare(WifiConfiguration object1,
                               WifiConfiguration object2) {
                return object1.priority - object2.priority;
            }
        });
    }
}
