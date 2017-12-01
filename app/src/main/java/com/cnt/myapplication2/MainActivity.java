package com.cnt.myapplication2;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    List<noteName> contactsList = new ArrayList<>();//recycle的数据list
    public MyAdapter contactsAdaper;//recycle的适配器
    RecyclerView recyclerView;
    ItemTouchHelper helper;//recycle的子项滑动拖动帮助类
    CircleImageView fab;//添加按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        boolean if_first = pref.getBoolean("if_first", false);
        if (if_first == false) {//第一次安装应用时，在数据库中添加一条初始数据
            noteName note = new noteName();
            note.setName("随笔使用手册");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date1 = sdf.format(new java.util.Date());
            note.setNote_date(date1);
            SimpleDateFormat sdff = new SimpleDateFormat("E");
            String date2 = sdff.format(new java.util.Date());
            note.setNote_xingqi(date2);
            note.setNoteBody("                      随笔使用手册\n1. 点击界面下面的\"+\"可以出新建一个小本.\n2. 长按左右拖动调整小本位置.\n3. 上下滑动删除不要的小本.\n4. 然后也没什么要特别说明的，只希望轻松简单的界面能够给你舒服的环境写上生活的记录.\n5. 还有，这是我用课余时间写的一个小便签应用，算是对自己学习上的一个检验.非常开心你使用我的软件，这是对我莫大的鼓励.\n6. 祝你使用愉快.^_^");
            note.save();
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("if_first", true);
            editor.apply();
        } else {
        }


        fab = (CircleImageView) findViewById(R.id.circle_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, Edit_Activity.class);
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                final View buttonLayout = layoutInflater.inflate(R.layout.dialog_layout, null);
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this).setView(buttonLayout);

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) buttonLayout.findViewById(R.id.notename_editText);
                        String noteName = editText.getText().toString();
                        if (!noteName.isEmpty()) {
                            intent.putExtra("noteName", noteName);
                            startActivityForResult(intent, 1);
                        } else {
                            intent.putExtra("noteName", "随笔");
                            startActivityForResult(intent, 1);
                        }

                    }
                });
                AppCompatImageButton cancelButton = (AppCompatImageButton) buttonLayout.findViewById(R.id.dialog_cencel_button);
                final Dialog dialog1 = dialog.show();
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.cancel();
                    }
                });
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        initList();//初始化recycle中的数据
        contactsAdaper = new MyAdapter(contactsList);
        recyclerView.setAdapter(contactsAdaper);
        helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            //左右拖动事件
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                Collections.swap(contactsList, from, to);
                contactsAdaper.notifyItemMoved(from, to);
                contactsAdaper.notifyDataSetChanged();
                //修改数据库
                noteName tempNote = new noteName();
                List<noteName> list = DataSupport.findAll(noteName.class);
                int listSize = list.size();
                noteName fromNote = list.get(listSize - from - 1);
                if (from < to) {
                    tempNote.setName(fromNote.getName());
                    tempNote.setNoteBody(fromNote.getNoteBody());
                    tempNote.setNote_xingqi(fromNote.getNote_xingqi());
                    tempNote.setNote_date(fromNote.getNote_date());
                    noteName toNote = list.get(listSize - from - 2);
                    fromNote.setName(toNote.getName());
                    fromNote.setNoteBody(toNote.getNoteBody());
                    fromNote.setNote_xingqi(toNote.getNote_xingqi());
                    fromNote.setNote_date(toNote.getNote_date());
                    fromNote.save();
                    toNote.setName(tempNote.getName());
                    toNote.setNote_date(tempNote.getNote_date());
                    toNote.setNoteBody(tempNote.getNoteBody());
                    toNote.setNote_xingqi(tempNote.getNote_xingqi());
                    toNote.save();
                } else if (to < from) {
                    tempNote.setName(fromNote.getName());
                    tempNote.setNoteBody(fromNote.getNoteBody());
                    tempNote.setNote_xingqi(fromNote.getNote_xingqi());
                    tempNote.setNote_date(fromNote.getNote_date());
                    noteName toNote = list.get(listSize - from);
                    fromNote.setName(toNote.getName());
                    fromNote.setNoteBody(toNote.getNoteBody());
                    fromNote.setNote_xingqi(toNote.getNote_xingqi());
                    fromNote.setNote_date(toNote.getNote_date());
                    fromNote.save();
                    toNote.setName(tempNote.getName());
                    toNote.setNote_date(tempNote.getNote_date());
                    toNote.setNoteBody(tempNote.getNoteBody());
                    toNote.setNote_xingqi(tempNote.getNote_xingqi());
                    toNote.save();
                }
                return true;
            }

            //上下滑动事件
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                noteName note = contactsList.get(position);
                contactsList.remove(position);
                String id = note.getId() + "";
                contactsAdaper.notifyItemRemoved(position);
                List<noteName> list = DataSupport.where("id=?", id).find(noteName.class);
                list.get(0).delete();
                note.delete();
                contactsAdaper.notifyDataSetChanged();
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        });
        helper.attachToRecyclerView(recyclerView);

    }

    private void initList() {
        List<noteName> list = DataSupport.findAll(noteName.class);
        contactsList.clear();
        int count = list.size();
        for (; count > 0; count--) {
            contactsList.add(list.get(count - 1));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                contactsList.clear();
                List<noteName> list = DataSupport.findAll(noteName.class);
                contactsList.clear();
                int count = list.size();
                for (; count > 0; count--) {
                    contactsList.add(list.get(count - 1));
                }
                contactsAdaper.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //之所以要复写这个方法，是因为recycle的子项点击事件触发的是startActivity(reWriteActivity)，当她结束时，数据是要更新的，所以要让recycle更新数据
        contactsList.clear();
        List<noteName> list = DataSupport.findAll(noteName.class);
        contactsList.clear();
        int count = list.size();
        for (; count > 0; count--) {
            contactsList.add(list.get(count - 1));
        }
        contactsAdaper.notifyDataSetChanged();
    }
}
