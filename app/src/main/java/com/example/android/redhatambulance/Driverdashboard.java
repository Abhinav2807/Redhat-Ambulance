package com.example.android.redhatambulance;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class Driverdashboard extends AppCompatActivity {

    Button profile;
    TextView name;
    TextView sos;
    public OkHttpClient client;
    Request request;
    WebSocket webSocket;
    String latitude;
    String longitude;
    ConstraintLayout constraintLayout;




    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.d("Websocket","open");

            JSONObject obj1=null;
            try{
                obj1=new JSONObject();
                obj1.put("action","requestReady");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            webSocket.send(obj1.toString());

        }

        @Override
        public void onMessage(WebSocket webSocket, String text){

            JSONObject obj;
            try{
                obj=new JSONObject(text);
                String action =  obj.getString("action");
                Log.d("Reply Info",action);

                switch (action){
                    case "requestAmbulance":
                        latitude = obj.getString("latitude");
                        longitude=obj.getString("longitude");
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=12.981944,77.69713&daddr="+latitude+","+longitude+""));
                        startActivity(intent);








                        break;

                    default:
                        Toast.makeText(Driverdashboard.this,"unknown action", Toast.LENGTH_SHORT).show();
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
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//            output("Error : " + t.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverdashboard);

        constraintLayout=(ConstraintLayout)findViewById(R.id.constraintLayout);
        name=(TextView)findViewById(R.id.john);
        sos=(TextView)findViewById(R.id.textView);
        constraintLayout.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        sos.setVisibility(View.VISIBLE);

        client = new OkHttpClient();

        request = new Request.Builder().url("ws://express-man-server-rh.1d35.starter-us-east-1.openshiftapps.com/ambulance").build();
        Driverdashboard.EchoWebSocketListener listener = new Driverdashboard.EchoWebSocketListener();
        webSocket=client.newWebSocket(request,listener);
        client.dispatcher().executorService().shutdown();

        profile=(Button)findViewById(R.id.button6);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Driverdashboard.this,profileDriver.class);
                startActivity(i);
            }
        });
    }
}
