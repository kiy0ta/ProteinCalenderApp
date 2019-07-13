package com.example.kiyota.proteincalendarapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.kiyota.proteincalendarapp.constants.DefaultData;
import com.example.kiyota.proteincalendarapp.constants.ProteinCalendarContract;
import com.example.kiyota.proteincalendarapp.dto.ProteinEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kiyota on 2019/07/12.
 */

public class ProteinCalendarDao {

    private Context mContext;
    //カレンダーのフォーマット型
    private static final String CALENDAR_FORMAT = "yyyy/MM/dd";

    public ProteinCalendarDao(Context context) {
        mContext = context;
    }

    //プロテイン画像を押下したら、日付とタイプを登録する
//    public void registProteinAllInfo(Date date,int type){
    public void registProteinAllInfo(String date, int type) {

        ContentValues values = new ContentValues();
        values.put(ProteinCalendarContract.Input.RECORD_DATE, date);
        values.put(ProteinCalendarContract.Input.RECORD_TYPE, type);
        values.put(ProteinCalendarContract.Input.RECORD_PRICE, DefaultData.defaultPrice);
        values.put(ProteinCalendarContract.Input.RECORD_BOTTLE, DefaultData.defaultBottle);
        values.put(ProteinCalendarContract.Input.RECORD_PROTEIN, DefaultData.defaultProtein);
        mContext.getContentResolver().insert(ProteinCalendarContract.Input.CONTENT_URI,
                values);
    }

    //DBの中のデータ数をカウントする処理
    //DBに入っているデータ件数を取得する
    public int getDataCount() {
        String[] projection = new String[]{
                ProteinCalendarContract.Input.RECORD_DATE,
                ProteinCalendarContract.Input.RECORD_TYPE,
                ProteinCalendarContract.Input.RECORD_PRICE,
                ProteinCalendarContract.Input.RECORD_BOTTLE,
                ProteinCalendarContract.Input.RECORD_PROTEIN
        };

        try (Cursor cur = mContext.getContentResolver().query(ProteinCalendarContract.Input.CONTENT_URI, projection,
                null, null, null)) {
            //DBの中に入っている件数を取得する
            if (cur != null && cur.getCount() > 0) {
                return cur.getCount();
            }
            return 0;
        }
    }

    //DBに入っているデータをすべて取得してEntityに格納する
    public List<ProteinEntity> selectAll() throws ParseException {
        List<ProteinEntity> list = new ArrayList<ProteinEntity>();
        String[] projection = new String[]{
                ProteinCalendarContract.Input.RECORD_DATE,
                ProteinCalendarContract.Input.RECORD_TYPE,
                ProteinCalendarContract.Input.RECORD_PRICE,
                ProteinCalendarContract.Input.RECORD_BOTTLE,
                ProteinCalendarContract.Input.RECORD_PROTEIN
        };

        try (Cursor cur = mContext.getContentResolver().query(ProteinCalendarContract.Input.CONTENT_URI, projection,
                null, null, null)) {

            if (cur != null && cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    ProteinEntity entity = new ProteinEntity();
                    entity.setDrinkingDayString(cur.getString(0));
                    //Stringで取得した「日付」をDateに変換して、Dateの「日付」にsetする
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    // Date型変換
                    Date formatDate = sdf.parse(entity.getDrinkingDayString());
                    entity.setDrinkingDay(formatDate);
                    entity.setProteinType(cur.getInt(1));
                    entity.setPrice(cur.getInt(2));
                    entity.setBottle(cur.getInt(3));
                    entity.setProtein(cur.getInt(4));
                    list.add(entity);
                }
                return list;
            }
        }
        return null;

    }
}
