package com.example.firstapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class listAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<roomitem> roomitems;

    public listAdapter(Context context, int layout, ArrayList<roomitem> roomitems){
        this.context= context;
        this.layout=layout;
        this.roomitems= roomitems;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public listAdapter(){}

    @Override
    public int getCount() {
        return roomitems.size();
    }

    @Override
    public Object getItem(int position) {
        return roomitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
            roomitem item = roomitems.get(position);
            if(view == null) {
                view = inflater.inflate(layout,viewGroup,false);
            }

            TextView RoomTitle = view.findViewById(R.id.RoomTitle);
            TextView RoomTitle2= view.findViewById(R.id.roomTitle2);
        RoomTitle.setText(item.getRoomName());
        RoomTitle2.setText(item.getRoomId());
        return view;
    }
}
