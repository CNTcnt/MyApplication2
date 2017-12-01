package com.cnt.myapplication2;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class Edit_Activity extends AppCompatActivity {
    Button backButton;
    Button okButton;
    TextView noteName_TextView;
    TextView timeYMD_textView;
    TextView timeHM_textView;
    android.support.v7.widget.AppCompatEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            //当sdk>=21时，要实现沉浸式状态栏，背景要延生到状态栏，由于我的背影是图片，会有偏差，所以我弄成2个布局，各有2张不同的图片
            setContentView(R.layout.activity_edit_);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            setContentView(R.layout.test);
        }

        backButton = (Button) findViewById(R.id.edit_backButton);
        okButton = (Button) findViewById(R.id.OKButton);
        noteName_TextView = (TextView) findViewById(R.id.editActivity_noteName);
        timeYMD_textView = (TextView) findViewById(R.id.timeYMD_textView);
        timeHM_textView = (TextView) findViewById(R.id.timeHM_textView);
        editText = (AppCompatEditText) findViewById(R.id.editActivity_editText);

        initTime();//时间更新；

        noteName_TextView.setText(getIntent().getStringExtra("noteName"));//得到dialog得到的名字

        backButton.setOnClickListener(new View.OnClickListener() {//返回按钮
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {//完成按钮
            @Override
            public void onClick(View v) {
                noteName note = new noteName();
                note.setName(noteName_TextView.getText().toString());
                note.setNote_date(timeYMD_textView.getText().toString());
                note.setNote_xingqi(timeHM_textView.getText().toString());
                note.setNoteBody(editText.getText().toString());
                note.save();
                finish();
            }
        });
    }

    private void initTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf.format(new java.util.Date());
        timeYMD_textView.setText(date1);
        SimpleDateFormat sdff = new SimpleDateFormat("E");
        String date2 = sdff.format(new java.util.Date());
        timeHM_textView.setText(date2);
    }
}
