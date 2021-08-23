package com.javaex.mysite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.javaex.vo.GuestbookVo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    //field
    private Button btnWrite;
    private EditText edtName;
    private EditText edtPassword;
    private EditText edtContent;


    //constructor

    //method g/s

    //method general
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWrite = (Button) findViewById(R.id.btnWrite);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtContent = (EditText) findViewById(R.id.edtContent);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("방명록쓰기");

        //저장버튼 클릭할때
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("javaStudy", "btnWrite onClick: ");

                // vo로만들기
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String content = edtContent.getText().toString();

                GuestbookVo guestbookVo = new GuestbookVo(name, password, content);
                Log.d("javaStudy", "vo = " + guestbookVo.toString());
                //서버로 전송
                Log.d("javaStudy", "서버 전송");
                WriteAsyncTask writeAsyncTask = new WriteAsyncTask();
                writeAsyncTask.execute(guestbookVo);




            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("javaStudy", "onOptionsItemSelected: ");


        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    //innerclass
    public class WriteAsyncTask extends AsyncTask<GuestbookVo, Intent, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(GuestbookVo... guestbookVos) {
            Log.d("javaStudy", "doInBackground: ");
            Log.d("javaStudy", "Vo : "+guestbookVos[0].toString());

            //vo --> json
            Gson gson = new Gson();
            String json = gson.toJson(guestbookVos[0]);

            try {

                //접속정보보
               URL url = new URL("http://192.168.35.166:8088/mysite5/api/guestbook/write2"); //url 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                //outputStream (json --> body)
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                bw.write(json);
                bw.flush();

                int resCode = conn.getResponseCode(); // 응답코드 200이 정상
                Log.d("javaStudy", "doInBackground: "+resCode);
                if (resCode == HttpURLConnection.HTTP_OK) { //정상이면
                    //Stream 을 통해 통신한다
                    //데이타 형식은 json으로 한다.
                    /*
                    //리스트 액티비티로 전환
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(intent);
                     */
                    //자신의액티비티종료
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Intent... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


}