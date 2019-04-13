package com.example.android.redhatambulance;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class dashboard extends AppCompatActivity implements SensorEventListener {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private DashboardFragment dashboardFragment;
    private ContactFragment contactFragment;
    private MedicalFragment medicalFragment;
    private AssistFragment assistFragment;
    private HospitalFragment hospitalFragment;

    public OkHttpClient client;

    Request request;
    WebSocket webSocket;

    //---------------------------------------
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;
    private float lastX, lastY, lastZ;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float vibrateThreshold = 0;
    //---------------------------------------

    private final class EchoWebSocketListener extends WebSocketListener  {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            UserSingleton.get().ws = webSocket;
            JSONObject obj=null;
            try{
                obj=new JSONObject();
                obj.put("action", "gyroReady");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Log.d("ReplyInfo","catch", e);
            }
            webSocket.send(obj.toString());
        }

        @Override
        public void onMessage(WebSocket webSocket, String text){

            JSONObject obj;
            try{
                obj=new JSONObject(text);
                String action =  obj.getString("action");
                Log.d("Reply Info",action);

                switch (action){
                    case "giveGyro":
                        JSONObject gyroObj = null;
                        try{
                            gyroObj=new JSONObject();
                            gyroObj.put("action", "gyroResponse");
                            gyroObj.put("x", deltaX);
                            gyroObj.put("y", deltaY);
                            gyroObj.put("z", deltaZ);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Log.d("ReplyInfo","catch", e);
                        }
                        webSocket.send(gyroObj.toString());
                        break;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Log.d("ReplyInfo","catch", e);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
//            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
//            output("Closing : " + code + " / " + reason);
            UserSingleton.get().ws = null;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//            output("Error : " + t.getMessage());
        }

    }

//    private void book() {
//
//        JSONObject obj=null;
//        try{
//            obj=new JSONObject();
//            obj.put("action","requestAmbulance");
//            obj.put("latitude",13);
//            obj.put("longitude",12);
//            obj.put("email","a");
//            obj.put("forWhom","self");
//
//        }
//        catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        webSocket.send(obj.toString());
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mMainFrame=(FrameLayout)findViewById(R.id.main_frame);
        mMainNav=(BottomNavigationView)findViewById(R.id.main_nav);

        dashboardFragment = new DashboardFragment();
        medicalFragment=new MedicalFragment();
        contactFragment=new ContactFragment();
        hospitalFragment=new HospitalFragment();
        assistFragment=new AssistFragment();
        client = new OkHttpClient();

        request = new Request.Builder().url("ws://express-man-server-rh.1d35.starter-us-east-1.openshiftapps.com/user").build();
        dashboard.EchoWebSocketListener listener = new dashboard.EchoWebSocketListener();
        webSocket=client.newWebSocket(request,listener);
        client.dispatcher().executorService().shutdown();

//        book();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // fai! we dont have an accelerometer!
        }




        setFragment(dashboardFragment);
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_dashboard:
                    {
                    setFragment(dashboardFragment);

                    return true;
                    }
                    case R.id.nav_assist:
                    {
                        setFragment(assistFragment);
                        return true;
                    }
                    case  R.id.nav_hospital:
                    {
                        setFragment(hospitalFragment);
                        return true;
                    }
                    case R.id.nav_contact:
                    {
                        setFragment(contactFragment);
                        return true;
                    }
                    case R.id.nav_medical:
                    {
                        setFragment(medicalFragment);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });


    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();

    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

