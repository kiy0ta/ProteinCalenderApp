package com.kiyokiyo.proteincalenderapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.kiyokiyo.proteincalenderapp.constants.DefaultData;
import com.kiyokiyo.proteincalenderapp.constants.ProteinCalendarContract;
import com.kiyokiyo.proteincalenderapp.dto.ProteinEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiyota on 2019/07/12.
 */

public class ProteinCalendarDao {

    private Context mContext;

    public ProteinCalendarDao(Context context) {
        mContext = context;
    }

    //プロテイン画像を押下したら、日付とタイプを登録する
    public void registProteinAllInfo(String date, String type) {
        ContentValues values = new ContentValues();
        values.put(ProteinCalendarContract.Input.RECORD_DATE, date);
        values.put(ProteinCalendarContract.Input.RECORD_TYPE, type);
        values.put(ProteinCalendarContract.Input.RECORD_PRICE, DefaultData.defaultPrice);
        values.put(ProteinCalendarContract.Input.RECORD_BOTTLE, DefaultData.defaultBottle);
        values.put(ProteinCalendarContract.Input.RECORD_PROTEIN, DefaultData.defaultProtein);
        mContext.getContentResolver().insert(ProteinCalendarContract.Input.CONTENT_URI, values);
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
                ProteinCalendarContract.Input._ID,
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
                    entity.setId(cur.getInt(0));
                    entity.setDrinkingDayString(cur.getString(1));
                    //Stringで取得した「日付」をDateに変換して、Dateの「日付」にsetする
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    // Date型変換
                    java.util.Date formatDate = sdf.parse(entity.getDrinkingDayString());
                    entity.setDrinkingDay(formatDate);
                    entity.setProteinType(cur.getInt(2));
                    entity.setPrice(cur.getInt(3));
                    entity.setBottle(cur.getInt(4));
                    entity.setProtein(cur.getInt(5));
                    list.add(entity);
                }
            }
        }
        //return nullにすると落ちる
        return list;
    }

    //DBに入っているデータをすべて取得してEntityに格納する
    public List<ProteinEntity> selectCurrentMonth(String titleMonth) throws ParseException {
        List<ProteinEntity> list = new ArrayList<ProteinEntity>();
        String[] projection = new String[]{
                ProteinCalendarContract.Input._ID,
                ProteinCalendarContract.Input.RECORD_DATE,
                ProteinCalendarContract.Input.RECORD_TYPE,
                ProteinCalendarContract.Input.RECORD_PRICE,
                ProteinCalendarContract.Input.RECORD_BOTTLE,
                ProteinCalendarContract.Input.RECORD_PROTEIN
        };

        try (Cursor cur = mContext.getContentResolver().query(ProteinCalendarContract.Input.CONTENT_URI, projection,
                ProteinCalendarContract.Input.RECORD_DATE + " like ?" , new String[]{titleMonth + "%"} , null)) {

            if (cur != null && cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    ProteinEntity entity = new ProteinEntity();
                    entity.setId(cur.getInt(0));
                    entity.setDrinkingDayString(cur.getString(1));
                    //Stringで取得した「日付」をDateに変換して、Dateの「日付」にsetする
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    // Date型変換
                    java.util.Date formatDate = sdf.parse(entity.getDrinkingDayString());
                    entity.setDrinkingDay(formatDate);
                    entity.setProteinType(cur.getInt(2));
                    entity.setPrice(cur.getInt(3));
                    entity.setBottle(cur.getInt(4));
                    entity.setProtein(cur.getInt(5));
                    list.add(entity);
                }
            }
        }
        //return nullにすると落ちる
        return list;
    }

    //特定の日付のTypeの値を取得する
    public List<ProteinEntity> selectTypeDate(String date) throws ParseException {
        List<ProteinEntity> list = new ArrayList<ProteinEntity>();
        String[] projection = new String[]{

                ProteinCalendarContract.Input.RECORD_TYPE
        };

        try (Cursor cur = mContext.getContentResolver().query(ProteinCalendarContract.Input.CONTENT_URI, projection,
                ProteinCalendarContract.Input.RECORD_DATE + " like ?" , new String[]{date} , null)) {

            if (cur != null && cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    ProteinEntity entity = new ProteinEntity();
                    entity.setProteinType(cur.getInt(0));
                    list.add(entity);
                }
            }
        }
        //return nullにすると落ちる
        return list;
    }
}
