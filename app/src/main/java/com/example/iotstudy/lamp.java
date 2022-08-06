package com.example.SmartStudy;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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


public class lamp extends Fragment {
    View mColorPreview;
    View v;
    Button set_col;
    ImageView imageView;
    Bitmap bitmap;
    int r,g,b,pixel;
    String  rgbColor, hex,pickColor,ci,mh ;
    SeekBar brightBar;
    TextView bright, ldrValue;
    SwitchCompat switch_lamp, switch_led;

   // MqttAndroidClient client;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_lamp, container, false);

        brightBar = v.findViewById(R.id.seek_bright);
        ldrValue = v.findViewById(R.id.ldr_val);
        bright = v.findViewById(R.id.txt_bright);
        switch_lamp = v.findViewById(R.id.switch_lamp);
        switch_led = v.findViewById(R.id.switch_led);

        mColorPreview = v.findViewById(R.id.preview_selected_color);
        set_col = v.findViewById(R.id.btn_set_color);
        imageView = v.findViewById(R.id.colorPicker);

//        Bundle bundle1 = getArguments();
//        if (bundle1 != null) {
//            ci = bundle1.getString("ci");
//            mh = bundle1.getString("mh");
//        }

       // client = new MqttAndroidClient(lamp.this.getContext(), mh,ci);


        switch_lamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String pubTopic1 = "powerLamp";
                if(compoundButton.isChecked()){
                    String payload = "on";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        user.client.publish(pubTopic1, message);
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(lamp.this.getContext(),"Lamp on!", Toast.LENGTH_SHORT).show();

                }else {
                    String payload = "off";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        user.client.publish(pubTopic1, message);
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(lamp.this.getContext(),   "lamp off!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switch_led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String pubTopic3 = "powerRGB";
                if(compoundButton.isChecked()){
                    String payload = "on";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        user.client.publish(pubTopic3, message);
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(lamp.this.getContext(),"LED on!", Toast.LENGTH_SHORT).show();

                }else {
                    String payload = "off";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        user.client.publish(pubTopic3, message);
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(lamp.this.getContext(),   "led off!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        brightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String pubTopic2 = "brightLamp";
                bright.setText(String.valueOf(i));
                String payload = String.valueOf(i);
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    user.client.publish(pubTopic2, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }

                Toast.makeText(lamp.this.getContext(), i + " " +"was set",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache(true);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap = imageView.getDrawingCache();
                    pixel = bitmap.getPixel((int)motionEvent.getX(), (int)motionEvent.getY());
                    r = Color.red(pixel);
                    g = Color.green(pixel);
                    b = Color.blue(pixel);

                    //String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
                    //r201g32b255&
                    rgbColor = "r"+r+"g"+g+"b"+b+"&";
                   // hex = "#"+Integer.toHexString(pixel).substring(2);
                    mColorPreview.setBackgroundColor(Color.rgb(r,g,b));
                }
                return true;
            }
        });

        set_col.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pubTopic4 = "colorRGB";
                        // now change the value of the GFG text
                        // as well.
                        pickColor = r +","+ g +","+ b;
                        String payload = rgbColor;
                        byte[] encodedPayload = new byte[0];
                        try {
                            encodedPayload = payload.getBytes("UTF-8");
                            MqttMessage message = new MqttMessage(encodedPayload);
                            user.client.publish(pubTopic4, message);
                        } catch (UnsupportedEncodingException | MqttException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(lamp.this.getContext(), "RGB:"+" "+pickColor,Toast.LENGTH_SHORT).show();
                    }
                });

        String topic3 = "intensityV";
        int qosk = 0;
        try {
            user.client.subscribe(topic3, qosk);
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

                if (msg.length() == 2) {
                    ldrValue.setText(msg);
                    Log.d(TAG, "**************222222222*******************" + msg);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        return v;
    }

}







