package com.example.firstapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_number, et_pass2;
    private Button btn_register, btn_idCheck;
    private boolean validate = false;
    private AlertDialog dialog;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_pass2 = findViewById(R.id.et_pass2);
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);

        btn_register = findViewById(R.id.btn_register);
        btn_idCheck = findViewById(R.id.btn_idCheck);

        //아이디 중복체크
        btn_idCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click");
                String userID = et_id.getText().toString();
                if(validate){
                    return; // 중복 체크 완료
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i(TAG, "json파싱 시작: " + response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                et_id.setEnabled(false); //이ㅇㅇ
                                validate = true; // 검증완료
                                btn_idCheck.setBackgroundColor(getResources().getColor(R.color.white));

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이미 존재하는 아이디가 있습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                Log.i(TAG, "success");
                queue.add(validateRequest);


            }
        });

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userID = et_id.getText().toString();
                String userPassword = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                int userNumber = Integer.parseInt(et_number.getText().toString());
                //Integer userNumberCheck = (Integer) userNumber;

                String PassCk = et_pass2.getText().toString();


                //아이디 중복체크 했는지 확인
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("중복된 아이디가 있는지 확인하세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                //한 칸이라도 입력 안했을 경우
                if (userID.equals("") || userPassword.equals("") || userName.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(userPassword.equals(PassCk)) { // 회원가입 성공
                                if (success) {
                                    Toast.makeText(getApplicationContext(), String.format("%s님 가입을 환영합니다.", userName), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                } else { // 회원가입 실패
                                    Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("비밀번호가 일치하지 않습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                // 서버로 Volley를 이용해서 요청을 함
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userNumber, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);


            }
        });

    }
}