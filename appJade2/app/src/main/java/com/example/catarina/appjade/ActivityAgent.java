package com.example.catarina.appjade;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.util.logging.Level;

import jade.android.RuntimeServiceBinder;
import jade.core.Agent;
import jade.util.leap.Properties;
import jade.android.AndroidHelper;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.MicroRuntime;
import jade.core.Profile;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Created by Catarina on 23/11/2015.
 */
public class ActivityAgent extends Activity{


    public MicroRuntimeServiceBinder microRuntimeServiceBinder;
    private ServiceConnection serviceConnection;
    Info info=new Info();
    private LocationManager locationManager;
    public ActivityAgent act;

    private final LocationListener gpsLocationListener =new LocationListener(){

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            try {
                locationManager.removeUpdates(networkLocationListener);
            }catch(Exception e){}

        }
    };

    private final LocationListener networkLocationListener =
            new LocationListener(){

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras){

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

                @Override
                public void onLocationChanged(Location location) {

                }
            };


     @Override
       protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 5000, 0,
                    networkLocationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000, 0, gpsLocationListener);
        }catch (Exception e){}
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(networkLocationListener);
            locationManager.removeUpdates(gpsLocationListener);
        }catch (Exception e){}
    }


    public MicroRuntimeServiceBinder getMicro(){
        return microRuntimeServiceBinder;
    }

    private boolean ativo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Gcontext.c=this;

        ativo=false;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activityagent);
        checkButton();
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(buttonChatListener);

    }

    public Boolean checkButton(){
        Button botao=(Button)findViewById(R.id.button1);
        if(ativo==true){
            botao.setText("off");
            ativo=false;

        }else{
            ativo=true;
            botao.setText("on");

        }
        return ativo;
    }

    private OnClickListener buttonChatListener = new OnClickListener() {
        public void onClick(View v) {

            Boolean onoff = checkButton();
            if (onoff==false) {
                try {
                    SharedPreferences settings = getSharedPreferences(
                            "androidJade", 0);
                    String host = settings.getString("defaultHost", "");
                    String port = settings.getString("defaultPort", "");
                    String name = settings.getString("defaultName", "");
                    startApp(name, host, port, agentStartupCallback);
                } catch (Exception ex) {

                }
            }
            else{
                AgentClass g=new AgentClass();
                g.DesligarAgent();
               Log.i("desligar","desligar");
            }
        }


        };

    public Info getInformacao(){
        Sensor s=new Sensor(this);
        return s.getAll();
    }
    public void startApp(final String nickname, final String host,
                          final String port,
                          final RuntimeCallback<AgentController> agentStartupCallback) {

        final Properties profile = new Properties();
        profile.setProperty(Profile.MAIN_HOST, host);
        profile.setProperty(Profile.MAIN_PORT, port);
        profile.setProperty(Profile.MAIN, Boolean.FALSE.toString());
        profile.setProperty(Profile.JVM, Profile.ANDROID);

        if (AndroidHelper.isEmulator()) {
            // Emulator: this is needed to work with emulated devices
            profile.setProperty(Profile.LOCAL_HOST, AndroidHelper.LOOPBACK);
        } else {
            profile.setProperty(Profile.LOCAL_HOST,AndroidHelper.getLocalIPAddress());
        }

        // Emulator: this is not really needed on a real device
        profile.setProperty(Profile.LOCAL_PORT, "2000");
       // System.out.println("aqui");
        if (microRuntimeServiceBinder == null) {
            serviceConnection = new ServiceConnection() {

                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
                    Log.i("service connect","ok");
                    startContainer(nickname, profile, agentStartupCallback);
                };

                public void onServiceDisconnected(ComponentName className) {
                  //  System.out.println("falhou");
                    microRuntimeServiceBinder = null;

                }
            };

            Boolean aaa=bindService(new Intent(getApplicationContext(),
                            MicroRuntimeService.class), serviceConnection,
                    Context.BIND_AUTO_CREATE);
            //System.out.println(aaa);
        } else {

            startContainer(nickname, profile, agentStartupCallback);
        }
    }

    private void startContainer(final String nickname, Properties profile,
                                final RuntimeCallback<AgentController> agentStartupCallback) {
        if (!MicroRuntime.isRunning()) {
            microRuntimeServiceBinder.startAgentContainer(profile,
                    new RuntimeCallback<Void>() {
                        @Override
                        public void onSuccess(Void thisIsNull) {
                            Log.i("Container", "Successfully start of the container...");
                            startAgent(nickname, agentStartupCallback);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //logger.log(Level.SEVERE, "Failed to start the container...");
                            Log.i("Container", "Unsuccessfully start of the container...");
                        }
                    });
        } else {
            startAgent(nickname, agentStartupCallback);
        }
    }



    private void startAgent(final String nickname,
                            final RuntimeCallback<AgentController> agentStartupCallback) {
        microRuntimeServiceBinder.startAgent(nickname,
                AgentClass.class.getName(),
                new Object[]{getApplicationContext()},
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void thisIsNull) {
                        Log.i("agent", "Successfully start of the agent...");
                        try {
                            agentStartupCallback.onSuccess(MicroRuntime
                                    .getAgent(nickname));
                        } catch (ControllerException e) {
                            // Should never happen
                            agentStartupCallback.onFailure(e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        //	logger.log(Level.SEVERE, "Failed to start the "								+ ChatClientAgent.class.getName() + "...");
                        Log.i("agent", "Unsuccessfully start of the agent...");
                        agentStartupCallback.onFailure(throwable);
                    }
                });
    }

    private RuntimeCallback<AgentController> agentStartupCallback = new RuntimeCallback<AgentController>() {
        @Override
        public void onSuccess(AgentController agent) {
        }

        @Override
        public void onFailure(Throwable throwable) {
            //	logger.log(Level.INFO, "Nickname already in use!");
            //myHandler.postError(getString(R.string.msg_nickname_in_use));
        }
    };

}
