package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javaex.vo.GuestbookVo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ReadActivity extends AppCompatActivity {


    private TextView readNo;
    private TextView readName;
    private TextView readDate;
    private TextView readContent;
    private Button btnGoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        readNo = (TextView)findViewById(R.id.readNo);
        readName = (TextView)findViewById(R.id.readName);
        readDate = (TextView)findViewById(R.id.readDate);
        readContent = (TextView)findViewById(R.id.readContent);
        btnGoList = (Button)findViewById(R.id.btnGoList);


        Intent intent = getIntent();

        int no = intent.getExtras().getInt("no");
        Log.d("javaStudy", "intent get: "+no);

        ReadAsyncTask readAsyncTask = new ReadAsyncTask();
        readAsyncTask.execute(no);

        btnGoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ReadActivity.this,ListActivity.class);
                startActivity(intent1);
                finish();

            }
        });

    }

    public class ReadAsyncTask extends AsyncTask<Integer,Integer,GuestbookVo>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GuestbookVo doInBackground(Integer... noArray) {
            Log.d("javaStudy", "doInBackground: ");
            Log.d("javaStudy", "no: "+noArray[0]);

            //gson 객체생성
            Gson gson = new Gson();

            GuestbookVo requestVo = null;
            GuestbookVo responseVo = new GuestbookVo();


            int no = noArray[0];

            //요청준비
            requestVo = new GuestbookVo();
            requestVo.setNo(no);


            String requestJson = gson.toJson(requestVo);
            Log.d("javaStudy", "requestJson: " + requestJson);



            try {
                URL url = new URL("http://192.168.35.166:8088/mysite5/api/guestbook/read"); //url 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                //REQUEST 준비
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                bw.write(requestJson);
                bw.flush();


                int resCode = conn.getResponseCode(); // 응답코드 200이 정상
                Log.d("javaStudy", "resCode: " + resCode);


                if(conn.getResponseCode()==200){

                //RESPONSE 준비
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                    String responseJson = "";
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        responseJson += line;
                    }

                    //json 확인
                    Log.d("javaStudy", "jsonData: " +responseJson);

                    //json-->guestbookVo
                    responseVo = gson.fromJson(responseJson,GuestbookVo.class);
                    Log.d("javaStudy", "responseVo: " +responseVo);



                }


            } catch (IOException e) {
                e.printStackTrace();
            }




            return responseVo;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(GuestbookVo guestbookVo) {
            super.onPostExecute(guestbookVo);

            readNo.setText(""+guestbookVo.getNo());
            readName.setText(guestbookVo.getName());
            readDate.setText(guestbookVo.getRegDate());
            readContent.setText(guestbookVo.getContent());
        }
    }

}