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

public class signin extends AppCompatActivity {

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

    public void sendData(){

        emailVal=email.getText().toString();
        passVal=password.getText().toString();

        if ( !emailVal.isEmpty() && !passVal.isEmpty() )
        {
            userLoginData = new JSONObject();
            try{

                userLoginData.put("email",emailVal);
                userLoginData.put("password",passVal);

            }
            catch (JSONException e){

                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
        }


    }


    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {

        sendData();
        webSocket.send(userLoginData.toString());

        }

        @Override
        public void onMessage(WebSocket webSocket, String text){

            Log.i("Reply Info",text);
            String reply="success";
            JSONObject obj;
            try{
                obj=new JSONObject(text);

                if(text==reply)
                {
                    UserSingleton.get().setEmail(obj.getString("email"));
                    UserSingleton.get().setName(obj.getString("username"));
                    Intent dashboard= new Intent(signin.this,dashboard.class);
                    startActivity(dashboard);
                }

                else{
                    Toast.makeText(signin.this,text.toString(), Toast.LENGTH_SHORT).show();
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

    private void start()
    {
        request = new Request.Builder().url("ws://express-man-server-rh.1d35.starter-us-east-1.openshiftapps.com/user").build();
        signin.EchoWebSocketListener listener = new signin.EchoWebSocketListener();
        webSocket=client.newWebSocket(request,listener);
        client.dispatcher().executorService().shutdown();


    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final TextView signup=(TextView)findViewById(R.id.noAccount);
        login=(Button)findViewById(R.id.button3);
        final Intent i = new Intent(this,signup.class);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(i);

            }
        });

        email=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        client = new OkHttpClient();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                Toast.makeText(signin.this, "Loging in", Toast.LENGTH_SHORT).show();
            }
        });
    }
}