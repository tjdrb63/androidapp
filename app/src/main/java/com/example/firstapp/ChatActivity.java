package com.example.firstapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;

import java.lang.reflect.Array;
import java.util.*;

public class ChatActivity extends Activity {
    private ArrayList<MessageItem> Msgitems =new ArrayList<>(); //정보저장할 리스트
    private Button sendBtn, fab_sub1, fab_sub2,copyBtn,exitBtn;
    private EditText et;
    private ListView listView;
    private DatabaseReference mDatabase;
    private ChatAdapter adapter;
    private String UserId,roomId,roomname,userName;
    private Animation fab_open, fab_close;
    private FloatingActionButton fab_main;
    private boolean isFabOpen = false;
    private Context mContext;
    private ArrayList<String> str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivity_main);

        Intent intent = getIntent(); // intent로 채팅방 아이디 받아옴
        Intent backintent = new Intent(this, listActivity.class);

        ArrayList<roomitem> roomlist2 = ((listActivity)listActivity.context_list).roomitems;
        backintent.putExtra("userId",UserId);
        backintent.putExtra("userName",userName);
        roomId = (String)intent.getSerializableExtra("roomId"); //채팅방 id값 받아옴
        UserId = (String)intent.getSerializableExtra("userId"); //현재 유저 아이디 받아옴
        userName = (String)intent.getSerializableExtra("userName");
        roomname = (String)intent.getSerializableExtra("roomname");
        et = findViewById(R.id.ChatText);           //글쓰는곳
        sendBtn = findViewById(R.id.SendBtn);       //전송버튼

        copyBtn =findViewById(R.id.fabbutton);
        exitBtn=findViewById(R.id.fabbutton2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new ChatAdapter(getApplicationContext(),R.layout.mymsgbox,Msgitems,UserId);

        listView = findViewById(R.id.chatList);
        listView.setAdapter(adapter);  //list에 어뎁터 넣기 MshItems에 따라 바뀜
        TextView nameview = findViewById(R.id.nametext);
        nameview.setText(roomname); // 방제목 지정

        //copyBtn
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("CODE", roomId); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "코드가 복사되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        str =new ArrayList<>();
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "채팅방에서 나갔습니다.", Toast.LENGTH_SHORT).show();
                mDatabase.child("User").child("name").child(UserId).child("list").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(Task<DataSnapshot> task) {
                        DataSnapshot lstack = task.getResult();
                        for(DataSnapshot s : lstack.getChildren()) {
                                String si2 = s.getValue(String.class);
                                if(!si2.equals(roomId)) {
                                    str.add(si2);
                                }
                        }
                        mDatabase.child("User").child("name").child(UserId).child("list").setValue(str);
                    }
                });
                finish();
            }
        });


        //fab
        mContext=getApplicationContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main=(FloatingActionButton)findViewById(R.id.actfab_main);
        fab_sub1 = findViewById(R.id.fabbutton);
        fab_sub2 = findViewById(R.id.fabbutton2);

        //fab 버튼 활성화
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    //fab_main.setImageResource(R.drawable.ic_add);
                    fab_sub1.startAnimation(fab_close);
                    fab_sub2.startAnimation(fab_close);
                    fab_sub1.setClickable(false);
                    fab_sub2.setClickable(false);
                    isFabOpen = false;

                } else {
                    //  fab_main.setImageResource(R.drawable.ic_close);
                    fab_sub1.startAnimation(fab_open);
                    fab_sub2.startAnimation(fab_open);
                    fab_sub1.setClickable(true);
                    fab_sub2.setClickable(true);
                    isFabOpen = true;
                }
            }
        });

        //DB에서 채팅내역 불러오기
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
                    MessageItem checkMsg = new MessageItem(User_Name, Msg, time, Profile,userName);
                    mDatabase.child("room").child("chat").child(roomId).push().setValue(checkMsg);
                }
                et.setText("");
            }

         });


        }

}