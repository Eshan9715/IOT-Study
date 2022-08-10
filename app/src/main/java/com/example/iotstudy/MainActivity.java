package com.example.iotstudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Button btn_regis_one =(Button)findViewById(R.id.btn_continue);

        btn_regis_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSecondActivity =new Intent(MainActivity.this, user.class);
                startActivity(openSecondActivity);


            }
        });



    }
}