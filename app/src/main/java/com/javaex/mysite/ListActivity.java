package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javaex.vo.GuestbookVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView lvGuestbookList;
    private Button btnWriteForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //ListView 객체화
        lvGuestbookList = (ListView) findViewById(R.id.lvGuestbookList);
        btnWriteForm = (Button) findViewById(R.id.btnWriteForm);

        btnWriteForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("javaStudy", "onClick: writeForm");

                Intent intent = new Intent(ListActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    //activity resume
    @Override
    protected void onResume() {
        super.onResume();


        //AsyncTask.getDATA
        ListAsyncTask listAsyncTask = new ListAsyncTask();
        listAsyncTask.execute();



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


                Intent intent =new Intent(ListActivity.this,ReadActivity.class);
                intent.putExtra("no",no);
                startActivity(intent);
            }
        });


    }

    public class ListAsyncTask extends AsyncTask<Void, Integer, List<GuestbookVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<GuestbookVo> doInBackground(Void... voids) {
            //서버에 연결

            //요청하기
            List<GuestbookVo> guestbookList = null;
            try {
                URL url = new URL("http://192.168.35.166:8088/mysite5/api/guestbook/list"); //url 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                int resCode = conn.getResponseCode(); // 응답코드 200이 정상
                Log.d("javaStudy", "doInBackground: " + resCode);
                if (resCode == 200) {   //정상이면
                    //Stream 을 통해 통신한다
                    //데이타 형식은 json으로 한다.
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);

                    String jsonData = "";
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        jsonData += line;
                    }
                    Log.d("javaStudy", "doInBackground: " + jsonData);
                    //json --> GuestbookVo
                    Gson gson = new Gson();
                    guestbookList = gson.fromJson(jsonData, new TypeToken<List<GuestbookVo>>() {
                    }.getType());

                    Log.d("javaStudy", "doInBackground: " + guestbookList.size());
                    Log.d("javaStudy", "doInBackground: " + guestbookList.get(0).getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //응답(GuestbookList) 받기 json --> java.object List<GuestBookVo> GuestbookList

            return guestbookList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<GuestbookVo> guestbookList) {
            Log.d("javaStudy", "onPostExecute: " + guestbookList.size());
            Log.d("javaStudy", "onPostExecute: " + guestbookList.get(0).getName());

            //Adapter 생성
            GuestbookListAdapter guestbookListAdapter = new GuestbookListAdapter(getApplicationContext(), R.id.lvGuestbookList, guestbookList);

            //ListView(Adapter)
            lvGuestbookList.setAdapter(guestbookListAdapter);



            super.onPostExecute(guestbookList);
        }
    }

}