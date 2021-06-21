package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;


public class RoomCreateAct extends Activity {
        private EditText rname;
        private Button conBtn, canBtn;
        private Intent intent = new Intent();
        private TextView rtext1, rtext2 ;
    public  void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 없애기
            setContentView(R.layout.makeroombox);
            rtext1 = findViewById(R.id.roomtext1);
            rtext2 = findViewById(R.id.roomtext2);
            rname = findViewById(R.id.rnametext);
            conBtn = findViewById(R.id.confirmBtn);
            canBtn = findViewById(R.id.cancleBtn);
            Intent rintent = getIntent();
            String num = (String)rintent.getSerializableExtra("num");

            if(num.equals("1")){
                rtext1.setText("채팅방 이름");
                rtext2.setText("채팅방 만들기");
            }
            else if(num.equals("2")){
                rtext1.setText("채팅방 번호");
                rtext2.setText("채팅방 입장하기");
            }
            conBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //랜덤한 해쉬코드 생성후 String으로 변환
                    Random rd = new Random();
                    int rdint = rd.nextInt(999999999);
                    String Hashcode = String.valueOf(rdint);
                    //Edit text 내용 반환 및 확인버튼 눌렀다고 알림
                    intent.putExtra("Result",rname.getText().toString());
                    intent.putExtra("Hashcode",Hashcode);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            canBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Chat","Dd");
                    setResult(RESULT_CANCELED,intent);
                    finish();
                }
            });


        }
        //바깥레이어 클릭해도 창안닫힘
        public boolean onTouchEvent(MotionEvent event){
            if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
                return false;
            }
            return true;
        }
}
