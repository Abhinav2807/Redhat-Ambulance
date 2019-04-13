package com.example.android.redhatambulance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class signup extends AppCompatActivity {

    public OkHttpClient client;
    EditText name;
    EditText email;
    EditText number;
    EditText address;
    EditText password;
    Button signup;

    WebSocket webSocket;
    Request request;

    void signup(){

        String nameVal=name.getText().toString();
        String emailVAl=email.getText().toString();
        String numberVal=number.getText().toString();
        String addressVal=address.getText().toString();
        String passwordVal=password.getText().toString();
        JSONObject obj=null;
        try {
            obj=new JSONObject();
            obj.put("action","signup");
            obj.put("email",emailVAl);
            obj.put("name",nameVal);
            obj.put("password",passwordVal);
            obj.put("number",numberVal);
            obj.put("address",addressVal);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
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
                switch (action)
                {
                    case "signupResponse":
                        Log.d("Reply Info","here");
                        if(obj.getString("status").equals("success"))
                        {
                            Log.d("Reply Info","here2");
                            Intent i= new Intent(signup.this,dashboard.class);
                            webSocket.close(22,"Bye");
                            startActivity(i);
                        }
                }

            }

            catch (JSONException e) {
                e.printStackTrace();
            }

        }








    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name=(EditText)findViewById(R.id.editText3);
        email=(EditText)findViewById(R.id.editText5);
        number=(EditText)findViewById(R.id.editText6);
        address=(EditText)findViewById(R.id.editText7);
        password=(EditText)findViewById(R.id.editText4);
        signup=(Button)findViewById(R.id.button4);
        client = new OkHttpClient();

        request = new Request.Builder().url("ws://express-man-server-rh.1d35.starter-us-east-1.openshiftapps.com/user").build();
        signup.EchoWebSocketListener listener = new signup.EchoWebSocketListener();
        webSocket=client.newWebSocket(request,listener);
        client.dispatcher().executorService().shutdown();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });






    }
}
