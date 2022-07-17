package com.example.SmartStudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class user extends AppCompatActivity {
    SwitchCompat switch_in, switch_auto, switch_connect;
    static MqttAndroidClient client;
    Button cont;
    boolean hiveConnect = false;
    boolean userIn = false;

    static String mqtt_host = "tcp://broker.mqttdashboard.com:1883";
    String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user);

        switch_in = findViewById(R.id.switch_in);
        switch_auto = findViewById(R.id.switch_auto);
        switch_connect = findViewById(R.id.switch_connect);

        cont = findViewById(R.id.btn_cont);

        switch_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    Connect();
                } else {
                    Disconnect();
                }
            }
        });

        switch_in.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String pubTopic7 = "userActive";
                    if (compoundButton.isChecked()) {
                        String payload = "in";
                        byte[] encodedPayload = new byte[0];
                        try {
                            encodedPayload = payload.getBytes("UTF-8");
                            MqttMessage message = new MqttMessage(encodedPayload);
                            client.publish(pubTopic7, message);
                        } catch (UnsupportedEncodingException | MqttException e) {
                            e.printStackTrace();
                        }
                        userIn = true;
                        Toast.makeText(user.this, "user in!", Toast.LENGTH_SHORT).show();

                    } else {
                        String payload = "out";
                        byte[] encodedPayload = new byte[0];
                        try {
                            encodedPayload = payload.getBytes("UTF-8");
                            MqttMessage message = new MqttMessage(encodedPayload);
                            client.publish(pubTopic7, message);
                        } catch (UnsupportedEncodingException | MqttException e) {
                            e.printStackTrace();
                        }
                        userIn = false;
                        Toast.makeText(user.this, "user out!", Toast.LENGTH_SHORT).show();
                    }
                }

        });

        switch_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        String pubTopic7 = "userControl";
                        if (compoundButton.isChecked()) {
                            String payload = "auto";
                            byte[] encodedPayload = new byte[0];
                            try {
                                encodedPayload = payload.getBytes("UTF-8");
                                MqttMessage message = new MqttMessage(encodedPayload);
                                client.publish(pubTopic7, message);
                            } catch (UnsupportedEncodingException | MqttException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(user.this, "user auto!", Toast.LENGTH_SHORT).show();

                        } else {
                            String payload = "manual";
                            byte[] encodedPayload = new byte[0];
                            try {
                                encodedPayload = payload.getBytes("UTF-8");
                                MqttMessage message = new MqttMessage(encodedPayload);
                                client.publish(pubTopic7, message);
                            } catch (UnsupportedEncodingException | MqttException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(user.this, "user manual!", Toast.LENGTH_SHORT).show();
                        }
                    }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiveConnect && userIn) {
                    Intent homeAct = new Intent(user.this, home.class);
                    startActivity(homeAct);
                    Toast.makeText(user.this, "Connected Successfully!", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(user.this, "Check your connection & settings!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void Connect() {

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), mqtt_host, clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    hiveConnect = true;
                    Toast.makeText(getApplicationContext(), "connected!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    hiveConnect = false;
                    Toast.makeText(getApplicationContext(), "connected failed!", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void Disconnect() {
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // we are now successfully disconnected
                        hiveConnect = false;
                    Toast.makeText(user.this,"Disconnected!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                        hiveConnect = true;
                    Toast.makeText(user.this,"Not Disconnected!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
//
//                lamp lamp = new lamp();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("ci", clientId);
//                bundle1.putString("mh",mqtt_host);
//
//                lamp.setArguments(bundle1);
//                fragmentTransaction.replace(R.id.frameLayout,lamp).commit();
//
//                fan fan = new fan();
//                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("cii", clientId);
//                bundle.putString("mhh",mqtt_host);
//
//                fan.setArguments(bundle);
//                fragmentTransaction2.replace(R.id.frameLayout,fan).commit();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        btn_connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                clientId = MqttClient.generateClientId();
//                client = new MqttAndroidClient(user.this, mqtt_host,
//                        clientId);
//
//                try {
//                    IMqttToken token = client.connect();
//                    token.setActionCallback(new IMqttActionListener() {
//                        @Override
//                        public void onSuccess(IMqttToken asyncActionToken) {
//                            // We are connected
//                            Log.d(TAG, "onSuccess");
//                            Toast.makeText(user.this,"connected!", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                            // Something went wrong e.g. connection timeout or firewall problems
//                            Log.d(TAG, "onFailure");
//                            Toast.makeText(user.this,"connected failed!", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        btn_disconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    IMqttToken disconToken = client.disconnect();
//                    disconToken.setActionCallback(new IMqttActionListener() {
//                        @Override
//                        public void onSuccess(IMqttToken asyncActionToken) {
//                            // we are now successfully disconnected
//                            Toast.makeText(user.this,"Disconnected!", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onFailure(IMqttToken asyncActionToken,
//                                              Throwable exception) {
//                            // something went wrong, but probably we are disconnected anyway
//                            Toast.makeText(user.this,"Not Disconnected!", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
