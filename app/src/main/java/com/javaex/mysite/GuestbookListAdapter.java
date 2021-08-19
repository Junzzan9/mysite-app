package com.javaex.mysite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.javaex.vo.GuestbookVo;

import java.util.List;

public class GuestbookListAdapter extends ArrayAdapter<GuestbookVo> {

    private TextView txtNo;
    private TextView txtName;
    private TextView txtRegDate;
    private TextView txtContent;


    //constructor
    public GuestbookListAdapter(Context context, int resource, List<GuestbookVo> items) {
        super(context, resource, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("javaStudy", "position: "+position);
        View view = convertView;


        if (view == null) {// if 만들어놓은 view x --> 만들기

            //새로 틀 만들기기
            LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(getContext());

            //틀로 리스트제작작
            view = layoutInflater.inflate(R.layout.activity_list_item, null);
            Log.d("javaStudy", "틀  새로만듬: ");
        }

        //1개 xml 데이터 매칭
        txtNo = view.findViewById(R.id.txtNo);
        txtName = view.findViewById(R.id.txtName);
        txtRegDate = view.findViewById(R.id.txtRegDate);
        txtContent = view.findViewById(R.id.txtContent);

        //데이터 가져오기(1개데이터) super에 전체데이터가있다.
        GuestbookVo guestbookVo = super.getItem(position);
        txtNo.setText("" + guestbookVo.getNo());
        txtName.setText(guestbookVo.getName());
        txtRegDate.setText(guestbookVo.getRegDate());
        txtContent.setText(guestbookVo.getContent());

        return view;
    }


}
