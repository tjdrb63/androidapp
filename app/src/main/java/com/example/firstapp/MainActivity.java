package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends Activity {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent mainintent = getIntent();

     //   button1 = findViewById(R.id.button);
      //  button2 = findViewById(R.id.button2);
      //  button3 = findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);

        String userName = (String)mainintent.getSerializableExtra("userName");
        String userId = (String)mainintent.getSerializableExtra("userID");
        Log.d("Chat",userId);

        Intent intent = new Intent(this, listActivity.class);
        intent.putExtra("userName",userName);
        intent.putExtra("userId",userId);

        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });



    }
}