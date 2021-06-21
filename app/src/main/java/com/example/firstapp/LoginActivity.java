package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    final static String TAG = "loginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        // et_name = findViewById(R.id.et_name);
        // et_number = findViewById(R.id.et_number);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG,"click");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class );
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"click");
                String userID = et_id.getText().toString();
                String userPassword = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );
                            Log.i(TAG,"click");

                            if(success) {//로그인 성공시
                                Log.i(TAG,"click");
                                String userID = jsonObject.getString( "userID" );
                                String userPassword = jsonObject.getString( "userPassword" );
                                String userName = jsonObject.getString( "userName" );
                                // String userNumber = jsonObject.getString("userNumber");

                                Toast.makeText( getApplicationContext(), String.format("%s님 환영합니다.", userName), Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( LoginActivity.this, MainActivity.class );

                                intent.putExtra( "userID", userID );
                                intent.putExtra( "userPassword", userPassword );
                                intent.putExtra( "userName", userName );
                                // intent.putExtra( "userNumber", userNumber );

                                startActivity( intent );

                            } else {//로그인 실패시
                                Log.i(TAG,"click");
                                Toast.makeText(getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}