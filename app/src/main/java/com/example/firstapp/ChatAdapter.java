package com.example.firstapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MessageItem> Msgitems;
    private int layout;
    private LayoutInflater inflater; // 레이아웃 ID 를 뷰로 바꾸는거
    private String id;

        public ChatAdapter(Context context,int layout,ArrayList<MessageItem> Msgitems,String id){
            this.context=context;
            this.layout=layout;
            this.Msgitems=Msgitems;
            this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // 보이는 뷰 추출
            this.id=id;
        }


    @Override
    public int getCount() {
        return  Msgitems.size();
    }

    @Override
    public Object getItem(int position) {
        return Msgitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

         MessageItem item = Msgitems.get(position); // 현재 보여줄 번째의 데이터로 뷰 생성


        // 여기에 메세지 주인 확인 여부 필요
        if(view == null){
            view = inflater.inflate(layout,viewGroup,false); //처음그릴떄만 inflate
        }
        if(id.equals(item.getName())){
            view = inflater.inflate(layout,viewGroup,false); 
            // 현재 ID 의 주인과 일치하는 대화내용일경우 오른쪽 레이아웃 생성
        }
        else
        {
            view = inflater.inflate(R.layout.othermsgbox,viewGroup,false);
            // 현재 ID의 주인과 일치하지않으면 왼쪽 레이아웃생성
        }   


        CircleImageView iv = view.findViewById(R.id.iv);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        TextView tvTime = view.findViewById(R.id.tv_time);

       // iv.setImageResource(0);
        tvName.setText(item.getuserId());
        tvMsg.setText(item.getMessage());
        tvTime.setText(item.getTime());


        return view;
    }
}
