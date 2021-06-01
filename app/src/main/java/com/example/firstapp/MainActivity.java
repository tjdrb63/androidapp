package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MessageItem> Msgitems =new ArrayList<>(); //정보저장할 리스트
    private Button sendBtn;
    private EditText et;
    private ListView listView;
    private DatabaseReference mDatabase;
    private ChatAdapter adapter;
    private String UserId ;
    private String roomId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); // intent로 채팅방 아이디 받아옴
        roomId = (String)intent.getSerializableExtra("roomId"); //채팅방 id값 받아옴
        UserId = (String)intent.getSerializableExtra("userId"); //현재 유저 아이디 받아옴

        et = findViewById(R.id.ChatText);           //글쓰는곳
        sendBtn = findViewById(R.id.SendBtn);       //전송버튼
        
        mDatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new ChatAdapter(getApplicationContext(),R.layout.mymsgbox,Msgitems,UserId);

        listView = findViewById(R.id.chatList);
        listView.setAdapter(adapter);  //list에 어뎁터 넣기 MshItems에 따라 바뀜

        getSupportActionBar().setTitle(roomId); //제목



        mDatabase.child("room").child("chat").child(roomId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //최초실행시 데이터베이스의 채팅창내용을 전부 가져옴 새로추가된내용은 반영 X
                // Log.d("Chat",snapshot.getValue().toString());
                Log.d("Chat",snapshot.toString());
                MessageItem msgit = snapshot.getValue(MessageItem.class);
                Msgitems.add(msgit);

                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //채팅 치면 실시간으로 변화함
                Msgitems.clear();
               // Log.d("Chat",snapshot.getValue().toString());
                MessageItem msgit = snapshot.getValue(MessageItem.class);
                Msgitems.add(msgit);

                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(!et.getText().toString().equals("")) {

                    String User_Name = UserId;       //사용자 이름
                    String Msg = et.getText().toString();   // 메세지 내용
                    Calendar cal = Calendar.getInstance(); // 현재 시간
                    String time = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE); // 시간:분
                    String Profile = "@mipmap/ic_launcher";  // 파일링크 임의
                    MessageItem checkMsg = new MessageItem(User_Name, Msg, time, Profile);
                    mDatabase.child("room").child("chat").child(roomId).push().setValue(checkMsg);
                }

                et.setText("");


            }

         });


        }

}