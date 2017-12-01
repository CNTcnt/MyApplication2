package com.cnt.myapplication2;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/25.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<noteName> list_data;

    public MyAdapter(List<noteName> noteNames) {
        list_data = noteNames;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        noteName note_name = list_data.get(position);
        holder.textView.setText(note_name.getName());
        String[] date = note_name.getNote_date().split("-");//这里错
        Log.d("aaa", "onBindViewHolder: " + note_name.getNote_date());
        holder.MDtimeTextView.setText(date[1] + "." + date[2]);
        holder.xingqiTextView.setText(note_name.getNote_xingqi());
        holder.itemView.setTag(position);//让每个子项都获取到他自己的位置,然后可以通过它来找到数据库中的；
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);
        //子项点击事件
        viewHolder.itemView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteName note = list_data.get((int) v.getTag());
                Intent intent = new Intent(v.getContext(), reWriteActivity.class);
                intent.putExtra("noteName", note.getName());
                intent.putExtra("noteBody", note.getNoteBody());
                intent.putExtra("xingqi", note.getNote_xingqi());
                intent.putExtra("date", note.getNote_date());
                intent.putExtra("id", note.getId());
                v.getContext().startActivity(intent);
            }
        });


        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView MDtimeTextView;
        TextView xingqiTextView;
        TextView timeTextView;
        View itemView1;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView1 = itemView;
            textView = (TextView) itemView.findViewById(R.id.item_textView);
            MDtimeTextView = (TextView) itemView.findViewById(R.id.MDtime_item_textView);
            xingqiTextView = (TextView) itemView.findViewById(R.id.xingqi_item_textView);
        }
    }
}
