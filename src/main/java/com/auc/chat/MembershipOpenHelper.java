package com.auc.chat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MembershipOpenHelper extends SQLiteOpenHelper {

    Context context;

    public MembershipOpenHelper(Context context) {
        super(context, "member.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String sql = "CREATE TABLE membership(" +
                    "email varchar(40) not null primary key," +
                    "pwd varchar(72) not null" +
                    ");";
            db.execSQL(sql);
            Toast.makeText(context, "[membership] 테이블 생성", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
