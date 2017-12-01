package com.cnt.myapplication2;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/25.
 */

public class noteName extends DataSupport {
    private int id;
    private String noteName;//note名字
    private String noteBody;//note主体
    private String note_date;//日期
    private String note_xingqi;//周几

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }

    public String getNote_xingqi() {
        return note_xingqi;
    }

    public void setNote_xingqi(String note_xingqi) {
        this.note_xingqi = note_xingqi;
    }


    public void setName(String name){
        this.noteName = name;
    }
    public String getName(){
        return noteName;
    }
}
