package com.cnt.myapplication2;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.List;

public class reWriteActivity extends AppCompatActivity {
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

        init();//初始化；

        backButton.setOnClickListener(new View.OnClickListener() {//返回按钮
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {//完成按钮
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date1 = sdf.format(new java.util.Date());
                SimpleDateFormat sdff = new SimpleDateFormat("E");
                String date2 = sdff.format(new java.util.Date());
                String id = getIntent().getIntExtra("id", -1) + "";
                List<noteName> note = DataSupport.where("id=?", id).find(noteName.class);
                note.get(0).delete();//把更新的数据行放在第一个
                noteName newNote = new noteName();
                newNote.setName(getIntent().getStringExtra("noteName"));
                newNote.setNote_date(date1);
                newNote.setNote_xingqi(date2);
                newNote.setNoteBody(editText.getText().toString());
                newNote.save();
                finish();
            }
        });
    }

    private void init() {
        noteName_TextView.setText(getIntent().getStringExtra("noteName"));//得到dialog得到的名字
        timeYMD_textView.setText(getIntent().getStringExtra("date"));
        timeHM_textView.setText(getIntent().getStringExtra("xingqi"));
        editText.setText(getIntent().getStringExtra("noteBody"));
        editText.setSelection(getIntent().getStringExtra("noteBody").length());//设置editView光标的实时位置
    }
}
