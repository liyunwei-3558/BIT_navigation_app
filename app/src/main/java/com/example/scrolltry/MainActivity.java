package com.example.scrolltry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText input_s;
    private EditText input_t;
    SpfaPlanner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO:在应用打开时就读好链式前向星
        sp = new SpfaPlanner(this);
        sp.init();
        setContentView(R.layout.activity_main);
    }

    public void btn_Callback(View view){
//        Log.e("TAG", "search点击成功！");
        input_s = (EditText)findViewById(R.id.start_input);
        input_t = (EditText)findViewById(R.id.end_input);


        //Toast.makeText(this, "进入规划:"+input_s.getText().toString()+"去"+input_t.getText().toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,ScrollingActivity.class);//需要传递字符串集，或者直接对数据库进行搜索后传id
        intent.putExtra("start",input_s.getText().toString());
        intent.putExtra("end",input_t.getText().toString());
        startActivity(intent);
    }



}