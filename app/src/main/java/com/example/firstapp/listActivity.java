package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.security.identity.ResultData;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class listActivity extends Activity {
    private ListView roomlist ;
    private ArrayList<roomitem> roomitems = new ArrayList<roomitem>();
    private DatabaseReference mDatabase;
    private ArrayList<String> roomIds =new ArrayList<>();
    private String UserId ="장성규";
    private ArrayList<String> listitem =new ArrayList<>();
    private Button CreateBtn,RoominsBtn;
    private listAdapter adapter;
    private int ch = 0;
    
    //시작할떄 채팅방 리스트 불러옴
    public  void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 없애기
            setContentView(R.layout.roomlist);

            Intent intent = new Intent(this,MainActivity.class);
            Intent roomintent = new Intent(this,RoomCreateAct.class);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            adapter = new listAdapter(getApplicationContext(),R.layout.roombox,roomitems);
            CreateBtn = findViewById(R.id.createBtn);
            RoominsBtn = findViewById(R.id.roominBtn);
            roomlist = findViewById(R.id.lv_room);
            roomlist.setAdapter(adapter);
            Calendar cal = Calendar.getInstance();
            String time = cal.get(Calendar.HOUR_OF_DAY) + ":"+ cal.get(Calendar.MINUTE);




        //채팅방만들기 버튼
        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(roomintent,1);
            }
        });

        //채팅방 들어가기 버튼
        RoominsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(roomintent,2);
            }
        });

        //채팅방 누를시 방이동
        roomlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    roomitem rt = roomitems.get(position);

                    intent.putExtra("roomId",rt.getRoomId()); //누른 방번호 보냄
                    intent.putExtra("userId",UserId); // 현재 유저 Id 보냄
                    startActivity(intent);
                 //   Log.d("Chat",rt.getRoomId());
                }
            });

        //채팅방 갯수밑 내용불러오기
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot,String s) {
                for (DataSnapshot child : snapshot.child("name").child(UserId).child("list").getChildren()) {
                    listitem.add(child.getValue(String.class)); //해당유저의 채팅방 목록 list에 넣기
                }
                for (String item : listitem) { //채팅방 이름 받아옴  //마지막 대화내용 구현해야됨
                    String rname = snapshot.child(item).getValue(String.class);
                      String lastitem = snapshot.child(item).toString();
                      if(rname != null){
                          roomitems.add(new roomitem(item,rname));
                          Log.d("Chat",lastitem);
                      }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });



    }

    //채팅방 만들기/들어가기를 무사히했다면
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
            super.onActivityResult(requestCode,resultCode,data);
            String Resultdata = data.getStringExtra("Result");
            String hsCode = data.getStringExtra("Hashcode");
            ArrayList<String> lArr= new ArrayList<>();

            if(resultCode == RESULT_OK){
                if(requestCode == 1){ //createBtn 일 경우
                    //방번호 만들기
                    ch=1;
                    mDatabase.child("room")
                            .child(String.valueOf(hsCode))
                            .setValue(Resultdata); //방이름으로 생성

                    lArr.add(String.valueOf(hsCode)); // 생성된 hscode user-list에 넣음

                }
                else if(requestCode==2){
                    //방번호 있는지 체크
                    mDatabase.child("room").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(Task<DataSnapshot> task) {
                            ch=0;
                            DataSnapshot lstack = task.getResult();
                            for(DataSnapshot s : lstack.getChildren()){
                                if(Resultdata.equals(s.getKey()))
                                {
                                    ch=1;
                                    String Hname = s.getValue().toString(); //방 이름가져오기
                                    Log.d("Chat String", Hname);
                                    lArr.add(Resultdata); //여기선 결과값이 방번호
                                    roomitems.add(new roomitem(Resultdata,Hname));
                                    Log.d("Chat good",Hname+"   "+Resultdata);
                                }
                            }
                            if(ch==0){
                                // list 순회했는데 없으면
                                Log.d("Chat","없는방 번호 입니다.");
                            }
                        }
                    });
                }
                // 현재 유저의 list를 받아와서 새로운 방번호 삽입후 다시 넣는 기능


                    mDatabase.child("User").child("name").child(UserId).child("list")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("Chat", "오류발생");
                                } else {
                                    DataSnapshot lstack = task.getResult(); //task 결과값 snapshot으로 저장
                                    for (DataSnapshot s : lstack.getChildren()) // room1~room_end
                                    {
                                        lArr.add(s.getValue().toString());
                                    }
                                    //list 새롭게 넣기
                                    mDatabase.child("User").child("name").child(UserId).child("list").setValue(lArr);
                                }
                                if(requestCode == 1) roomitems.add(new roomitem(hsCode, Resultdata));
                                Collections.reverse(roomitems);
                                //else roomitems.add(new roomitem(Resultdata,Hname));
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
    }
}
