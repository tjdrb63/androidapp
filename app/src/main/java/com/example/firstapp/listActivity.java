package com.example.firstapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class listActivity extends Activity {

    public static Context context_list;
    private ListView roomlist ;
    public ArrayList<roomitem> roomitems = new ArrayList<roomitem>();
    private DatabaseReference mDatabase;
    private ArrayList<String> roomIds =new ArrayList<>();
    private String userId ,userName;
    private ArrayList<String> listitem =new ArrayList<>();
    private Button CreateBtn,RoominsBtn;
    private listAdapter adapter;
    private int ch = 0;
    private Animation fab_open, fab_close;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private boolean isFabOpen = false;
    private Context mContext;

    private Button button1;


    //시작할떄 채팅방 리스트 불러옴
    public  void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 없애기
        setContentView(R.layout.roomlist);

        context_list=this;
        button1 = findViewById(R.id.button);
            Intent loginintent = getIntent();
            Intent intentMain = new Intent(this, MainActivity.class );
            Intent intent = new Intent(this, ChatActivity.class);
            Intent roomintent = new Intent(this,RoomCreateAct.class);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            adapter = new listAdapter(getApplicationContext(),R.layout.roombox,roomitems);
            roomlist = findViewById(R.id.lv_room);
            roomlist.setAdapter(adapter);
            Calendar cal = Calendar.getInstance();
            String time = cal.get(Calendar.HOUR_OF_DAY) + ":"+ cal.get(Calendar.MINUTE);

            userId = (String)loginintent.getSerializableExtra("userId");
            userName = (String)loginintent.getSerializableExtra("userName");

            Log.d("Chat list", userId+ userName);
            //fab
        mContext=getApplicationContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main=(FloatingActionButton)findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) findViewById(R.id.fab_sub2);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(intentMain);
            }
        });
        //fab 띄우기
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

        //채팅방만들기 버튼
        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomintent.putExtra("num","1");
                startActivityForResult(roomintent,1);
            }
        });

        //채팅방 들어가기 버튼
        fab_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomintent.putExtra("num","2");
                startActivityForResult(roomintent,2);
            }
        });

        //채팅방 누를시 방이동
        roomlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    roomitem rt = roomitems.get(position);
                    intent.putExtra("roomId",rt.getRoomId()); //누른 방번호 보냄
                    intent.putExtra("roomname",rt.getRoomName());
                    intent.putExtra("userId",userId); // 현재 유저 Id 보냄
                    intent.putExtra("userName",userName);
                    startActivity(intent);
                    Log.d("Chat",rt.getRoomId());

                }
            });

        //채팅방 갯수밑 내용불러오기
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot,String s) {
                for (DataSnapshot child : snapshot.child("name").child(userId).child("list").getChildren()) {
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


                listitem.clear();
                for (DataSnapshot child : snapshot.child("name").child(userId).child("list").getChildren()) {
                   int cnt=0;
                    listitem.add(child.getValue(String.class)); //해당유저의// 채팅방 목록 list에 넣기
                    for(roomitem ch1 : roomitems) {
                        if(ch1.getRoomId().equals(child.getValue((String.class)))){
                            Log.d("Chat delete", ch1.getRoomId());
                            roomitems.remove(cnt);
                        }
                    }
                }

                for (String item : listitem) { //채팅방 이름 받아옴  //마지막 대화내용 구현해야됨
                    String rname = snapshot.child(item).getValue(String.class);
                    String lastitem = snapshot.child(item).toString();
                    if(rname != null){
                        roomitems.add(new roomitem(item,rname));
                        Log.d("Chat",lastitem);

                    }
                }
                Log.d("Chat",listitem.toString());
                adapter.notifyDataSetChanged();
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



        if(resultCode == RESULT_OK ) {
            if (Resultdata.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("알림").setMessage("공백은 지정할수 없습니다.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                if (requestCode == 1) { //createBtn 일 경우
                    //방번호 만들기
                    ch = 1;
                    mDatabase.child("room")
                            .child(String.valueOf(hsCode))
                            .setValue(Resultdata); //방이름으로 생성
                    lArr.add(String.valueOf(hsCode)); // 생성된 hscode user-list에 넣음
                } else if (requestCode == 2) {
                    Log.d("Chat", "check1");
                    //방번호 있는지 체크
                    mDatabase.child("room").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(Task<DataSnapshot> task) {
                            ch = 0;
                            DataSnapshot lstack = task.getResult();
                            for (DataSnapshot s : lstack.getChildren()) {
                                if (Resultdata.equals(s.getKey())) {
                                    ch = 1;
                                    String Hname = s.getValue().toString(); //방 이름가져오기
                                    Log.d("Chat String", Hname);
                                    lArr.add(Resultdata); //여기선 결과값이 방번호
                                    roomitems.add(new roomitem(Resultdata, Hname));
                                    Log.d("Chat good", Hname + "   " + Resultdata);
                                }
                            }
                            if (ch == 0) {
                                // list 순회했는데 없으면
                                Toast.makeText(getApplicationContext(), "존재하지 않는 방번호입니다", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }


                Log.d("Chat", "check1");
                // 현재 유저의 list를 받아와서 새로운 방번호 삽입후 다시 넣는 기능

                mDatabase.child("User").child("name").child(userId).child("list")
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
                                    mDatabase.child("User").child("name").child(userId).child("list").setValue(lArr);
                                }
                                if (requestCode == 1)
                                    roomitems.add(new roomitem(hsCode, Resultdata));
                                Collections.reverse(roomitems);
                                //else roomitems.add(new roomitem(Resultdata,Hname));
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        }

    }

}
