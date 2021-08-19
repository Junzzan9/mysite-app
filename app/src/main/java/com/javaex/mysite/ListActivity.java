package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.javaex.vo.GuestbookVo;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView lvGuestbookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //get DATA from SERVER
        List<GuestbookVo> guestbookVoList = guestbookFromServer();
        Log.d("javaStudy", "server ok ");
        Log.d("javaStudy", "onCreate: " + guestbookVoList.toString());


        //ListView 객체화
        lvGuestbookList = (ListView) findViewById(R.id.lvGuestbookList);

        //Adapter 생성
        GuestbookListAdapter guestbookListAdapter = new GuestbookListAdapter(getApplicationContext(), R.id.lvGuestbookList, guestbookVoList);

        //ListView(Adapter)
        lvGuestbookList.setAdapter(guestbookListAdapter);

        lvGuestbookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //현재 클릭한 뷰의 리스트의 index 값
                Log.d("javaStudy", "index: "+i);

                //화면의 text
                TextView txtContent = (TextView)view.findViewById(R.id.txtContent);
                Log.d("javaStudy", "txtContent: "+txtContent.getText().toString());


                //화면에 출력되지않은 데이터터
                GuestbookVo guestbookVo = (GuestbookVo) adapterView.getItemAtPosition(i);
                Log.d("javaStudy", "Vo : "+guestbookVo.toString());
                Log.d("javaStudy", "Vo.date : "+guestbookVo.getRegDate());

                //클릭된 item pk
                int no = guestbookVo.getNo();
                Log.d("javaStudy", "pk : "+guestbookVo.getNo());
            }
        });
    }



    public List<GuestbookVo> guestbookFromServer(){
        List<GuestbookVo> guestbookList = new ArrayList<GuestbookVo>();

        for(int i=1; i<=50;i++){
            GuestbookVo guestbookVo = new GuestbookVo();
            guestbookVo.setNo(i);
            guestbookVo.setName(i+"준식");
            guestbookVo.setRegDate("2021/08/19"+i);
            guestbookVo.setContent("ddddd");

            guestbookList.add(guestbookVo);

        }
        return guestbookList;
    }

}