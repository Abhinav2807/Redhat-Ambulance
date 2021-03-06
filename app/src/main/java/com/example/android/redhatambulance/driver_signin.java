package com.example.android.redhatambulance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class driver_signin extends AppCompatActivity {

    public OkHttpClient client;

    EditText email;
    EditText password;
    Button login;
    String emailVal;
    String passVal;

    JSONObject userLoginData;

    Request request;
    WebSocket webSocket;

    //To send data to server via web sockets

    void login(){
        emailVal=email.getText().toString();
        passVal=password.getText().toString();
        JSONObject obj=null;
        try{
            obj=new JSONObject();
            obj.put("action", "requestSignin");
            obj.put("email", emailVal);
            obj.put("password", passVal);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.d("ReplyInfo","catch", e);
        }

        webSocket.send(obj.toString());
    }


    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {



        }

        @Override
        public void onMessage(WebSocket webSocket, String text){

            JSONObject obj;
            try{
                obj=new JSONObject(text);
                String action =  obj.getString("action");
                Log.d("Reply Info",action);

                switch (action){
                    case "signinResponse":
                        Log.d("Reply Info","here");
                        if(obj.getString("status").equals("success")){
                            Log.d("Reply Info","here2");
                            UserSingleton.get().setEmail(obj.getString("email"));
//                            UserSingleton.get().setName(obj.getString("details"));
                            Log.d("Reply Info","here3");
                            Intent i= new Intent(driver_signin.this,Driverdashboard.class);
                            webSocket.close(NORMAL_CLOSURE_STATUS,"ghu");
                            startActivity(i);
                            Log.d("Reply Info","here4");

                        } else{
                            Toast.makeText(driver_signin.this,"fail", Toast.LENGTH_SHORT).show();

                        }

                        break;
                    case "someshoit":
                        break;
                    default:
                        Toast.makeText(driver_signin.this,"unknown action", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_driver_signin);
        final TextView signup=(TextView)findViewById(R.id.noDriverAccount);
        login=(Button)findViewById(R.id.driver_signin);
        final Intent i = new Intent(this,signup.class);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(i);

            }
        });

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        client = new OkHttpClient();

        request = new Request.Builder().url("ws://express-man-server-rh.1d35.starter-us-east-1.openshiftapps.com/ambulance").build();
        driver_signin.EchoWebSocketListener listener = new driver_signin.EchoWebSocketListener();
        webSocket=client.newWebSocket(request,listener);
        client.dispatcher().executorService().shutdown();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

//                Toast.makeText(UserSignin.this, "Logging in", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



