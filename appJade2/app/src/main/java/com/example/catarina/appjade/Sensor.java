package com.example.catarina.appjade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;


/**
 * Created by Catarina on 29/11/2015.
 */
public class Sensor{

    private LocationManager locationManager;
    private String loc;
    private String bat;
    private String strt;
    private String strw;
    Info info;
    TelephonyManager telephonyManager;
    myPhoneStateListener psListener;
    Context c;

    public Sensor(Context c){
        this.c=c;
    }

    private final LocationListener gpsLocationListener =new LocationListener(){

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    info.setLocalizacao("GPS available again");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    info.setLocalizacao("GPS out of service");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    info.setLocalizacao("GPS temporarily unavailable");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            info.setLocalizacao("GPS Provider Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            info.setLocalizacao("GPS Provider Disabled");
        }

        @Override
        public void onLocationChanged(Location location) {
            try {
                locationManager.removeUpdates(networkLocationListener);
            }catch(Exception e){}
            info.setLocalizacao("New GPS location: "
                    + String.format("%9.6f", location.getLatitude()) + ", "
                    + String.format("%9.6f", location.getLongitude()));
        }
    };

    private final LocationListener networkLocationListener =
            new LocationListener(){

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras){
                    switch (status) {
                        case LocationProvider.AVAILABLE:
                            info.setLocalizacao("Network location available again");
                            break;
                        case LocationProvider.OUT_OF_SERVICE:
                            info.setLocalizacao("Network location out of service");
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            info.setLocalizacao("Network location temporarily unavailable");
                            break;
                    }
                }

                @Override
                public void onProviderEnabled(String provider) {
                    info.setLocalizacao("Network Provider Enabled");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    info.setLocalizacao("Network Provider Disabled");
                }

                @Override
                public void onLocationChanged(Location location) {
                    info.setLocalizacao("New network location: "
                            + String.format("%9.6f", location.getLatitude()) + ", "
                            + String.format("%9.6f", location.getLongitude()));
                }
            };

    public void getWifiStr(){
        WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        info.setWifi(String.valueOf(level));
    }



    private void getBatteryPercentage() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                int level;
                context.unregisterReceiver(this);
                if(intent==null){
                    level=0;
                }else {
                    int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    level = -1;
                    if (currentLevel >= 0 && scale > 0) {
                        level = (currentLevel * 100) / scale;
                    }
                }
                info.setBateria("" + level);
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        c.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    private void getLocation(){
        Location loc=null;
        LocationManager locationManager = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
        try {
            loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }catch(Exception e){
            System.out.println("Break");
        }
        if(loc==null){
            try {
                loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }catch(Exception e){
                System.out.println("Break");
            }
        }

        info.setLocalizacao("LA" + loc.getLatitude()+"LO"+loc.getLongitude());
    }

    public Info getAll(){
        info=new Info();
        //Location
        getLocation();

        //Bateria
        getBatteryPercentage();

        //Força Sinal wifi
        getWifiStr();

        return info;
    }
}
