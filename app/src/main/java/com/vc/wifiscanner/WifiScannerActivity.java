package com.vc.wifiscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.vc.wifiscanner.manager.CustomWifiManager;

import java.util.ArrayList;
import java.util.List;

public class WifiScannerActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mWifiScanResult;
    private WifiManager mWifiManager;
    private List<WifiConfiguration> mWifiConfiguration;
    private List<ScanResult> mWifiList;
    private WifiReceiver mWifiReceiver;
    private boolean mIsConnected=false;
    private List<String> mPassableHotsPot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scanner);
        mWifiReceiver = new WifiReceiver();
        mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        registReceiver();
        initView();
    }

    private void registReceiver() {
        // 注册Receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(mWifiReceiver, filter);
    }

    private void initView() {
        mWifiScanResult = (EditText) findViewById(R.id.et_wifi_scan_result);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_receive).setOnClickListener(this);
    }

    /* 监听热点变化 */
    private final class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mWifiList = mWifiManager.getScanResults();
            if (mWifiList == null || mWifiList.size() == 0 || mIsConnected)
                return;
            onReceiveNewNetworks(mWifiList);
        }
    }

    /*当搜索到新的wifi热点时判断该热点是否符合规格*/
    public void onReceiveNewNetworks(List<ScanResult> wifiList){
        mPassableHotsPot =new ArrayList<String>();
        for(ScanResult result:wifiList){
            System.out.println(result.SSID);
            if((result.SSID).contains("YRCCONNECTION"))
                mPassableHotsPot.add(result.SSID);
        }
        synchronized (this) {
            connectToHotpot();
        }
    }

    /*连接到热点*/
    public void connectToHotpot(){
        if(mPassableHotsPot ==null || mPassableHotsPot.size()==0)
            return;
        WifiConfiguration wifiConfig=this.setWifiParams(mPassableHotsPot.get(0));
        int wcgID = mWifiManager.addNetwork(wifiConfig);
        // 网络连接列表
        boolean flag=mWifiManager.enableNetwork(wcgID, true);
        if (!mPassableHotsPot.equals("YRCCONNECTION")){
            mWifiManager.disconnect();
            mWifiManager.enableNetwork(wcgID, true);
            mWifiManager.reconnect();
        }
        String currentBssid = mWifiManager.getConnectionInfo().getBSSID();
        if (currentBssid != null && currentBssid.equals("YRCCONNECTION")){
            mIsConnected = true;
        }
        System.out.println("connect success? "+mIsConnected);
    }

    /*设置要连接的热点的参数*/
    private WifiConfiguration setWifiParams(String ssid){
        WifiConfiguration apConfig = new WifiConfiguration();
        apConfig.SSID="\""+ssid+"\"";
        apConfig.preSharedKey="\"12122112\"";
        apConfig.hiddenSSID = true;
//        apConfig.status = WifiConfiguration.Status.ENABLED;
//        apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//        apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//        apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//        apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//        apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//        apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        return apConfig;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*销毁时注销广播*/
        if (mWifiReceiver != null){
            unregisterReceiver(mWifiReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                CustomWifiManager.getInstance().setWifiApEnabled(mWifiManager,true);
                break;
            case R.id.btn_receive:
                CustomWifiManager.getInstance().setWifiApEnabled(mWifiManager,false);
                mWifiManager.setWifiEnabled(true);
                mWifiManager.startScan();
//                WifiScanUtils.scanNearbyWifiDev(this,mWifiManager);
                break;
        }
    }
}
