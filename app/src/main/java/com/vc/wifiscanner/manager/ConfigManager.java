package com.vc.wifiscanner.manager;

/**
 * Created by sw_01 on 2016/10/14.
 */

public class ConfigManager {

    private static ConfigManager sInstance;

    public static ConfigManager getInstance() {
        if (null == sInstance) {
            synchronized (ConfigManager.class) {
                if (null == sInstance) {
                    sInstance = new ConfigManager();
                }
            }
        }
        return sInstance;
    }






















}
