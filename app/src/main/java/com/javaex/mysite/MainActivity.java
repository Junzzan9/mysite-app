package com.javaex.mysite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.javaex.vo.GuestbookVo;

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

        btnWrite = (Button)findViewById(R.id.btnWrite);
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

                GuestbookVo guestbookVo = new GuestbookVo(name,password,content);
                Log.d("javaStudy", "vo = "+guestbookVo.toString());
                //서버로 전송
                Log.d("javaStudy", "서버 전송");

                //리스트 액티비티로 전환
                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("javaStudy", "onOptionsItemSelected: ");


        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }




}