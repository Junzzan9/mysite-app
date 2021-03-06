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

            //gson ????????????
            Gson gson = new Gson();

            GuestbookVo requestVo = null;
            GuestbookVo responseVo = new GuestbookVo();


            int no = noArray[0];

            //????????????
            requestVo = new GuestbookVo();
            requestVo.setNo(no);


            String requestJson = gson.toJson(requestVo);
            Log.d("javaStudy", "requestJson: " + requestJson);



            try {
                URL url = new URL("http://192.168.35.166:8088/mysite5/api/guestbook/read"); //url ??????
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url ??????
                conn.setConnectTimeout(10000); // 10??? ?????? ????????? ??? ????????? ????????? ??????
                conn.setRequestMethod("POST"); // ???????????? POST
                conn.setRequestProperty("Content-Type", "application/json"); //????????? ????????? ?????? json
                conn.setRequestProperty("Accept", "application/json"); //????????? ????????? ?????? json
                conn.setDoOutput(true); //OutputStream?????? POST ???????????? ?????????????????? ??????.
                conn.setDoInput(true); //InputStream?????? ????????? ?????? ????????? ???????????? ??????.

                //REQUEST ??????
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                bw.write(requestJson);
                bw.flush();


                int resCode = conn.getResponseCode(); // ???????????? 200??? ??????
                Log.d("javaStudy", "resCode: " + resCode);


                if(conn.getResponseCode()==200){

                //RESPONSE ??????
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

                    //json ??????
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