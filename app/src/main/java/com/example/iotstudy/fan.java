package com.example.iotstudy;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;



public class fan extends Fragment {

    //CircleSeekBar circleSeekBar;
    View v;
    int r, g, b, pixel;
    SeekBar speedBar;
    String powerFan, speedFan, speed_val, cii,mhh;
    TextView speed, roomTemp, roomHumid;
    SwitchCompat power_switch, rot_switch;

   // MqttAndroidClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fan, container, false);

        roomTemp = v.findViewById(R.id.room_temp);
        roomHumid = v.findViewById(R.id.room_humid);

        power_switch = v.findViewById(R.id.switch_fan);
        speed = v.findViewById(R.id.txt_speed);

        speedBar = v.findViewById(R.id.seek_speed);

//        Bundle bundle1 = getArguments();
//        if (bundle1 != null) {
//            cii = bundle1.getString("cii");
//            mhh = bundle1.getString("mhh");
        //}
       // client = new MqttAndroidClient(fan.this.getContext(), mhh,cii);
        //======================================================================================================================

        power_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String pubTopic5 = "powerFan";
                if (compoundButton.isChecked()) {
                    String payload = "on";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        user.client.publish(pubTopic5, message);
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(fan.this.getContext(), "fan on!", Toast.LENGTH_SHORT).show();

                } else {
                    String payload = "off";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        user.client.publish(pubTopic5, message);
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(fan.this.getContext(), "fan off!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // circleSeekBar.setSeekBarChangeListener(this);
        // circleSeekBar.setProgressDisplayAndInvalidate(13);

        //circleSeekBar.setProgressDisplayAndInvalidate(Integer.parseInt(rot.getText().toString()));


        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String pubTopic6 = "speedFan";
                speed.setText(String.valueOf(i));
                speed_val = String.valueOf(i);
                String payload = speed_val;
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    user.client.publish(pubTopic6, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }

                Toast.makeText(fan.this.getContext(), i + " " +"was set",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //=====================================================================================================================

        String topic = "temperatureV";
        String topic2 = "humidityV";


        int qos = 1;
        int qosh = 0;

        try {
            user.client.subscribe(topic, qos);
            user.client.subscribe(topic2, qosh);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        user.client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = new String(message.getPayload());
                Log.d(TAG, "**************msglength*******************" + msg.length());

                if(msg.length() ==7){
                    roomTemp.setText(msg);
                    Log.d(TAG, "**************7777777777*******************" + msg);
                }else if(msg.length() ==5){
                    roomHumid.setText(msg);
                    Log.d(TAG, "**************5555555555*******************" + msg);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


        //==================================================================================================================

        return v;
    }
}





//    @Override
//    public void onPointsChanged(CircleSeekBar circleSeekBar, int points, boolean fromUser) {
//        //circleSeek.setText(String.valueOf(points));
//        rot_val = String.valueOf(points);
//    }
//
//    @Override
//    public void onStartTrackingTouch(CircleSeekBar circleSeekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(CircleSeekBar circleSeekBar) {
//
//    }


//        setRotate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String payload = rot_val;
//                byte[] encodedPayload = new byte[0];
//                try {
//                    encodedPayload = payload.getBytes("UTF-8");
//                    MqttMessage message = new MqttMessage(encodedPayload);
//                    client.publish(angleTopic, message);
//                } catch (UnsupportedEncodingException | MqttException e) {
//                    e.printStackTrace();
//                }
//
//                Toast.makeText(fan.this.getContext(), rot_val + "was the angle",Toast.LENGTH_SHORT).show();
//            }
//        });

//        rot_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(compoundButton.isChecked()){
//                    String payload = "on";
//                    byte[] encodedPayload = new byte[0];
//                    try {
//                        encodedPayload = payload.getBytes("UTF-8");
//                        MqttMessage message = new MqttMessage(encodedPayload);
//                        client.publish(rotateTopic, message);
//                    } catch (UnsupportedEncodingException | MqttException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(fan.this.getContext(),"rotation on!", Toast.LENGTH_SHORT).show();
//                }else{
//                    String payload = "off";
//                    byte[] encodedPayload = new byte[0];
//                    try {
//                        encodedPayload = payload.getBytes("UTF-8");
//                        MqttMessage message = new MqttMessage(encodedPayload);
//                        client.publish(rotateTopic, message);
//                    } catch (UnsupportedEncodingException | MqttException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(fan.this.getContext(),   "rotation off!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
